package com.example.SpringSecurityDemo.Service;


import com.example.SpringSecurityDemo.Entity.model.Competition;
import com.example.SpringSecurityDemo.Entity.model.CompetitionPigeon;
import com.example.SpringSecurityDemo.Entity.model.Pigeon;
import com.example.SpringSecurityDemo.Exception.EntityNotFoundException;
import com.example.SpringSecurityDemo.Repository.CompetitionPigeonRepository;
import com.example.SpringSecurityDemo.Repository.CompetitionRepository;
import com.example.SpringSecurityDemo.interfacee.ICompetitionPigeonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class CompetitionPigeonService implements ICompetitionPigeonService {

    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private PigeonService pigeonService;

    @Autowired
    private CompetitionPigeonRepository competitionPigeonRepository;

    @Autowired
    private CompetitionService competitionService;


@Override
    public void addPigeonToCompetition( CompetitionPigeon competitionPigeon) {

        Competition competition = competitionRepository.findById(competitionPigeon.getCompetition().getId())
                .orElseThrow(() -> new EntityNotFoundException("Competition","Competition not found with ID: "
                        + competitionPigeon.getCompetition().getId()));

        Pigeon pigeon = pigeonService.findById(competitionPigeon.getPigeon().getRingNumber());
        if (pigeon == null) {
            throw new EntityNotFoundException("pigeon","Pigeon not found with ring number: "
                    + competitionPigeon.getPigeon().getRingNumber());
        }

        competitionPigeon.setCompetition(competition);
        competitionPigeon.setPigeon(pigeon);



        competitionPigeonRepository.save(competitionPigeon);



    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    @Override
    public List<CompetitionPigeon> getAllPigeonEtCompetition() {
        return competitionPigeonRepository.findAll();
    }
    @Override
    public int getCountPigeonToCompetition(Long competitionId) {
        List<CompetitionPigeon> competitionPigeons = competitionPigeonRepository.findByCompetitionId(competitionId);


        Set<Long> uniquePigeons = new HashSet<>();

        for (CompetitionPigeon competitionPigeon : competitionPigeons) {

            uniquePigeons.add(competitionPigeon.getPigeon().getRingNumber());
        }

        return uniquePigeons.size();
    }

    @Override
    public void StartCompetition(Long competitionId) {
        double totalLat = 0;
        double totalLon = 0;
        int count = 0;

        List<CompetitionPigeon> competitionPigeons = competitionPigeonRepository.findByCompetitionId(competitionId);
        double[] releasePoint = findOptimalReleasePoint(competitionPigeons, 100.0);
        System.out.println("Optimal Release Point: Latitude = " + releasePoint[0] + ", Longitude = " + releasePoint[1]);

        for (CompetitionPigeon competition : competitionPigeons) {
            double latitude = competition.getPigeon().getBreeder().getLatitude();
            double longitude = competition.getPigeon().getBreeder().getLongitude();
            totalLat += latitude;
            totalLon += longitude;
            count++;
        }

        double latitude = totalLat / count;
        double longitude = totalLon / count;

        int  TotalPigeon =  getCountPigeonToCompetition(competitionId);

        int  PigeonCount = calculatePigeonCount( competitionId ,  TotalPigeon) ;



        competitionService.updateCompetition(competitionId , latitude , longitude , TotalPigeon ,PigeonCount) ;


        double totalDistance = 0.0;
        int countCompetitionPigeons = competitionPigeons.size();

        for (CompetitionPigeon competition : competitionPigeons) {

            double LatitudeBreeder = competition.getPigeon().getBreeder().getLatitude();
            double LongitudeBreeder = competition.getPigeon().getBreeder().getLongitude();

            double distance = calculateDistance(latitude, longitude, LatitudeBreeder, LongitudeBreeder);
            System.out.println("distance = " + distance);
            competition.setDistance(distance);

            // Add to the total distance for average calculation
            totalDistance += distance;

            // Save the updated competition
            competitionPigeonRepository.save(competition);
        }


        double averageDistance = totalDistance / countCompetitionPigeons;
        System.out.println("Average distance = " + averageDistance);

        Competition competition =  competitionService.getCompetitionByid(competitionId);

        competition.setDistance(averageDistance);

        competitionRepository.save(competition);

    }
    @Override
    public int calculatePigeonCount(Long competitionId, int totalPigeon) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new RuntimeException("Competition not found"));
        int percentage = competition.getPercentage();

        if (percentage <= 0) {
            throw new IllegalArgumentException("Percentage must be greater than 0");
        }

        return (int) Math.ceil((totalPigeon * percentage) / 100.0);
    }


    @Override
    public String updateEndTime(Long breederId, Long ringNumber, LocalTime endTime) {
        CompetitionPigeon competitionPigeonOpt = competitionPigeonRepository.findByPigeon_RingNumber(ringNumber);
        System.out.println("Found CompetitionPigeons: " + competitionPigeonOpt);
        Long  BreederID =  competitionPigeonOpt.getPigeon().getBreeder().getId() ;
        if (competitionPigeonOpt.getEndTime() != null) {
            throw new IllegalArgumentException("Pigeon Already done");
        }
        if (competitionPigeonOpt != null && BreederID.equals(breederId)) {
            competitionPigeonOpt.setEndTime(endTime);
            competitionPigeonRepository.save(competitionPigeonOpt);
            return "cool Pigeon done";
        }else {
            throw new IllegalArgumentException("Pigeon not found");
        }


    }

    @Override
    public void EndCompetition(Long competitionId) {


        List<CompetitionPigeon> competitionPigeons = competitionPigeonRepository.findByCompetitionId(competitionId);
        for (CompetitionPigeon competitionPigeon : competitionPigeons) {

            if(competitionPigeon.getEndTime() != null && competitionPigeon.getDistance() != null) {
           LocalTime   endTime = competitionPigeon.getEndTime() ;
           LocalDateTime  departTime = competitionPigeon.getCompetition().getDepartureTime();

                // Calculate Duration between departure time and end time
                Duration duration = Duration.between(departTime, departTime.with(endTime));

                double hours = duration.toHours();
                double minutes = duration.toMinutes() % 60;
                double seconds = duration.getSeconds() % 60;
                double  distanceKm = competitionPigeon.getDistance() ;

                // Convert time to total minutes
                double totalMinutes = (hours * 60) + minutes + (seconds / 60.0);

// Convert distance to meters
                double distanceMeters = distanceKm * 1000;

// Calculate speed in meters per minute
                double vitesse = distanceMeters / totalMinutes;

                System.out.println("Vitesse (m/min): " + vitesse);
            }

        }



        int  TotalPigeon =  getCountPigeonToCompetition(competitionId);

        int  PigeonCount = calculatePigeonCount( competitionId ,  TotalPigeon) ;

     //   competitionService.updateCompetition(competitionId , latitude , longitude , TotalPigeon ,PigeonCount) ;


        System.out.println();
    }


    public static double[] findOptimalReleasePoint(List<CompetitionPigeon> pigeons, double targetDistanceKm) {
        // Step 1: Calculate the centroid as the starting point
        double totalLat = 0.0;
        double totalLon = 0.0;
        int count = pigeons.size();

        for (CompetitionPigeon competition : pigeons) {
            totalLat += competition.getPigeon().getBreeder().getLatitude();
            totalLon += competition.getPigeon().getBreeder().getLongitude();
        }

        // Initial point is the centroid
        double centroidLat = totalLat / count;
        double centroidLon = totalLon / count;

        // Step 2: Adjust point to meet the target distance
        return adjustPointToDistance(pigeons, centroidLat, centroidLon, targetDistanceKm);
    }

    private static double[] adjustPointToDistance(List<CompetitionPigeon> pigeons, double startLat, double startLon, double targetDistanceKm) {
        double lat = startLat;
        double lon = startLon;
        double tolerance = 1.0; // 1 km tolerance

        for (int i = 0; i < 1000; i++) { // Limit iterations for safety
            double avgDistance = calculateAverageDistance(pigeons, lat, lon);

            if (Math.abs(avgDistance - targetDistanceKm) <= tolerance) {
                break; // Point found within tolerance
            }

            // Adjust lat and lon by a small factor in the direction needed
            double adjustmentFactor = 0.01; // Step size to adjust the point
            if (avgDistance < targetDistanceKm) {
                lat += adjustmentFactor;
                lon += adjustmentFactor;
            } else {
                lat -= adjustmentFactor;
                lon -= adjustmentFactor;
            }
        }
        return new double[]{lat, lon};
    }

    private static double calculateAverageDistance(List<CompetitionPigeon> pigeons, double lat, double lon) {
        double totalDistance = 0.0;

        for (CompetitionPigeon pigeon : pigeons) {
            double breederLat = pigeon.getPigeon().getBreeder().getLatitude();
            double breederLon = pigeon.getPigeon().getBreeder().getLongitude();
            totalDistance += haversineDistance(lat, lon, breederLat, breederLon);
        }
        return totalDistance / pigeons.size();
    }
    private static final double EARTH_RADIUS_KM = 6371.0;
    // Haversine formula to calculate distance between two latitude/longitude points
    private static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

}


