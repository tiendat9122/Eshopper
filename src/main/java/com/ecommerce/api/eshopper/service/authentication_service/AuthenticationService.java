package com.ecommerce.api.eshopper.service.authentication_service;

import com.ecommerce.api.eshopper.auth.AuthenticationRequest;
import com.ecommerce.api.eshopper.auth.AuthenticationResponse;
import com.ecommerce.api.eshopper.entity.Role;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.repository.RoleCustomRepo;
import com.ecommerce.api.eshopper.repository.UserRepository;
import com.ecommerce.api.eshopper.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleCustomRepo roleCustomRepo;
    private final JwtService jwtService;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        Authentication authen = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authenticationRequest.getEmail(),
                                authenticationRequest.getPassword()
                        ));

        SecurityContextHolder.getContext().setAuthentication(authen);

        User user = SecurityUtils.getPrinciple();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        var jwtToken = jwtService.generateToken(user, authorities);

        var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

//    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
//        Authentication authen =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
//        User user = userRepository.findByEmail(authenticationRequest.getEmail());
//        List<Role> role = null;
//        if(user != null) {
//            role = roleCustomRepo.getRole(user);
//        }
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        Set<Role> set = new HashSet<>();
//        role.stream().forEach(c->set.add(new Role(c.getName())));
//        user.setRole(set);
//        set.stream().forEach(i->authorities.add(new SimpleGrantedAuthority(i.getName())));
//
//        var jwtToken = jwtService.generateToken(user, authorities);
//        var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);
//        return AuthenticationResponse.builder().token(jwtToken).refreshToken(jwtRefreshToken).build();
//    }
}
