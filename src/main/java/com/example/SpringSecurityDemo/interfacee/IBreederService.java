package com.example.SpringSecurityDemo.interfacee;


import com.example.SpringSecurityDemo.Entity.User.Breeder;

import java.util.List;
import java.util.Optional;

public interface IBreederService {

    Breeder findByNomColombie(String nomColombie);

    Optional<Breeder> findByID(Long id);

    List<Breeder> findAll();
}
