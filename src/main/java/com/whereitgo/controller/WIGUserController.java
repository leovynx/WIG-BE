package com.whereitgo.controller;

import com.whereitgo.model.WIGUser;
import com.whereitgo.utility.WIGResponse;
import com.whereitgo.utility.WIGResponseStatus;
import com.whereitgo.service.WIGUserService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class WIGUserController {

    private final WIGUserService userService;

    // CREATE USER
    @PostMapping
    public WIGResponse<WIGUser> createUser(
            @RequestBody WIGUser user) {

        WIGUser savedUser = userService.createUser(user);

        return WIGResponse.success(
                savedUser,
                201,
                "User created successfully");
    }

    // GET USER BY ID
    @GetMapping("/{userId}")
    public WIGResponse<WIGUser> getUserById(
            @PathVariable String userId) {

        WIGUser user = userService.getUserById(userId);

        return WIGResponse.success(
                user,
                200,
                "User fetched successfully");
    }

    // GET ALL USERS
    @GetMapping
    public WIGResponse<List<WIGUser>> getAllUsers() {

        List<WIGUser> users = userService.getAllUsers();

        return WIGResponse.success(
                users,
                200,
                "Users fetched successfully");
    }

    // UPDATE USER
    @PutMapping("/{userId}")
    public WIGResponse<WIGUser> updateUser(
            @PathVariable String userId,
            @RequestBody WIGUser updatedUser) {

        WIGUser user = userService.updateUser(userId, updatedUser);

        return WIGResponse.<WIGUser>builder()
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