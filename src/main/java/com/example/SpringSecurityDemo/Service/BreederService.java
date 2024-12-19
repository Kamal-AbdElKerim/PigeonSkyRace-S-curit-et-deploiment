package com.example.SpringSecurityDemo.Service;

import com.example.SpringSecurityDemo.Entity.User.Breeder;
import com.example.SpringSecurityDemo.Repository.BreederRepository;
import com.example.SpringSecurityDemo.interfacee.IBreederService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BreederService implements IBreederService {


  private final   BreederRepository breederRepository;



  @Override
    public Breeder findByNomColombie(String nomColombie){
        return breederRepository.findByNomColombie(nomColombie);
    }
    @Override
    public Optional<Breeder> findByID(Long id){
        return breederRepository.findById(id);
    }
    @Override
    public List<Breeder> findAll(){
        return breederRepository.findAll();
    }


}
