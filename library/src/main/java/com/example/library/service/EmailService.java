package com.example.library.service;

import brevo.ApiClient;
import brevo.ApiException;
import brevo.Configuration;
import brevoApi.TransactionalEmailsApi;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailTo;
import com.example.library.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    public void sendActivationEmail(User user) {
        try {
            // Initialize Brevo API client
            ApiClient defaultClient = Configuration.getDefaultApiClient();
            defaultClient.setApiKey(apiKey);

            // Initialize the Brevo Transactional Email API
            TransactionalEmailsApi emailApi = new TransactionalEmailsApi(defaultClient);

            // Prepare parameters for the Brevo template
            Map<String, Object> params = new HashMap<>();
            params.put("FIRSTNAME", user.getName());
            params.put("LASTNAME", user.getSurname());

            // Create the email request
            SendSmtpEmail email = new SendSmtpEmail()
                    .to(Collections.singletonList(new SendSmtpEmailTo().email(user.getEmail())))
                    .templateId(1L)
                    .params(params);

            // Send the email
            emailApi.sendTransacEmail(email);
            System.out.println("Activation email sent successfully to: " + user.getEmail());

        } catch (ApiException e) {
            System.err.println("Error while sending activation email: " + e.getMessage());
            System.err.println("Response body: " + e.getResponseBody()); // Log detailed error response
            e.printStackTrace(); // Optional: Print full stack trace for debugging
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
