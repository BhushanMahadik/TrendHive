package com.TrendHive.TrendHive.dto.MerchantDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantPartialRequestDto {

    @Size(min = 3,max = 20, message = "Username must between 3 and 20 characters")
    private String merchantName;

    @Size(min = 6, max = 15, message = "Password must be between 6 and 15 characters")
    private String password;

    @Email(message = "Email should be valid")
    private String email;
}
