package com.example.SpringSecurityDemo.Entity.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BreederDto {

    @NotEmpty
    private String username;

    @NotBlank
    private String nomColombie;

    private double latitude; // GPS coordinates
    private double longitude;

}
