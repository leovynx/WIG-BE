package com.whereitgo.utility.httpEntity;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;
}