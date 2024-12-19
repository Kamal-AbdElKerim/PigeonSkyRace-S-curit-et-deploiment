package com.example.SpringSecurityDemo.interfacee;


import com.example.SpringSecurityDemo.Entity.User.Breeder;
import com.example.SpringSecurityDemo.Entity.User.LoginDto;
import com.example.SpringSecurityDemo.Entity.User.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface IAccountService {

    ResponseEntity<Object> InfoAuth();

    ResponseEntity<Object> createUser(UserDto userDto);

    Breeder updateUserRoles(Long userID, Set<Long> roleIds);

    ResponseEntity<Object> Login(LoginDto loginDto);
}
