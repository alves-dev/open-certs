package com.opencerts.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Profile("dev")
@RestController
public class DeveloperController {

    @GetMapping("/session-info")
    public ResponseEntity<Object> sessionInfo(HttpServletRequest request) {
        return ResponseEntity.ok(
                Map.of(
                        "timeout_seconds", request.getSession().getMaxInactiveInterval(),
                        "timeout_minutes", request.getSession().getMaxInactiveInterval() / 60,
                        "timeout_hours", request.getSession().getMaxInactiveInterval() / 60 / 60
                )
        );
    }
}
