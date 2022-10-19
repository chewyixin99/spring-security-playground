package com.example.demo.user;

import com.example.demo.models.User;
import com.example.demo.models.UserModel;

public interface UserService {

    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);
    
}
