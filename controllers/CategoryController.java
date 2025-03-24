package com.TrendHive.TrendHive.controllers;

import com.TrendHive.TrendHive.dto.CategoryDto.CategoryRequestDto;
import com.TrendHive.TrendHive.dto.CategoryDto.CategoryResponseDto;
import com.TrendHive.TrendHive.entities.Category;
import com.TrendHive.TrendHive.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryRequestDto categoryRequestDto){
        Category category = categoryService.convertToCategory(categoryRequestDto);
        Category createdCategory = categoryService.create(category);
        return new ResponseEntity<>(categoryService.convertToCategoryResponseDto(createdCategory),HttpStatus.CREATED);
    }

    @PutMapping("/{name}")
    public ResponseEntity<Category> updatedByCategoryName(@PathVariable String name, @RequestBody CategoryRequestDto categoryRequestDto){
        Category updatedCategory =categoryService.updateByName(name , categoryRequestDto);
        return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteByName(@PathVariable String name){
        categoryService.deleteCaategoryByName(name);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(){
        return new ResponseEntity<List<CategoryResponseDto>>(categoryService.getAll().stream().map(category ->
                categoryService.convertToCategoryResponseDto(category)).collect(Collectors.toList()), HttpStatus.OK);
    }
}
