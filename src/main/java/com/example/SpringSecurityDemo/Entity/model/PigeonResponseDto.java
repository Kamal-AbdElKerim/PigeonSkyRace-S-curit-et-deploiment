package com.example.SpringSecurityDemo.Entity.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
public class PigeonResponseDto {
    private String ringNumber;
    private String gender;
    private int age;
    private String color;
    private BreederDto breeder;
}
