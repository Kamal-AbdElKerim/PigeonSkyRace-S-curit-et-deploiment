package com.example.SpringSecurityDemo.Entity.User;


import com.example.SpringSecurityDemo.Entity.Role.RoleResponseDto;
import com.example.SpringSecurityDemo.Entity.model.Pigeon;
import com.example.SpringSecurityDemo.Entity.model.PigeonResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserResponseDto {
    private String userID;
    private String username;
    private String nomColombie;
    private Set<RoleResponseDto> roles;
    private double latitude; // GPS coordinates
    private double longitude;
    private List<PigeonResponseDto> pigeons;
}
