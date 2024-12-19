package com.example.SpringSecurityDemo.Service;

import com.example.SpringSecurityDemo.Entity.model.Competition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;

@Service
public class CompetitionScheduler {

    @Autowired
    private CompetitionService competitionService;
    @Autowired
    private CompetitionPigeonService competitionPigeonService;

    @Scheduled(fixedRate = 60000)
    public void checkCompetitionsToStart() {
        List<Competition> competitions = competitionService.fetchCompetition();


        for (Competition competition : competitions) {
            // Adjust the competition departure time to UTC and subtract 1 hour if needed
            ZonedDateTime utcDepartureTime = competition.getDepartureTime()
                    .atZone(ZoneOffset.UTC)
                    .minusHours(1);

            System.out.println("Competition " + competition.getName() + " adjusted departure time to UTC: " + utcDepartureTime);
            System.out.println("ZonedDateTime.now() :" + ZonedDateTime.now());
            ZonedDateTime nowUtc = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).minusHours(1);
            System.out.println("Now in UTC: " + nowUtc);
            System.out.println(utcDepartureTime.isBefore(nowUtc));

            // Check if the competition is due to start
            if ((utcDepartureTime.isBefore(ZonedDateTime.now()) || utcDepartureTime.equals(ZonedDateTime.now()))
                    && !competition.isStarted()) {
                competitionPigeonService.StartCompetition(competition.getId());
                System.out.println("Competition " + competition.getName() + " has started!");
            }
        }
}}

