package com.opencerts.certification;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class QuestionService {

    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public Question save(Question question) {
        return repository.save(question);
    }

    public Question findRandomByCertification(String certificationId) {
        var all = repository.findByCertification(certificationId);
        if (all.isEmpty())
            return null;
        return all.get(new Random().nextInt(all.size()));
    }

    public Question getQuestionById(UUID questionId) {
        return repository.findById(questionId).get();
    }
}
