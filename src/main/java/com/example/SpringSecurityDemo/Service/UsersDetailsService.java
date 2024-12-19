package com.example.SpringSecurityDemo.Service;

import com.example.SpringSecurityDemo.Entity.User.Breeder;
import com.example.SpringSecurityDemo.Repository.BreederRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class UsersDetailsService implements UserDetailsService {


    private final BreederRepository breederRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Breeder breeder = breederRepository.findByNomColombie(username);
        System.out.println(breeder.getUsername());
        Set<GrantedAuthority> authorities = breeder.getRoles().stream()
                .map((roles) -> new SimpleGrantedAuthority(roles.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                breeder.getNomColombie(),
                breeder.getPassword(),
                authorities
        );

    }
}
