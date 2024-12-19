package com.example.SpringSecurityDemo.Repository;

import com.example.SpringSecurityDemo.Entity.User.Breeder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BreederRepository extends JpaRepository<Breeder,Long> {

    Breeder findByNomColombie(String username);
   Optional<Breeder>  findByUsername(String username);

}
