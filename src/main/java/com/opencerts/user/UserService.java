package com.opencerts.user;

import com.opencerts.shared.UserDTO;
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

    public User getCurrent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User user) {
            return user;
        }
        log.warn("No authenticated user found in security context");
        return null;
    }

    public UserDTO getDTOById(String userId) {
        return repository.findById(userId)
                .map(user -> new UserDTO(user.id(), user.name()))
                .orElseThrow();
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
