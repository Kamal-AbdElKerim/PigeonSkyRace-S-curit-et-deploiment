package com.example.SpringSecurityDemo.Entity.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "competitions") // Maps to a SQL table
@Getter
@Setter
@ToString
@NoArgsConstructor // Lombok generates a no-args constructor
@AllArgsConstructor // Lombok generates a constructor with all fields
@Builder // Lombok generates a builder for easier object creation
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-generated ID for the competition


    private String name; // Name of the competition

    private double latitude;
    private double longitude;


    @JsonFormat(pattern = "yy/MM/dd HH:mm:ss")

    private LocalDateTime departureTime;

    private int pigeonTotal;


    private int percentage;

    private int pigeonCount;

    private boolean status;

    private boolean started;

    private Double distance;

    @ManyToMany
    @JoinTable(
            name = "competition_pigeon",
            joinColumns = @JoinColumn(name = "competition_id"),
            inverseJoinColumns = @JoinColumn(name = "pigeon_id")
    )
    private Set<CompetitionPigeon> pigeons;

    // Getters, Setters, and other methods are automatically handled by Lombok
}
