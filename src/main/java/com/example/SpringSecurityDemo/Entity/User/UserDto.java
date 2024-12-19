package com.example.SpringSecurityDemo.Entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String userID ;


    @NotBlank(message = "Username is required and cannot be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9_]*$",
            message = "Username can only contain letters, numbers, and underscores"
    )
    private String username;
    @NotBlank(message = "nomColombie is required and cannot be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9_]*$",
            message = "nomColombie can only contain letters, numbers, and underscores"
    )
    private String nomColombie;

    private double latitude; // GPS coordinates
    private double longitude;



    @NotBlank(message = "Password is required and cannot be blank")
    @Size(min = 6, message = "Minimum password length is 6 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;

}
