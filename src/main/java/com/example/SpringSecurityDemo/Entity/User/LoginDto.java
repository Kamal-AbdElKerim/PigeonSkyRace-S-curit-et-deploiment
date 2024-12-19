package com.example.SpringSecurityDemo.Entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {


    @NotBlank(message = "nomColombie is required and cannot be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")

    private String nomColombie;


    @NotBlank(message = "Password is required and cannot be blank")

    private String password;

}
