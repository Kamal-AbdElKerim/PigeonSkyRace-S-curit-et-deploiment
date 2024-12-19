package com.example.SpringSecurityDemo.interfacee;


import com.example.SpringSecurityDemo.Entity.model.CompetitionPigeon;

import java.time.LocalTime;
import java.util.List;

public interface ICompetitionPigeonService {

    void addPigeonToCompetition(CompetitionPigeon competitionPigeon);

    List<CompetitionPigeon> getAllPigeonEtCompetition();

    int getCountPigeonToCompetition(Long competitionId);

    void StartCompetition(Long competitionId);

    int calculatePigeonCount(Long competitionId, int totalPigeon);

    String updateEndTime(Long breederId, Long ringNumber, LocalTime endTime);

    void EndCompetition(Long competitionId);


}
