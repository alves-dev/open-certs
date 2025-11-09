package com.opencerts.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User user) {
            return user;
        }
        return null;
    }

    public User createUpdateWhenLogin(User newUser) {
        return repository.findByEmail(newUser.email())
                .map(existingUser -> {
                    existingUser.loginDone();
                    return repository.save(existingUser);
                })
                .orElseGet(() -> repository.save(newUser));
    }
}
