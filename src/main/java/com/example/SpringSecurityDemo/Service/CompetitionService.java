package com.example.SpringSecurityDemo.Service;


import com.example.SpringSecurityDemo.Entity.model.Competition;
import com.example.SpringSecurityDemo.Entity.model.CompetitionDTO;
import com.example.SpringSecurityDemo.Entity.model.CompetitionPigeon;
import com.example.SpringSecurityDemo.Repository.CompetitionPigeonRepository;
import com.example.SpringSecurityDemo.Repository.CompetitionRepository;
import com.example.SpringSecurityDemo.interfacee.CompetitionServiceInterface;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CompetitionService implements CompetitionServiceInterface {

    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private PigeonService pigeonService;

    @Autowired
    private CompetitionPigeonRepository competitionPigeonRepository;



    @Override
    public Competition addCompetition(CompetitionDTO competitionDTO) {
        // Convert CompetitionDTO to Competition entity
        Competition competition = new Competition();
        competition.setName(competitionDTO.getName());
        competition.setDepartureTime(competitionDTO.getDepartureTime());
        competition.setPercentage(competitionDTO.getPercentage());
        competition.setStatus(true);
        competition.setStarted(false);

        System.out.println(competitionDTO.getDepartureTime());
        System.out.println(competition.getDepartureTime());

        // Save the competition to the database
        competitionRepository.save(competition);
        return competition ;
    }
    @Override
    public List<Competition> fetchCompetition() {
        return competitionRepository.findAll();
    }

    @Override
    public void updateCompetition(Long competitionId, double latitude , double longitude , int TotalPigeon , int PigeonCount) {

        Competition existingCompetition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new RuntimeException("Competition not found"));

        existingCompetition.setPigeonTotal(TotalPigeon);
        existingCompetition.setPigeonCount(PigeonCount);
        existingCompetition.setLatitude(latitude);
        existingCompetition.setLongitude(longitude);
        existingCompetition.setStarted(true);

        competitionRepository.save(existingCompetition);
        System.out.println("competitionRepository.save(existingCompetition)" + competitionRepository.save(existingCompetition));
    }
    @Override
    public void updateCompetition(Competition competition) {
        competitionRepository.save(competition);
    }
    @Override
    public Competition getCompetitionByid(Long competitionId) {
      return   competitionRepository.findById(competitionId).orElseThrow(() -> new RuntimeException("Competition not found"));
    }
    @Override
    public ResponseEntity<Object> endCompetition(Long competitionId) {

        Optional<Competition> competitionOpt = competitionRepository.findById(competitionId);

        System.out.println("d");
        if (competitionOpt.isPresent()) {
            Competition competition = competitionOpt.get();
            System.out.println("find");
            competition.setStatus(false);
            competition.setStarted(false);
            competitionRepository.save(competition);

            this.calculateResult(competitionId);

            // Returning a success response
            return ResponseEntity.status(HttpStatus.OK).body("Competition ended successfully");
        }

        // Returning a failure response with NOT_FOUND status
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No competition found");
    }

    public long calculateTotalSeconds(CompetitionPigeon competitionPigeon, Competition competition) {
        System.out.println("qhihi1");

        System.out.println(competitionPigeon.getEndTime()); // e.g., "15:00"
        System.out.println(competition.getDepartureTime()); // e.g., "2024-11-13T17:12:12"

        // Extract hours and minutes
        String endTimeStr = String.valueOf(competitionPigeon.getEndTime()); // Assuming it's a String in "HH:mm"
        int endHour = Integer.parseInt(endTimeStr.split(":")[0]);
        int endMinute = Integer.parseInt(endTimeStr.split(":")[1]);

        LocalDateTime departureTime = competition.getDepartureTime(); // Assuming it's LocalDateTime
        int departureHour = departureTime.getHour();
        int departureMinute = departureTime.getMinute();

        // Convert hours and minutes to total seconds from midnight
        int endTotalSeconds = (endHour * 3600) + (endMinute * 60);
        int departureTotalSeconds = (departureHour * 3600) + (departureMinute * 60);
        System.out.println(endTotalSeconds);
        System.out.println(departureTotalSeconds);
        // Calculate difference
        long totalSeconds = endTotalSeconds - departureTotalSeconds;

        System.out.println("qhihi");
        System.out.println("End Time Seconds: " + endTotalSeconds);
        System.out.println("Departure Time Seconds: " + departureTotalSeconds);
        System.out.println("Total Seconds Difference: " + totalSeconds);

        return totalSeconds;
    }

    private void calculateResult(Long competitionId){

        List<CompetitionPigeon> competitionPigeons = competitionPigeonRepository.findByCompetitionId(competitionId);


        for (CompetitionPigeon competitionPigeon : competitionPigeons) {
            System.out.println(competitionPigeon);

            long totalSeconds = this.calculateTotalSeconds(competitionPigeon, competitionPigeon.getCompetition());


            if (totalSeconds != 0) {
                double vitesse = (competitionPigeon.getDistance() * 1000)  / (totalSeconds / 60);
                competitionPigeon.setVitesse(vitesse);
            } else {
                System.out.println("Error: EndTime is zero, cannot calculate speed");
            }


        }
        competitionPigeons.sort(new Comparator<CompetitionPigeon>() {
            @Override
            public int compare(CompetitionPigeon p1, CompetitionPigeon p2) {
                return Double.compare(p2.getVitesse(), p1.getVitesse());
            }
        });

        for (int rank = 0; rank < competitionPigeons.size(); rank++) {
            CompetitionPigeon competitionPigeon = competitionPigeons.get(rank);

            double score = 100 * (1 - (double) (rank) / (competitionPigeons.size() - 1));
            competitionPigeon.setScore(score);

            competitionPigeonRepository.save(competitionPigeon);
        }


        try{
            this.GeneratePDF(competitionPigeons);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void GeneratePDF(List<CompetitionPigeon> competitionPigeons) throws IOException {
        // Create a new workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Competition Pigeons");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("CL");
        headerRow.createCell(1).setCellValue("Colombier");
        headerRow.createCell(2).setCellValue("N bague");
        headerRow.createCell(3).setCellValue("Heure");
        headerRow.createCell(4).setCellValue("Distance");
        headerRow.createCell(5).setCellValue("Vitesse");
        headerRow.createCell(6).setCellValue("Point");

        // Populate the data rows
        int rowNum = 1;
        int rank = 0;

        for (;rank <  Math.ceil(competitionPigeons.size() * ( competitionPigeons.get(0).getCompetition().getPercentage() / 100.0 )) ; rank++) {
            Row row = sheet.createRow(rowNum++);

            // Fill in the values for each column
            row.createCell(0).setCellValue(rank + 1);  // CL
            row.createCell(1).setCellValue(competitionPigeons.get(rank).getPigeon().getBreeder().getNomColombie());  // Colombier
            row.createCell(2).setCellValue(competitionPigeons.get(rank).getPigeon().getRingNumber());  // N bague
            row.createCell(3).setCellValue(competitionPigeons.get(rank).getEndTime().toString());  // Heure
            row.createCell(4).setCellValue(competitionPigeons.get(rank).getDistance());  // Distance
            row.createCell(5).setCellValue(competitionPigeons.get(rank).getVitesse());  // Vitesse
            row.createCell(6).setCellValue(competitionPigeons.get(rank).getScore());  // Point

        }

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream("CompetitionPigeons.xlsx")) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }

        System.out.println("Excel file generated successfully.");
    }
}


