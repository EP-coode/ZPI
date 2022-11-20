package com.core.backend.registration;

import com.core.backend.service.RegistrationService;
import com.core.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


@EnableAsync
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private RegistrationService service;

    @Autowired
    private JavaMailSender mailSender;

    @Async
    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = service.getVerificationToken(user).getToken();

        String recipientAddress = user.getEmail();
        String subject = "Potwierdzenie rejestracji";
        String message = "Kliknij w ten link aby potwierdzić rejestrację: http://localhost:3000/confirmation?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}
