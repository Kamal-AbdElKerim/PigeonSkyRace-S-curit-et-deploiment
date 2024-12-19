package com.example.SpringSecurityDemo.Entity.Role;

import lombok.Data;

import java.util.Set;

@Data
public class RoleUpdateRequest {
    private Set<Long> roleIds;
}
