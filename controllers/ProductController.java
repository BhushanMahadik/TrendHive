package com.TrendHive.TrendHive.controllers;

import com.TrendHive.TrendHive.dto.ProductDto.ProductRequestDto;
import com.TrendHive.TrendHive.dto.ProductDto.ProductResponseDto;
import com.TrendHive.TrendHive.entities.Product;
import com.TrendHive.TrendHive.repository.ProductRepository;
import com.TrendHive.TrendHive.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<?> uploadImage(@PathVariable int id, @RequestParam("image") MultipartFile file) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            product.setImage(file.getBytes());
            productRepository.save(product);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading image: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Determine the content type based on your image (adjust if needed)
        MediaType mediaType = MediaType.IMAGE_JPEG; // Change to IMAGE_PNG if necessary

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(product.getImage());
    }

    @DeleteMapping("/{id}/deleteImage")
    public ResponseEntity<String> deleteImage(@PathVariable int id) {
        // Retrieve the product by its id
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        Product product = productOptional.get();

        // Remove the image by setting the image field to null
        product.setImage(null);
        productRepository.save(product);

        return ResponseEntity.ok("Image deleted successfully");
    }

//    @PostMapping("/user/{userId}")
//    public ResponseEntity<Product> addProduct(@PathVariable int userId, @RequestBody Product product) {
//        return ResponseEntity.ok(productService.saveProduct(product, userId));
//    }

    @PostMapping(value = "user/{userId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ProductResponseDto> createProduct(@PathVariable int userId,@Valid @ModelAttribute ProductRequestDto productRequestDto){
        Product product = productService.convertToProduct(productRequestDto);
        Product createdProduct = productService.create(product, userId);
        return new ResponseEntity<ProductResponseDto>(productService.convertToProductResponseDto(createdProduct), HttpStatus.CREATED);
    }

    @GetMapping("")
    public  ResponseEntity<Page<ProductResponseDto>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        Page<ProductResponseDto> products = productService.getAll(page, size, sortDirection, sortBy)
                .map(productService::convertToProductResponseDto);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getPoroductById(@PathVariable int id){
        return new ResponseEntity<ProductResponseDto>(productService.convertToProductResponseDto
                (productService.getProduct(id)),HttpStatus.OK);
    }

    @GetMapping("title/{title}")
    public ResponseEntity<Page<ProductResponseDto>> getPostByTitle(
            @PathVariable String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Page<ProductResponseDto> product = productService.getByTitle(title,page, size, sortDirection, sortBy)
                .map(productService::convertToProductResponseDto);
        return ResponseEntity.ok(product);
    }

    @GetMapping("categoryName/{category}")
    public ResponseEntity<Page<ProductResponseDto>> getPostByTag(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Page<ProductResponseDto> products = productService.getByTag(category,page, size, sortDirection, sortBy)
                .map(productService::convertToProductResponseDto);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}/merchants/{merchantId}")
    public ResponseEntity<ProductResponseDto> updateProductById(@PathVariable int id,@Valid @RequestBody ProductRequestDto productRequestDto,
                                                                @PathVariable int merchantId){
        Product productResponse = productService.update(id, productService.convertToProduct(productRequestDto), merchantId);
        return new ResponseEntity<ProductResponseDto>(productService.convertToProductResponseDto(productResponse),HttpStatus.OK);
    }

    @DeleteMapping("/{id}/merchants/{userId}")
    public ResponseEntity<?> deletePostById(@PathVariable int id, @PathVariable int userId){
        productService.delete(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
