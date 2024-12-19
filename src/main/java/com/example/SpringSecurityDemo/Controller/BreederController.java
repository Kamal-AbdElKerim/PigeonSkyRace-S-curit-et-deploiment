package com.example.SpringSecurityDemo.Controller;


import com.example.SpringSecurityDemo.Entity.User.Breeder;
import com.example.SpringSecurityDemo.Entity.model.Pigeon;
import com.example.SpringSecurityDemo.Service.BreederService;
import com.example.SpringSecurityDemo.Service.PigeonService;
import com.example.SpringSecurityDemo.interfacee.IBreederService;
import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Api/breeders")
@RequiredArgsConstructor
public class BreederController {



  private final IBreederService breederService ;


    private final  PigeonService pigeonService ;

    @GetMapping("/")
    public ResponseEntity<List<Breeder>> getAllBreeders() {
        List<Breeder> breeders = breederService.findAll();

        for (Breeder b : breeders) {
            List<Pigeon> pigeons = pigeonService.findByBreederId(b.getId());
            for (Pigeon p : pigeons) {
                p.setBreeder(null);
            }
            b.setPigeons(pigeons);
        }

        return ResponseEntity.ok(breeders);
    }



    @GetMapping("/{breederId}")
    public ResponseEntity<Breeder> getBreederById(@PathVariable Long breederId) {
        Optional<Breeder> breeder = breederService.findByID(breederId);


        return breeder.map(b -> {

            List<Pigeon> pigeons = pigeonService.findByBreederId(breederId);
            for (Pigeon p : pigeons) {
                p.setBreeder(null);
            }
            b.setPigeons(pigeons);
            return ResponseEntity.ok(b);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }





}
