package com.example.SpringSecurityDemo.Controller;


import com.example.SpringSecurityDemo.Entity.User.Breeder;
import com.example.SpringSecurityDemo.Entity.model.CompetitionPigeon;
import com.example.SpringSecurityDemo.Entity.model.EndTimeRequest;
import com.example.SpringSecurityDemo.Repository.BreederRepository;
import com.example.SpringSecurityDemo.Service.CompetitionPigeonService;
import com.example.SpringSecurityDemo.interfacee.ICompetitionPigeonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Api/CompetitionPigeon")

public class CompetitionPigeonController {


    private final ICompetitionPigeonService competitionPigeonService;

    private final BreederRepository userRepository;


    private static final Logger logger = LoggerFactory.getLogger(CompetitionPigeonController.class);

    @PostMapping("/AddPigeonToCompetition")
    public ResponseEntity<Map<String, String>> addPigeonToCompetition(@Valid @RequestBody CompetitionPigeon competitionPigeon) {

        competitionPigeonService.addPigeonToCompetition(competitionPigeon);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Competition added successfully!");
        return ResponseEntity.ok(response);
    }


    @GetMapping()
    public List<CompetitionPigeon> getAllPigeonEtCompetition() {
        return competitionPigeonService.getAllPigeonEtCompetition();
    }



    @GetMapping("/{competitionId}/Start-competition")
    public ResponseEntity<Map<String, String>> startCompetition(@PathVariable Long competitionId) {
        // Call the service to start the competition
        competitionPigeonService.StartCompetition(competitionId);

        // Create a response body with a success message
        Map<String, String> response = new HashMap<>();
        response.put("message", "Competition started successfully!");
        response.put("competitionId", String.valueOf(competitionId));

        // Return the response with a 200 OK status
        return ResponseEntity.ok(response);
    }



    @PostMapping("/pigeon/{ringNumber}/end-time")
    public String updateEndTime(
            @PathVariable Long ringNumber,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody EndTimeRequest endTimeRequest) {

        LocalDateTime endTime = endTimeRequest.getEndTime();

        LocalTime endTimePlusOneHour = endTime.plusHours(1).toLocalTime();


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<Breeder> otherUser = Optional.ofNullable(userRepository.findByNomColombie(authentication.getName()));

        if (otherUser.isPresent()) {
        return competitionPigeonService.updateEndTime(otherUser.get().getId(), ringNumber, endTimePlusOneHour);

        }

        return null ;

    }


    @GetMapping("/{competitionId}/End-competition")
    public String EndCompetition(@PathVariable Long competitionId) {
        competitionPigeonService.StartCompetition(competitionId);

        return "Competition Start!";
    }





}
