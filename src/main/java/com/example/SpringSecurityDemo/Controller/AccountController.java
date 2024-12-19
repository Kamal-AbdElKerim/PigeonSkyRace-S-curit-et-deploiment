package com.example.SpringSecurityDemo.Controller;



import com.example.SpringSecurityDemo.Entity.Role.RoleUpdateRequest;
import com.example.SpringSecurityDemo.Entity.User.Breeder;
import com.example.SpringSecurityDemo.Entity.User.LoginDto;
import com.example.SpringSecurityDemo.Entity.User.UserDto;
import com.example.SpringSecurityDemo.Service.AccountService;
import com.example.SpringSecurityDemo.interfacee.IAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {

    private final IAccountService accountService;

    @GetMapping("/current-user")
    public ResponseEntity<Object> getCurrentUser() {
        return accountService.InfoAuth() ;
    }


    @PostMapping("/register")
    public ResponseEntity<Object> register(
            @Valid @RequestBody UserDto userDto
           ) {
        return accountService.createUser(userDto) ;
    }

    @PutMapping("/users/{userId}/roles")
    public ResponseEntity<Breeder> updateUserRoles(
            @PathVariable Long userId, @RequestBody RoleUpdateRequest roleUpdateRequest) {
        Breeder updatedBreeder = accountService.updateUserRoles(userId, roleUpdateRequest.getRoleIds());
        return ResponseEntity.ok(updatedBreeder);
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @Valid @RequestBody LoginDto loginDto
           ) {
        return accountService.Login(loginDto) ;
    }
}
