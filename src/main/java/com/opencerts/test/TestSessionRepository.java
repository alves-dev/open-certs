package com.opencerts.test;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestSessionRepository extends MongoRepository<TestSession, String> {
    Optional<TestSession> findByUserIdAndIdentifier(UUID userId, String identifier);

    List<TestSession> findByUserId(UUID userId);
}
