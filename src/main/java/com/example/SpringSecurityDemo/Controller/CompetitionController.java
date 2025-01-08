package com.example.SpringSecurityDemo.Controller;


import com.example.SpringSecurityDemo.Entity.ResponseDto;
import com.example.SpringSecurityDemo.Entity.model.Competition;
import com.example.SpringSecurityDemo.Entity.model.CompetitionDTO;
import com.example.SpringSecurityDemo.Service.CompetitionService;
import com.example.SpringSecurityDemo.interfacee.CompetitionServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Api/Competition")
public class CompetitionController {

    @Autowired
    private CompetitionServiceInterface competitionService;

    @GetMapping()
    public List<Competition> getCompetition() {
        return competitionService.fetchCompetition();
    }

    @PostMapping()
    public ResponseEntity<Competition> addCompetition(@Valid @RequestBody CompetitionDTO competitionDTO) {
        // Call the service to add the competition
        Competition competition = competitionService.addCompetition(competitionDTO);

        // Return the saved competition object as JSON with a 201 Created status
        return ResponseEntity.status(HttpStatus.CREATED).body(competition);
    }

    @GetMapping("/End-Competition/{competitionid}")
    public ResponseEntity<Object> endCompetition(@PathVariable Long competitionid) {

        // Calling service method that returns ResponseDto or a String message

        return competitionService.endCompetition(competitionid);
    }

//    @GetMapping("GetCompetitionResult")
//    public ResponseEntity<Object> getCompetitionResult() {
//
//    }
}

