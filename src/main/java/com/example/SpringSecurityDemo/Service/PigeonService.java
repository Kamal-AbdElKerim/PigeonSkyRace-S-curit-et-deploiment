package com.example.SpringSecurityDemo.Service;


import com.example.SpringSecurityDemo.Entity.User.Breeder;
import com.example.SpringSecurityDemo.Entity.model.Pigeon;
import com.example.SpringSecurityDemo.Exception.EntityAlreadyExistsException;
import com.example.SpringSecurityDemo.Repository.BreederRepository;
import com.example.SpringSecurityDemo.Repository.PigeonRepository;
import com.example.SpringSecurityDemo.interfacee.PigeonServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PigeonService implements PigeonServiceInterface {

    @Autowired
    private PigeonRepository pigeonRepository;


    @Autowired
    private BreederRepository breederRepository;

    @Override
    public Pigeon addPigeon(Long breederId, Pigeon pigeon) {
        // Find the breeder by ID
        Breeder breeder = breederRepository.findById(breederId)
                .orElseThrow(() -> new RuntimeException("Breeder not found"));

       Pigeon pigeon1 = pigeonRepository.findByRingNumber(pigeon.getRingNumber()) ;

        if (pigeon1 != null) {
            System.out.println("Pigeon already exists");
            throw new EntityAlreadyExistsException("Pigeon","Pigeon already used");
        }

        pigeon.setBreeder(breeder);

        return pigeonRepository.save(pigeon);
    }


    @Override
    public List<Pigeon> getAllPigeons() {
        return pigeonRepository.findAll();
    }

    @Override
    public Optional<Pigeon> getPigeonByRingNumber(Long ringNumber) {
        return pigeonRepository.findById(ringNumber);
    }

    @Override
    public void deletePigeon(Long ringNumber) {
        pigeonRepository.deleteById(ringNumber);
    }
    @Override
    public List<Pigeon> findByBreederId(Long breederId) {
        return pigeonRepository.findByBreederId(breederId);
    }
    @Override
    public  Pigeon findById(Long id) {
        return pigeonRepository.findById(id).orElse(null);
    }

}

