package com.franklin.backend.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.franklin.backend.entity.User;
import com.franklin.backend.entity.User.Role;
import com.franklin.backend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        try {
            CustomOAuth2User oAuthUser = (CustomOAuth2User) authentication.getPrincipal();
            User user = userService.currentUser(oAuthUser);

            String redirectUri = (String) request.getSession().getAttribute("REDIRECT_URI");
            request.getSession().removeAttribute("REDIRECT_URI");

            String targetUrl;
            if (user.getRole().equals(Role.NEW_USER)) {
                targetUrl = frontendUrl + "/register";
            } else if (redirectUri != null && !redirectUri.isEmpty()) {
                targetUrl = redirectUri;
            } else {
                targetUrl = frontendUrl + "/chat-app";
            }

            System.out.println("Redirecting to: " + targetUrl);

            response.setHeader("Access-Control-Allow-Origin", frontendUrl);
            response.setHeader("Access-Control-Allow-Credentials", "true");

            clearAuthenticationAttributes(request);

            response.sendRedirect(targetUrl);

        } catch (Exception e) {
            System.err.println("Error in authentication success handler: " + e.getMessage());
            response.sendRedirect(frontendUrl + "/login?error=authentication_error");
        }
    }
}