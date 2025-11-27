package com.opencerts.test;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TestSessionRepository extends MongoRepository<TestSession, String> {
    Optional<TestSession> findByUserIdAndIdentifier(String userId, String identifier);

    List<TestSession> findByUserId(String userId);
}
