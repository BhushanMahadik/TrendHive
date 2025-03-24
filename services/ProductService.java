package com.TrendHive.TrendHive.services;

import com.TrendHive.TrendHive.dto.ProductDto.ProductRequestDto;
import com.TrendHive.TrendHive.dto.ProductDto.ProductResponseDto;
import com.TrendHive.TrendHive.entities.Category;
import com.TrendHive.TrendHive.entities.Merchant;
import com.TrendHive.TrendHive.entities.Product;
import com.TrendHive.TrendHive.entities.User;
import com.TrendHive.TrendHive.repository.CategoryRepository;
import com.TrendHive.TrendHive.repository.MerchantRepository;
import com.TrendHive.TrendHive.repository.ProductRepository;
import com.TrendHive.TrendHive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MerchantService merchantService;

//    public Product saveProduct(Product product, int userId) {
//        Optional<User> userOpt = userRepository.findById(userId);
//        if (userOpt.isEmpty()) {
//            throw new RuntimeException("User not found!");
//        }
//
//        product.setUser(userOpt.get()); // 🔴 Ensure User is set before saving
//        return productRepository.save(product);
//    }

    public Product create(Product product,int userId){
//        Set<Category> persistedCategory;
//        if (product.getCategories() != null && !product.getCategories().isEmpty()){
//            persistedCategory = product.getCategories().stream().map(category ->
//            {
//                return categoryRepository.findByName(category.getName()).orElseGet(() -> categoryRepository.save(category));
//            }
//            ).collect(Collectors.toSet());
//            product.setCategories(persistedCategory);
//        }
//        return productRepository.save(product);
        Merchant merchant = merchantService.getById(userId);
        product.setMerchant(merchant);
        product.setCategories(getPersistedCategorys(product.getCategories()));
        Product saveProduct = productRepository.save(product);
        return saveProduct;
    }

    private Set<Category> getPersistedCategorys(Set<Category> categories){
        if (categories == null || categories.isEmpty()){
            return new HashSet<>();
        }
        return categories.stream()
                .map(category -> categoryRepository.findByName(category.getName()).
                        orElseGet(() ->categoryRepository.save(category)))
                .collect(Collectors.toSet());
    }

//    @PreAuthorize("hasRole('ROLE_USER')")
    public Page<Product> getAll(int page, int size, String sortDirection, String sortBy){
        Pageable pageable = PageRequest.of(page , size, Sort.by(Sort.Direction.fromString(sortDirection),sortBy));
        return productRepository.findAll(pageable);
    }

    public Product getProduct(int id){
        return productRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Post with id "+id+" not found")
        );
    }

    public Page<Product> getByTitle(String title,int page, int size, String sortDirection, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection),sortBy));
        return productRepository.findByTitleContaining(title,pageable);
    }

    public Page<Product> getByTag(String category,int page, int size, String sortDirection, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection),sortBy));
        return productRepository.findByCategoriesName(category,pageable);
    }

    public List<Product> getMerchantProducts(int merchantId){
        Merchant merchant = merchantService.getById(merchantId);
        return merchant.getProducts();
    }

    @Transactional
    public Product update(int id,Product updateProduct, int merchantId){
        Merchant merchant = merchantService.getById(merchantId);
        Product merchantProduct = merchant.getProducts().stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product with id "+id+" not found"));

        if (updateProduct.getTitle() != null){
            merchantProduct.setTitle(updateProduct.getTitle());
        }
        if(updateProduct.getDescription() != null){
            merchantProduct.setDescription(updateProduct.getDescription());
        }
        if (updateProduct.getStockQuantity() != null){
            merchantProduct.setStockQuantity(updateProduct.getStockQuantity());
        }
        if(!updateProduct.getCategories().isEmpty()){
            merchantProduct.setCategories(getPersistedCategorys(updateProduct.getCategories()));
        }
        return productRepository.save(merchantProduct);
    }
    private Set<Category> getPersistedCategory(Set<Category> categories){
        if (categories == null || categories.isEmpty()){
            return new HashSet<>();
        }
        return categories.stream()
                .map( category -> categoryRepository.findByName(category.getName()).orElseGet(() -> categoryRepository.save(category)))
                .collect(Collectors.toSet());
    }



    @Transactional
    public void delete(int id, int userId){
        Merchant merchant = merchantService.getById(userId);
        Product merchantProduct = merchant.getProducts().stream().filter(product -> product.getId() == id)
                .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Post with id "+id+" not found"));
        merchantProduct.setCategories(new HashSet<>());

        merchant.getProducts().removeIf(product -> product.getId() == id);

        merchantRepository.save(merchant);

        productRepository.deleteById(id);
    }

    public ProductResponseDto convertToProductResponseDto(Product product){
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(product.getId());
        productResponseDto.setTitle(product.getTitle());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setCreatedDate(product.getCreatedDate());
        productResponseDto.setLastModifiedDate(product.getLastModifiedDate());
        productResponseDto.setCategories(product.getCategories().stream().map(Category::getName).collect(Collectors.toSet()));
        productResponseDto.setMerchant(product.getMerchant().getMerchantName());
        // If image exists, encode it to Base64 and set in the DTO
        if (product.getImage() != null && product.getImage().length > 0) {
            // Construct a URL based on your application's domain and endpoint
            String imageUrl = "http://localhost:8081/dev/api/TrendHive/products/" + product.getId() + "/image";
            productResponseDto.setImage(imageUrl);
        }
        return productResponseDto;
    }

    public Product convertToProduct(ProductRequestDto productRequestDto){
        Product product = new Product();
        product.setTitle(productRequestDto.getTitle());
        product.setDescription(productRequestDto.getDescription());
        product.setStockQuantity(productRequestDto.getStockQuantity());
        product.setPrice(productRequestDto.getPrice());
        product.setCategories(productRequestDto.getCategories().stream().map(name -> new Category(name)).collect(Collectors.toSet()));
        MultipartFile imageFile = productRequestDto.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                product.setImage(imageFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to process image", e);
            }
        }
        return product;
    }
}
