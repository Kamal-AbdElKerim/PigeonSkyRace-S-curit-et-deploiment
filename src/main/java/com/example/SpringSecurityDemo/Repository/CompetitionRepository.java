package com.example.SpringSecurityDemo.Repository;

import com.example.SpringSecurityDemo.Entity.model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    // You can add custom query methods here, if needed.
    // For example, a method to find competitions by name:
    List<Competition> findByName(String name);
}
