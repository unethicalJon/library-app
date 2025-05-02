package com.example.library.service;

import brevo.ApiClient;
import brevo.ApiException;
import brevo.Configuration;
import brevoApi.TransactionalEmailsApi;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailTo;
import com.example.library.configuration.BrevoConfig;
import com.example.library.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private final BrevoConfig brevoConfig;

    @Autowired
    public EmailService(BrevoConfig brevoConfig) {
        this.brevoConfig = brevoConfig;
    }

    public void sendActivationEmail(User user) {
        try {
            TransactionalEmailsApi emailApi = new TransactionalEmailsApi(brevoConfig.getApiClient());

            Map<String, Object> params = new HashMap<>();
            params.put("firstname", user.getName());
            params.put("lastname", user.getSurname());

            SendSmtpEmail email = new SendSmtpEmail()
                    .to(Collections.singletonList(new SendSmtpEmailTo().email(user.getEmail())))
                    .templateId(1L)
                    .params(params);

            emailApi.sendTransacEmail(email);
            System.out.println("Activation email sent successfully to: " + user.getEmail());

        } catch (Exception e) {
            System.err.println("Error sending activation email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
