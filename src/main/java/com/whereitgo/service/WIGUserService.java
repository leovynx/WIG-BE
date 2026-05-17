package com.whereitgo.service;

import com.whereitgo.exceptionHandling.WIGUserExceptions;
import com.whereitgo.model.WIGUser;
import com.whereitgo.repository.WIGUserRepository;
import com.whereitgo.utility.UserIdGenerator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WIGUserService {

    private final WIGUserRepository userRepository;

    public WIGUser createUser(WIGUser user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new WIGUserExceptions.EmailAlreadyExistsException(
                    "Email already exists");
        }

        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new WIGUserExceptions.PhoneNumberAlreadyExistsException(
                    "Phone number already exists");
        }

        user.setUserId(
                UserIdGenerator.generateUserId());

        return userRepository.save(user);
    }

    public WIGUser getUserById(String userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new WIGUserExceptions.UserNotFoundException("User not found"));
    }

    public List<WIGUser> getAllUsers() {

        return userRepository.findAll();
    }

    public WIGUser updateUser(String userId, WIGUser updatedUser) {

        WIGUser existingUser = userRepository.findById(userId)
                .orElseThrow(() ->
                        new WIGUserExceptions.UserNotFoundException("User not found"));

        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setDob(updatedUser.getDob());
        existingUser.setCountry(updatedUser.getCountry());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

        return userRepository.save(existingUser);
    }

    public void deleteUser(String userId) {

        WIGUser existingUser = userRepository.findById(userId)
                .orElseThrow(() ->
                        new WIGUserExceptions.UserNotFoundException("User not found"));

        userRepository.delete(existingUser);
    }
}