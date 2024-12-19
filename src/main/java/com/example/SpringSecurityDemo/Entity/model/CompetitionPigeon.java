package com.example.SpringSecurityDemo.Entity.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Entity
@Table(name = "competition_pigeons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionPigeon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    @ManyToOne
    @JoinColumn(name = "pigeon_id", nullable = false)
    private Pigeon pigeon;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    private Double distance;
    private Double vitesse;
    private Double score;

    // No need for explicit getters/setters due to Lombok annotations
}
