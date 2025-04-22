package com.example.library.security;

import com.example.library.service.OAuth2Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2Service oAuth2Service;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException{
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        try {
            String jwt = oAuth2Service.handleOAuth2User(oAuth2User);

            response.setContentType("application/json");
            response.getWriter().write("{\"token\": \"" + jwt + "\"}");
            response.getWriter().flush();
        } catch (AccessDeniedException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
        }
    }
}
