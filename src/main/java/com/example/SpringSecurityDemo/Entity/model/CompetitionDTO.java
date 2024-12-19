package com.example.SpringSecurityDemo.Entity.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CompetitionDTO {

    private String name; // Competition name

    @JsonFormat(pattern = "yy/MM/dd HH:mm:ss")
    private LocalDateTime departureTime;


    private int pigeonCount;

    private int pigeonTotal;

    private int percentage;

    private double latitude;

    private double longitude;

    private Double distance;

    // Constructors
    public CompetitionDTO() {}


}
