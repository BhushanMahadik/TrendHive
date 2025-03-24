package com.TrendHive.TrendHive.dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    int id;
    String title;
    String description;
    int price;
    Set<String> categories = new HashSet<>();
    LocalDateTime createdDate;
    LocalDateTime lastModifiedDate;
    String merchant;
    private String image;
}
