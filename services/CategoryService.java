package com.TrendHive.TrendHive.services;

import com.TrendHive.TrendHive.dto.CategoryDto.CategoryRequestDto;
import com.TrendHive.TrendHive.dto.CategoryDto.CategoryResponseDto;
import com.TrendHive.TrendHive.entities.Category;
import com.TrendHive.TrendHive.entities.Product;
import com.TrendHive.TrendHive.repository.CategoryRepository;
import com.TrendHive.TrendHive.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getAll(){
        return categoryRepository.findAll();
    }

//    Creation of Category
    public Category create(Category category){
        if(categoryRepository.findByName(category.getName()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Category with name "+category.getName()+" is already exist");
        }
        return categoryRepository.save(category);
    }

//    Update category By name:-

    public void deleteCaategoryByName(String name){
        Category category = categoryRepository.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"Category with name "+name+" not found"));
        Page<Product> products = productRepository.findByCategoriesName(name, Pageable.unpaged());
        if (!products.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Category with name "+name+" has post reference so you cannot delete it");
        }
        categoryRepository.deleteByName(name);
    }

    public Category updateByName(String name, CategoryRequestDto categoryRequestDto){
        String newName =categoryRequestDto.getName();
        Category categoryName = categoryRepository.findByName(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category with the name "+name+" not found"));
        categoryName.setName(newName);
        return categoryRepository.save(categoryName);
    }

    public CategoryResponseDto convertToCategoryResponseDto(Category category){
        return new CategoryResponseDto(category.getId(),category.getName());
    }

    public Category convertToCategory(CategoryRequestDto categoryRequestDto){
        return new Category(0,categoryRequestDto.getName());
    }
}
