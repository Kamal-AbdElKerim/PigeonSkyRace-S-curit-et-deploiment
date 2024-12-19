package com.example.SpringSecurityDemo.Entity.MapStruct;



import com.example.SpringSecurityDemo.Entity.Role.Role;
import com.example.SpringSecurityDemo.Entity.Role.RoleResponseDto;
import com.example.SpringSecurityDemo.Entity.User.Breeder;
import com.example.SpringSecurityDemo.Entity.User.UserDto;
import com.example.SpringSecurityDemo.Entity.User.UserResponseDto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // Mapping AppUser to UserDto

    UserDto appUserToUserDto(Breeder breeder);

    // Mapping UserDto to AppUser

    Breeder userDtoToAppUser(UserDto userDto);

    // Mapping AppUser to UserResponseDto with role names

    UserResponseDto appUserToUserResponseDto(Breeder breeder);

    // Mapping Role to RoleResponseDto (now extracting the role name correctly)
    default RoleResponseDto roleToRoleResponseDto(Role role) {
        if (role != null) {
            RoleResponseDto roleResponseDto = new RoleResponseDto();
            roleResponseDto.setRoleName(role.getName());  // Extracting the role's name
            return roleResponseDto;
        }
        return null;
    }

    // Mapping Set<Role> to Set<RoleResponseDto>
    default Set<RoleResponseDto> rolesToRoleResponseDtos(Set<Role> roles) {
        if (roles != null) {
            return roles.stream()
                    .map(this::roleToRoleResponseDto)
                    .collect(Collectors.toSet());
        }
        return null;
    }
}
