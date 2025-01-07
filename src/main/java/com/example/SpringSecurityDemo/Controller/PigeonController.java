package com.example.SpringSecurityDemo.Controller;


import com.example.SpringSecurityDemo.Entity.User.Breeder;
import com.example.SpringSecurityDemo.Entity.model.BreederDto;
import com.example.SpringSecurityDemo.Entity.model.Pigeon;
import com.example.SpringSecurityDemo.Entity.model.PigeonResponseDto;
import com.example.SpringSecurityDemo.Exception.EntityAlreadyExistsException;
import com.example.SpringSecurityDemo.Exception.EntityNotFoundException;
import com.example.SpringSecurityDemo.Repository.BreederRepository;
import com.example.SpringSecurityDemo.Service.PigeonService;
import com.example.SpringSecurityDemo.interfacee.PigeonServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Api/pigeons")
public class PigeonController {


    private final PigeonServiceInterface pigeonService;


    private final BreederRepository userRepository;


    private final PigeonResponseDto pigeonDto ;

    // Add a new pigeon
    @PostMapping("/{breederId}")
    public ResponseEntity<Pigeon> addPigeon(

            @RequestBody Pigeon pigeon ,
            @PathVariable String breederId) {

        Pigeon savedPigeon = pigeonService.addPigeon(breederId,pigeon);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPigeon);


    }

    public Optional<Breeder> getAuthenticatedBreeder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        return Optional.ofNullable(userRepository.findByNomColombie(authentication.getName()));
    }


    @GetMapping("/{pigeonId}")
    public ResponseEntity<PigeonResponseDto> getPigeonWithBreeder(@PathVariable Long pigeonId) {
        Optional<Pigeon> pigeonOpt = pigeonService.getPigeonByRingNumber(pigeonId);

        if (pigeonOpt.isEmpty()) {
            throw new EntityNotFoundException("pigeonId", "Pigeon ID not found");
        }

        Pigeon pigeon = pigeonOpt.get();
        Optional<Breeder> authenticatedBreederOpt = getAuthenticatedBreeder();

        if (authenticatedBreederOpt.isEmpty()) {
            throw new EntityNotFoundException("authenticated","You are not authenticated.");
        }

        Breeder authenticatedBreeder = authenticatedBreederOpt.get();

        boolean isAdmin = authenticatedBreeder.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));

        if (!isAdmin) {
            if (pigeon.getBreeder() == null ||
                    !authenticatedBreeder.getNomColombie().equals(pigeon.getBreeder().getNomColombie())) {
                throw new EntityNotFoundException("NomColombie", "This pigeon is not yours.");
            }
        }

        // Prepare the response DTO
        PigeonResponseDto pigeonDto = new PigeonResponseDto();
        BeanUtils.copyProperties(pigeon, pigeonDto);

        Breeder breeder = pigeon.getBreeder();
        if (breeder != null) {
            BreederDto breederDto = new BreederDto();
            BeanUtils.copyProperties(breeder, breederDto);
            pigeonDto.setBreeder(breederDto);
        }

        return ResponseEntity.ok(pigeonDto);
    }





    // Get all pigeons

    @GetMapping("/Breeder/{BreederID}")
    public List<PigeonResponseDto> getAllPigeons(@PathVariable Long BreederID) {
        List<Pigeon> pigeons = pigeonService.getAllPigeons(BreederID);
        List<PigeonResponseDto> pigeonDtos = new ArrayList<>();

        for (Pigeon pigeon : pigeons) {
            // Create a new instance of PigeonResponseDto for each pigeon
            PigeonResponseDto pigeonDto = new PigeonResponseDto();

            // Copy properties from Pigeon to PigeonResponseDto
            BeanUtils.copyProperties(pigeon, pigeonDto);

            // Handle the Breeder object if it exists
            Breeder breeder = pigeon.getBreeder();
            if (breeder != null) {
                BreederDto breederDto = new BreederDto();
                BeanUtils.copyProperties(breeder, breederDto);
                pigeonDto.setBreeder(breederDto);
            }

            // Add the newly created pigeonDto to the list
            pigeonDtos.add(pigeonDto);
        }

        return pigeonDtos;
    }


    // Delete a pigeon by ring number
    @DeleteMapping("/{ringNumber}")
    public ResponseEntity<Void> deletePigeon(@PathVariable Long ringNumber) {
        Optional<Pigeon> pigeonOpt = pigeonService.getPigeonByRingNumber(ringNumber);

        if (pigeonOpt.isEmpty()) {
            throw new EntityNotFoundException("Pigeon", "No pigeon found with ring number: " + ringNumber);
        }

        Pigeon pigeon = pigeonOpt.get();
        Optional<Breeder> authenticatedBreederOpt = getAuthenticatedBreeder();

        boolean isAdmin = authenticatedBreederOpt.get().getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));

        if (!isAdmin) {
            if (authenticatedBreederOpt.isEmpty() ||
                    pigeon.getBreeder() == null ||
                    !authenticatedBreederOpt.get().getNomColombie().equals(pigeon.getBreeder().getNomColombie())) {
                throw new EntityNotFoundException("authorized", "You are not authorized to delete this pigeon.");
            }
        }

        pigeonService.deletePigeon(ringNumber);

        return ResponseEntity.noContent().build(); // Returns 204 No Content for successful deletion
    }

}
