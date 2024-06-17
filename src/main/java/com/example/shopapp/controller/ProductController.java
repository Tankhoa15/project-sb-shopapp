package com.example.shopapp.controller;

import com.example.shopapp.dto.ProductDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
            ){
        try {
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            return ResponseEntity.ok("Product create successfully!");
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<String> getAllProduct(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        return ResponseEntity.ok(String.format("get all category, page = %d, limit = %d", page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable Long id){
        return ResponseEntity.ok("Product with ID: " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        return ResponseEntity.ok("Delete Product successfully!");
    }
}
