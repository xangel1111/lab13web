package com.tecsup.controller;

import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Producto;
import com.tecsup.resository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class ProductoController {

    @Autowired
    ProductoRepository productoRepository;

    @GetMapping("/productos")
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @PostMapping("/productos")
    public ResponseEntity<Producto> save(@RequestBody Producto producto) {
        Producto newProduct = productoRepository.save(producto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newProduct.getId())
                .toUri();

        return ResponseEntity.created(location).body(newProduct);
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<Producto> findById(@PathVariable long id) {
        if(!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("ProductNotFound UsingID: " + id);
        }
        Producto producto = productoRepository.findById(id).orElse(null);
        return ResponseEntity.ok(producto);
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<Producto> update(@PathVariable long id, @RequestBody Producto producto) {
        if(!productoRepository.existsById(id)) {
            System.out.println("Xd");
            throw new ResourceNotFoundException("ProductNotFound UsingID: " + id);
        }
        producto.setId(id);
        productoRepository.save(producto);
        return ResponseEntity.ok(producto);
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteById(@PathVariable long id) {
        if(!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("ProductNotFound UsingID: " + id);
        }
        productoRepository.deleteById(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    // RESPONSE ENTITY PRACTICE

    @GetMapping("/test/response_entity_ok")
    public ResponseEntity<String> getMessage() {
        String msg = "Hello World";
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/test/response_entity")
    public ResponseEntity<String> getMessageFull() {
        HttpHeaders header = new HttpHeaders();
        header.put("headerTest", new ArrayList<>(List.of("HeaderTestValue1", "HeaderTestValue2")));
        String msg = "Hello World" ;
        return new ResponseEntity<>(msg, header, HttpStatus.FOUND);
    }

    @GetMapping("/test/default")
    public String getMessageDefault() {
        return "Hello World";
    }
}
