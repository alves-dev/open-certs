package com.opencerts.challenge;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChallengeRepository extends MongoRepository<Challenge, String> {

    @Query("{ 'progressByUser.?0': { $exists: true } }")
    List<Challenge> findByUser(String userId);
}
