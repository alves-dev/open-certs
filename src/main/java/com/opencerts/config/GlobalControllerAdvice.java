package com.opencerts.config;

import com.opencerts.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * Adiciona o objeto 'user' ao Model de todas as views.
     * O Spring Security injeta o User logado através de @AuthenticationPrincipal.
     */
    @ModelAttribute("user")
    public User addLoggedInUser(@AuthenticationPrincipal User user) {
        // Retorna o objeto User (ou null, se não estiver logado, dependendo da sua configuração)
        return user;
    }

    /**
     * Adiciona o objeto 'currentRequest' ao Model de todas as views.
     * Isso permite o acesso a requestURI para highlight da sidebar.
     */
    @ModelAttribute("currentRequest")
    public HttpServletRequest addHttpServletRequest(HttpServletRequest request) {
        return request;
    }
}