package com.example.shopapp.controller;

import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ProductImageDTO;
import com.example.shopapp.dto.response.ProductListResponse;
import com.example.shopapp.dto.response.ProductResponse;
import com.example.shopapp.entity.Product;
import com.example.shopapp.entity.ProductImage;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.service.ProductService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result){
        try {
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            Product newProduct = productService.createProduct(productDTO);

            return ResponseEntity.ok(newProduct);
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files
    ){
        try {
            Product existingProduct = productService.getProductById(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;

            if(files.size() > ProductImage.MAX_IMG_OF_PRODUCT){
                return ResponseEntity.badRequest().body("Maximum 5 images are allowed");
            }

            List<ProductImage> productImages = new ArrayList<>();
            for(MultipartFile file : files) {

                if (file.getSize() == 0) {
                    continue;
                }

                //Check size and format of file
                if (file.getSize() > 10 * 1024 * 1024) {
                    //throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File is too large, Maximum size is 10MB");
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large, Maximum size is 10MB");
                }

                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }

                // Store file
                String fileName = storeFile(file);
                ProductImage productImage = productService.createProductImage(existingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(fileName)
                                .build());

                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    private String storeFile(MultipartFile file) throws IOException {

        if(!isImageFile(file) || file.getOriginalFilename() == null){
            throw new IOException("File is not an image");
        }

        // Normalize file name
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
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

    private boolean isImageFile(MultipartFile file){
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @GetMapping
    public ResponseEntity<ProductListResponse> getAllProduct(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        int totalPage = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(products)
                        .totalPages(totalPage)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable Long id){
        return ResponseEntity.ok("Product with ID: " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        return ResponseEntity.ok("Delete Product successfully!");
    }

    //@PostMapping("generateFakeProducts")
    private ResponseEntity<String> genarateFakeProducts(){
        Faker faker = new Faker();
        for(int i = 0; i < 1000; i++){
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)){
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(10, 1000))
                    .description(faker.lorem().sentence())
                    .categoryId((long)faker.number().numberBetween(1, 7))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake products are generated successfully!");
    }
}
