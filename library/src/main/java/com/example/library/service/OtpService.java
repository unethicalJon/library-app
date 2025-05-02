package com.example.library.service;

import brevoApi.TransactionalEmailsApi;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailTo;
import com.example.library.configuration.BrevoConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
    private final BrevoConfig brevoConfig;

    public void storeOtp(String username, String otp) {
        otpStore.put(username, otp);
    }

    public boolean verifyOtp(String username, String otp) {
        return otp.equals(otpStore.get(username));
    }

    public void sendOtpEmail(String name, String toEmail, String otp) {
        try {
            TransactionalEmailsApi apiInstance = new TransactionalEmailsApi(brevoConfig.getApiClient());

            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail()
                    .templateId(2L)
                    .to(Collections.singletonList(new SendSmtpEmailTo().email(toEmail)))
                    .params(Map.of(
                            "firstname", name,
                            "otp", otp
                    ));

            apiInstance.sendTransacEmail(sendSmtpEmail);
            System.out.println("OTP email sent successfully to " + toEmail);

        } catch (Exception e) {
            System.err.println("Error sending OTP email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
