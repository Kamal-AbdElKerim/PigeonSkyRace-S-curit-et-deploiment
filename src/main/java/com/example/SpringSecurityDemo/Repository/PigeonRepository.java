package com.example.SpringSecurityDemo.Repository;

import com.example.SpringSecurityDemo.Entity.model.Pigeon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PigeonRepository extends JpaRepository<Pigeon, Long> {

    // Find pigeons by breeder ID
    List<Pigeon> findByBreederId(Long breederId);
    Pigeon findByRingNumber(Long RingNumber);

}
