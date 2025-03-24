package com.TrendHive.TrendHive.dto.CartDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequestDto {
    @NotBlank(message = "title cannot be blank")
    @Size(min = 3, max = 100, message = "title must be between 3 to 100 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 5, max = 255, message = "Description must be between 5 to 255 characters")
    private String description;

    private int price;
    private Set<String> categories = new HashSet<>();
}
