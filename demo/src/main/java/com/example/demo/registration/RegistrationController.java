package com.example.demo.registration;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.PasswordModel;
import com.example.demo.models.User;
import com.example.demo.models.UserModel;
import com.example.demo.models.VerificationToken;
import com.example.demo.user.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j // used to log verification 'email' to console, reimplement with email, to remove
public class RegistrationController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid")) {
            return "User verified successfully";
        }
        return "Bad user";
    }

    @GetMapping("/resendVerificationToken")
    public String resentVerificationToken(
        @RequestParam("token") String oldToken,
        HttpServletRequest request
    ) {
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);

        User user = verificationToken.getUser();

        // send email
        resendVerificationTokenMail(user, applicationUrl(request), verificationToken);
        return "Verification link sent" ;
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if (user != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = passwordResetTokenMail(user, applicationUrl(request), token);
        }
        
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(
        @RequestParam("token") String token,
        @RequestBody PasswordModel passwordModel
    ) {
        String result = userService.validatePasswordResetToken(token);
        if (!result.equalsIgnoreCase("valid")) {
            return "Invalid token";
        }
        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if (user.isPresent()) {
            userService.changePassword(user.get(), passwordModel.getNewPassword());
            return "Password reset successful";
        } else {
            return "Invalid token";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel) {
        User user = userService.findUserByEmail(passwordModel.getEmail()); // handle exception: null pointer exception if user does not exist
        if (!userService.checkIfValidOldPassword(user, passwordModel.getOldPassword())) {
            return "Invalid old password";
        }
        // Save new password
        userService.changePassword(user, passwordModel.getNewPassword());
        return "Password changed successfully";
    }

    private String passwordResetTokenMail(
        User user,
        String applicationUrl,
        String token
    ) {
        // send email to user
        String url = applicationUrl + "/savePassword?token=" + token;
        // ideally this is where we send the email, 'sendVerificationEmail()'
        log.info("Click the link to reset your password {}", url); // to reimplement with email instead of logging jsut to console

        return url;
    }

    private void resendVerificationTokenMail(
        User user, 
        String applicationUrl,
        VerificationToken verificationToken
    ) {
        // send email to user
        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();
        // ideally this is where we send the email, 'sendVerificationEmail()'
        log.info("Click the link to verify your account {}", url); // to reimplement with email instead of logging jsut to console
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
