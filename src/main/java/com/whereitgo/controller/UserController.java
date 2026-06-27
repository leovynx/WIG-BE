package com.whereitgo.controller;

import com.whereitgo.model.AuthResponse;
import com.whereitgo.model.User;
import com.whereitgo.utility.httpEntity.WIGResponse;
import com.whereitgo.utility.httpEntity.WIGResponseStatus;
import com.whereitgo.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

        private final UserService userService;

        // CREATE USER
        @PostMapping
        public WIGResponse<AuthResponse> createUser(
                        @RequestBody User user) {

                String token = userService.createUser(user);

                return WIGResponse.success(
                                new AuthResponse(token),
                                201,
                                "User created successfully");
        }

        // GET USER BY ID
        @GetMapping
        public WIGResponse<User> getUserById() {

                User user = userService.getCurrentUser();

                return WIGResponse.success(
                                user,
                                200,
                                "User fetched successfully");
        }

        // UPDATE USER
        @PutMapping("/{userId}")
        public WIGResponse<User> updateUser(
                        @PathVariable String userId,
                        @RequestBody User updatedUser) {

                User user = userService.updateUser(userId, updatedUser);

                return WIGResponse.<User>builder()
                                .response(user)
                                .status(
                                                WIGResponseStatus.builder()
                                                                .statusCode(200)
                                                                .statusMessage("User updated successfully")
                                                                .build())
                                .build();
        }

        // DELETE USER
        @DeleteMapping("/{userId}")
        public WIGResponse<String> deleteUser(
                        @PathVariable String userId) {

                userService.deleteUser(userId);

                return WIGResponse.success(
                                "User deleted successfully",
                                200,
                                "User deleted successfully");
        }
}