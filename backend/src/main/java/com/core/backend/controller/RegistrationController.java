package com.core.backend.controller;

import com.core.backend.exception.NoUserException;
import com.core.backend.exception.NoVerificationTokenException;
import com.core.backend.exception.TokenExpiredException;
import com.core.backend.registration.OnRegistrationCompleteEvent;
import com.core.backend.service.RegistrationService;
import com.core.backend.model.User;
import com.core.backend.dto.RegisterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/registration")
public class RegistrationController {

    @Autowired
    private RegistrationService service;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @PostMapping("/register")
    ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterUser userDto, BindingResult result) {
        if(result.hasErrors()){
            return new ResponseEntity<>("Niepoprawne dane rejestracji", HttpStatus.BAD_REQUEST);
        }
        User registered = null;
        if (userDto != null) {
            try {
                registered = service.registerNewUserAccount(userDto);
                eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered));
            }
            catch (IllegalArgumentException ex){
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
            catch (Exception e) {
                service.deleteUnconfirmedUser(registered);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("Użytkownik zarejestrowany. Należy go potwierdzic za pomoca emaila", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Zły payload", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/confirm_registration")
    public ResponseEntity<Object> confirmRegistration(@RequestParam("token") String token) {
        try {
            service.confirmUser(token);
        }catch(NoVerificationTokenException e){
            return new ResponseEntity<>("Nie znaleziono tokenu", HttpStatus.NOT_FOUND);
        }catch(TokenExpiredException e){
            return new ResponseEntity<>("Token stracił ważność", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Użytkownik potwierdzony", HttpStatus.OK);
    }

    @GetMapping("/reset_token")
    public ResponseEntity<Object> resetVerificationToken(@RequestParam("email") String email) {
        User user;
        try {
            user = service.resetVerificationToken(email);
        }catch(NoUserException e){
            return new ResponseEntity<>("Uzytkownik nie znaleziony", HttpStatus.BAD_REQUEST);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>("Uzytkownik został już potwierdzony", HttpStatus.BAD_REQUEST);
        }
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
        return new ResponseEntity<>("Nowy token został wysłany na email", HttpStatus.OK);
    }

}
