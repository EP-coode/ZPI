package com.core.backend;

import com.core.backend.dto.user.LoginUser;
import com.core.backend.dto.user.RegisterUser;
import com.core.backend.model.User;
import com.core.backend.model.VerificationToken;
import com.core.backend.repository.UserRepository;
import com.core.backend.service.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class BackendRegistrationAndLoginTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    RegistrationService registrationService;
    @Autowired
    UserRepository userRepository;

    private final String correctUserName = "tempUser";
    private final String incorrectUserName = "x";
    private final String correctUserEmail = "sheren_maroneytc@magnet.ny";
    private final String correctUserPassword = "superhaslo123";
    private final String incorrectUserPassword = "abcd";

    @Test
    @Order(1)
    public void testRegistration() {

        RegisterUser registerUser = new RegisterUser();
        registerUser.setName(correctUserName);
        registerUser.setEmail(correctUserEmail);
        registerUser.setPassword(correctUserPassword);

        registerUser(registerUser, status().isCreated());
    }

    @Test
    @Order(2)
    public void testFailedRegistration() {
        RegisterUser registerUser = new RegisterUser();
        registerUser.setName(incorrectUserName);
        registerUser.setEmail(correctUserEmail);
        registerUser.setPassword(correctUserPassword);

        registerUser(registerUser, status().isBadRequest());

        registerUser.setName(correctUserName);
        registerUser.setEmail(correctUserEmail);
        registerUser.setPassword(incorrectUserPassword);

        registerUser(registerUser, status().isBadRequest());
    }

    @Test
    @Order(3)
    public void testLogin() {

        User registeredUser = userRepository.findByEmail(correctUserEmail);
        VerificationToken verificationToken = registrationService.getVerificationToken(registeredUser);
        loginUser(correctUserEmail, correctUserPassword, status().isBadRequest());

        confirmUser(verificationToken.getToken(), status().isOk());
        loginUser(correctUserEmail, correctUserPassword, status().isOk());
    }

    private void registerUser(RegisterUser registerUser, ResultMatcher expectResult) {
        try {
            var mockRequest = MockMvcRequestBuilders
                    .post("http://localhost:8080/registration/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(registerUser));
            mockMvc.perform(mockRequest).andExpect(expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void confirmUser(String verificationToken, ResultMatcher expectResult) {
        var mockRequest = MockMvcRequestBuilders
                .get("http://localhost:8080/registration/confirm_registration?token=" + verificationToken)
                .accept(MediaType.APPLICATION_JSON);
        try {
            mockMvc.perform(mockRequest).andExpect(expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void loginUser(String email, String password, ResultMatcher expectResult) {
        LoginUser loginUser = new LoginUser();
        loginUser.setEmail(email);
        loginUser.setPassword(password);
        try {
            var mockRequest = MockMvcRequestBuilders
                    .post("http://localhost:8080/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(loginUser));
            mockMvc.perform(mockRequest).andExpect(expectResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
