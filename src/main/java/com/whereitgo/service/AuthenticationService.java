package com.whereitgo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.whereitgo.exceptionHandling.UserExceptions;
import com.whereitgo.model.AuthResponse;
import com.whereitgo.model.User;
import com.whereitgo.repository.UserRepo;
import com.whereitgo.utility.httpEntity.LoginRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepository;
    private final JWTService jwtService;

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new UserExceptions.UserNotFoundException("User not found"));

        String token = jwtService.generateToken(user.getUserId());

        return new AuthResponse(token, user);
    }
}
