package com.example.demo.registration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
