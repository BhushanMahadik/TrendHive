package com.TrendHive.TrendHive.dto.MerchantDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantResponseDto {
    private int id;
    private String merchantName;
    private String password;
    private String email;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Set<String> roles;

}
