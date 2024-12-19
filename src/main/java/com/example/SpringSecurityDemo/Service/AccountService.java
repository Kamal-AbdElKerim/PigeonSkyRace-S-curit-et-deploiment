package com.example.SpringSecurityDemo.Service;


import com.example.SpringSecurityDemo.Config.SecurityConfig;
import com.example.SpringSecurityDemo.Entity.MapStruct.UserMapper;
import com.example.SpringSecurityDemo.Entity.Role.Role;
import com.example.SpringSecurityDemo.Entity.User.Breeder;
import com.example.SpringSecurityDemo.Entity.User.UserDto;
import com.example.SpringSecurityDemo.Entity.User.UserResponseDto;
import com.example.SpringSecurityDemo.Entity.User.LoginDto;
import com.example.SpringSecurityDemo.Exception.EntityAlreadyExistsException;
import com.example.SpringSecurityDemo.Exception.EntityNotFoundException;
import com.example.SpringSecurityDemo.Repository.BreederRepository;
import com.example.SpringSecurityDemo.Repository.RoleRepository;
import com.example.SpringSecurityDemo.interfacee.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;


@RequiredArgsConstructor
@Service
public class AccountService implements IAccountService {

    private final BreederRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final SecurityConfig securityConfiguration;



    @Override
    public ResponseEntity<Object> InfoAuth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<Breeder> otherUser = Optional.ofNullable(userRepository.findByNomColombie(authentication.getName()));

        if (otherUser.isPresent()) {
            return generateUserResponseDto(otherUser.get());
        }

        return new ResponseEntity<>("Utilisateur non trouvé", HttpStatus.NOT_FOUND);
    }
    @Override
    public ResponseEntity<Object> createUser(UserDto userDto){
        var bCryptEncoder = new BCryptPasswordEncoder();

        Breeder appuser = userMapper.userDtoToAppUser(userDto);

        Optional<Breeder> otherUser = Optional.ofNullable(userRepository.findByNomColombie(userDto.getNomColombie()));

        if (otherUser.isPresent()) {
            throw new EntityAlreadyExistsException("NomColombie", "NomColombie  already used.");
        }

        Role ROLE_USER = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found!"));

        appuser.setUserID(generateUserID(appuser));
        appuser.setPassword(bCryptEncoder.encode(userDto.getPassword()));

        appuser.setRoles(Set.of(ROLE_USER));
    

        userRepository.save(appuser);

        return generateUserResponseDto(appuser);
    }


    @Override
    public Breeder updateUserRoles(Long userID, Set<Long> roleIds) {
        System.out.println("les id"+roleIds);
        Breeder breeder = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Role> roles =  roleRepository.findAllById(roleIds);

        Set<Role> newRoles = new HashSet<>(roles);

        System.out.println("roles: " + newRoles);

        if (newRoles.isEmpty()) {
            throw new EntityNotFoundException("Roles","Roles not found");
        }

        breeder.setRoles(newRoles);

        return  userRepository.save(breeder);


    }

    private ResponseEntity<Object> generateUserResponseDto(Breeder breeder) {
        // Generate JWT token
      String jwtToken = securityConfiguration.createJwtToken(breeder);

        // Map AppUser to UserResponseDto
        UserResponseDto userResponseDto = userMapper.appUserToUserResponseDto(breeder);

        // Prepare response
        var response = new HashMap<String, Object>();
         response.put("token", jwtToken);
        response.put("user", userResponseDto);

        return ResponseEntity.ok(response);
    }

    private String generateUserID(Breeder breeder) {
        return UUID.randomUUID().toString().substring(0, 10) + breeder.getNomColombie().toLowerCase()  + UUID.randomUUID().toString().substring(0, 10);
    }



        public ResponseEntity<Object> Login(LoginDto loginDto){
        // Authenticate the user
            System.out.println(loginDto.getNomColombie());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getNomColombie(), loginDto.getPassword())
        );
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // You can access the roles here
        for (GrantedAuthority authority : authorities) {
            System.out.println("Role: " + authority.getAuthority());
        }

        Breeder user = userRepository.findByNomColombie(loginDto.getNomColombie());
        return generateUserResponseDto(user);

    }

}
