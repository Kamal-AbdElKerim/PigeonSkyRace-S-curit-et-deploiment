package com.example.SpringSecurityDemo.Repository;

import com.example.SpringSecurityDemo.Entity.model.CompetitionPigeon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitionPigeonRepository extends JpaRepository<CompetitionPigeon, Long> {

    // Find CompetitionPigeon by competitionId (assuming competitionId is a String)
    List<CompetitionPigeon> findByCompetitionId(Long competitionId);

    // Find CompetitionPigeon by Pigeon's ring number (assuming the relationship is correct)
    CompetitionPigeon findByPigeon_RingNumber(Long ringNumber);
}
