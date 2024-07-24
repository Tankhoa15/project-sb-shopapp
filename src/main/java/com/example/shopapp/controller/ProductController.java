package com.example.shopapp.controller;

import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ProductImageDTO;
import com.example.shopapp.entity.Product;
import com.example.shopapp.entity.ProductImage;
import com.example.shopapp.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @Valid @ModelAttribute ProductDTO productDTO,
            //@RequestPart("file") MultipartFile file,
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

            Product newProduct = productService.createProduct(productDTO);

            List<MultipartFile> files = productDTO.getFiles();
            files = files == null ? new ArrayList<MultipartFile>() : files;
            for(MultipartFile file : files){

                if(file.getSize() == 0){
                    continue;
                }

                //Check size and format of file
                if(file.getSize() > 10 * 1024 * 1024){
                    //throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File is too large, Maximum size is 10MB");
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large, Maximum size is 10MB");
                }

                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }

                // Store file
                String fileName = storeFile(file);
                ProductImage productImage = productService.createProductImage(newProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(fileName)
                                .build());
            }

            return ResponseEntity.ok("Product create successfully!");
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        // Normalize file name
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        // Generate unique file name
        String uniqueFileName = UUID.randomUUID().toString() + "_" + filename;
        // Create directory to save file
        Path uploadDir = Paths.get("uploads");
        // if directory not exist, create it
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // path to save file
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        // copy file to destination
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
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
