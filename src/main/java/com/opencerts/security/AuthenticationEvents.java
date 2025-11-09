package com.opencerts.security;

import com.opencerts.user.User;
import com.opencerts.user.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class AuthenticationEvents {

    private final UserService userService;

    public AuthenticationEvents(UserService userService) {
        this.userService = userService;
    }

    @EventListener
    public void onAuthenticationSuccess(InteractiveAuthenticationSuccessEvent event) {
        String name = null;
        String email = null;
        String providerStr = null;

        String providerId = null;
        String profileImage = null;

        if (event.getSource() instanceof OAuth2AuthenticationToken authenticationToken) {
            providerStr = authenticationToken.getAuthorizedClientRegistrationId();
        }

        OAuth2User userAuth = (OAuth2User) event.getAuthentication().getPrincipal();

        if (Objects.equals(providerStr, "google")) {
            name = userAuth.getAttribute("name");
            email = userAuth.getAttribute("email");
            providerId = userAuth.getAttribute("sub");
            profileImage = userAuth.getAttribute("picture");
        }

        if (name == null || providerId == null)
            return;

        User user = User.create(name, profileImage, email, providerId);
        user = userService.createUpdateWhenLogin(user);

        // cria uma nova autenticação
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of()
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
