package com.whereitgo.service;

import com.whereitgo.exceptionHandling.WIGUserExceptions;
import com.whereitgo.model.AuthResponse;
import com.whereitgo.model.WIGUser;
import com.whereitgo.model.WIGUserPrincipal;
import com.whereitgo.repository.WIGUserRepository;
import com.whereitgo.utility.UserIdGenerator;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WIGUserService implements UserDetailsService {

    private final WIGUserRepository userRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private JWTService jwtService;

    public String createUser(WIGUser user) {

        if (user.getEmail() == null || user.getEmail() == "") {
            throw new WIGUserExceptions.EmailAlreadyExistsException(
                    "Email is required");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new WIGUserExceptions.EmailAlreadyExistsException(
                    "Email already exists");
        }

        user.setUserId(
                UserIdGenerator.generateUserId());
        user.setPassword(encoder.encode(user.getPassword()));

        userRepository.save(user);

        return jwtService.generateToken(user.getUserId());
    }

    public WIGUser getUserById(String userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new WIGUserExceptions.UserNotFoundException("User not found"));
    }

    public List<WIGUser> getAllUsers() {

        return userRepository.findAll();
    }

    public WIGUser updateUser(String userId, WIGUser updatedUser) {

        WIGUser existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new WIGUserExceptions.UserNotFoundException("User not found"));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setDob(updatedUser.getDob());
        existingUser.setCountry(updatedUser.getCountry());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

        return userRepository.save(existingUser);
    }

    public void deleteUser(String userId) {

        WIGUser existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new WIGUserExceptions.UserNotFoundException("User not found"));

        userRepository.delete(existingUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Testing");
        WIGUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("User 404");
                    return new WIGUserExceptions.UserNotFoundException("User not found");
                });
        return new WIGUserPrincipal(user);
    }

    public WIGUser getCurrentUser() {

        String userId = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}