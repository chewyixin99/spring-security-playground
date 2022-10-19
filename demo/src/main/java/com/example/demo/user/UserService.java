package com.example.demo.user;

import com.example.demo.models.User;
import com.example.demo.models.UserModel;
import com.example.demo.models.VerificationToken;

public interface UserService {

    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);
    
}
