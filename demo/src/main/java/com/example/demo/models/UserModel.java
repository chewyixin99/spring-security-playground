package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    // this file is similar to the create/updateUserRequest in our project
    private String username;
    private String email;
    private String password;
    private String matchingPassword;
}
