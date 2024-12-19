package com.example.SpringSecurityDemo.Config;
import com.example.SpringSecurityDemo.Entity.Role.Role;
import com.example.SpringSecurityDemo.Entity.User.Breeder;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${security.jwt.issuer}")
    private String jwtIssuer;

    private final   CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http , CorsConfigurationSource corsConfigurationSource) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // Utilisation de la configuration CORS
                .authorizeHttpRequests((authorize) -> {
                    authorize.requestMatchers("/account/register" , "/account/login").permitAll();
                    authorize.requestMatchers("/Api/pigeons/**", "/Api/breeders/{breederId}" , "/account/current-user").hasAnyRole("USER", "ADMIN");
                    authorize.requestMatchers("/Api/breeders/**","/account/users/{userId}/roles").hasRole("ADMIN");
                    authorize.requestMatchers("/Api/Competition/**" , "/Api/CompetitionPigeon/**" , "/account/current-user").hasRole("ORGANIZER");
                    authorize.anyRequest().authenticated();
                })
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // Custom 401 handler
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            customAuthenticationEntryPoint.handleAccessDeniedException(request, response, (AccessDeniedException) accessDeniedException);
                        })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jw -> {
                            JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
                            grantedAuthoritiesConverter.setAuthorityPrefix("");
                            grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

                            Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jw);
                            return new JwtAuthenticationToken(jw, authorities, jw.getSubject());
                        }))
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        var secretKey = new SecretKeySpec(jwtSecretKey.getBytes(), "");
        return NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm (MacAlgorithm.HS256).build();
    }

    public String createJwtToken(Breeder appUser) {
        Instant now = Instant.now();

        // Map roles to a list of strings
        List<String> roles = appUser.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        // Build JWT claims
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtIssuer) // Your issuer name
                .issuedAt(now)
                .expiresAt(now.plusSeconds(24 * 3600)) // 1 day expiration
                .subject(appUser.getUsername()) // Subject of the token
                .claim("roles", roles)
                .claim("email", appUser.getNomColombie()) // Add roles claim
                .build();

        // Encode the token
        var encoder = new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecretKey.getBytes()));
        var params = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        return encoder.encode(params).getTokenValue();
    }





}
