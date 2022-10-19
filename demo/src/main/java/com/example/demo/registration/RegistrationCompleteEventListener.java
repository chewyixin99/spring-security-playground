package com.example.demo.registration;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import com.example.demo.models.User;
import com.example.demo.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j // used to log verification 'email' to console, reimplement with email
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // create verification token for the user link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);

        // send mail to user
        String url = event.getApplicationUrl() + "verifyRegistration?token=" + token;
        // ideally this is where we send the email, 'sendVerificationEmail()'
        log.info("Click the link to verify your account{}", url); // to reimplement with email instead of logging jsut to console
    }

    
    
}
