package com.TrendHive.TrendHive.dto.UserDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPartialRequestDto {

    @Size(min = 3,max = 20, message = "Username must between 3 and 20 characters")
    private String username;

    @Size(min = 6, max = 15, message = "Password must be between 6 and 15 characters")
    private String password;

    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "address cannot be empty")
    @Size(min = 1,max = 150, message = "Address must be between 1 to 150 characters")
    private String address;

//    @NotBlank
//    @Size(max = 10,message = "Phone number must be of 10 characters")
//    private Long phonenumber;
}
