package com.TrendHive.TrendHive.dto.MerchantDto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
    private  String field;
    private String defaultMessage;
}
