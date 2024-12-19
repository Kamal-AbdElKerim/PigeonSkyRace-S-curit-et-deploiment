package com.example.SpringSecurityDemo.interfacee;


import com.example.SpringSecurityDemo.Entity.model.Competition;
import com.example.SpringSecurityDemo.Entity.model.CompetitionDTO;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface CompetitionServiceInterface {
    // Method to add a new competition
    Competition addCompetition(CompetitionDTO competitionDTO);
    // Fetch all competitions
    List<Competition> fetchCompetition();

    // Update competition with details (latitude, longitude, total pigeons, pigeon count)
    void updateCompetition(Long competitionId, double latitude, double longitude, int totalPigeon, int pigeonCount);

    // Update competition entity
    void updateCompetition(Competition competition);

    // Get competition by ID
    Competition getCompetitionByid(Long competitionId);

    // End a competition and calculate results
    ResponseEntity<Object> endCompetition(Long competitionId);


}
