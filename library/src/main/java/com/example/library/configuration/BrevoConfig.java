package com.example.library.configuration;

import brevo.ApiClient;
import brevo.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BrevoConfig {

    private final ApiClient apiClient;

    public BrevoConfig(@Value("${brevo.api.key}") String apiKey) {
        this.apiClient = Configuration.getDefaultApiClient();
        this.apiClient.setApiKey(apiKey);
    }

    public ApiClient getApiClient() {
        return apiClient;
    }
}

