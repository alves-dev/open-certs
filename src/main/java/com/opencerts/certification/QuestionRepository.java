package com.opencerts.certification;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends MongoRepository<Question, UUID> {

    List<Question> findByCertification(String certificationId);
}
