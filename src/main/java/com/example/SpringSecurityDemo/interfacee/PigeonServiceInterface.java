package com.example.SpringSecurityDemo.interfacee;


import com.example.SpringSecurityDemo.Entity.model.Pigeon;

import java.util.List;
import java.util.Optional;

public interface PigeonServiceInterface {

    // Add a new pigeon
    Pigeon addPigeon(Long breederId, Pigeon pigeon);

    // Get all pigeons
    List<Pigeon> getAllPigeons();

    // Find pigeon by ring number
    Optional<Pigeon> getPigeonByRingNumber(Long ringNumber);

    // Delete a pigeon by ring number
    void deletePigeon(Long ringNumber);

    // Find pigeons by breeder ID
    List<Pigeon> findByBreederId(Long breederId);

    // Find pigeon by ID
    Pigeon findById(Long id);
}
