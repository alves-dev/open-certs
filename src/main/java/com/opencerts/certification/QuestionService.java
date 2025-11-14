package com.opencerts.certification;

import com.opencerts.test.TestSession;
import com.opencerts.test.TestSessionService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class QuestionService {

    private final QuestionRepository repository;
    private final TestSessionService testSessionService;

    public QuestionService(QuestionRepository repository, TestSessionService testSessionService) {
        this.repository = repository;
        this.testSessionService = testSessionService;
    }

    public Question save(Question question) {
        return repository.save(question);
    }

    public Question findRandomByCertification(String certificationId, String testIdentifier) {
        Optional<TestSession> test = testSessionService.findByIdentifier(testIdentifier);

        // obtém IDs já respondidos; ajuste o nome do método conforme sua classe TestSession
        Set<UUID> answeredIds = test
                .map(TestSession::answeredQuestionIds) // -> Set<UUID>
                .orElse(Collections.emptySet());

        var all = repository.findByCertification(certificationId);
        if (all.isEmpty())
            return null;

        // filtra questões já respondidas
        List<Question> available = all.stream()
                .filter(q -> !answeredIds.contains(q.id()))
                .toList();

        if (available.isEmpty())
            return null;

        return available.get(ThreadLocalRandom.current().nextInt(available.size()));
    }

    public Question getQuestionById(UUID questionId) {
        return repository.findById(questionId).get();
    }
}
