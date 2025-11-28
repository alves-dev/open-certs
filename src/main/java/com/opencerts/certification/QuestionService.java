package com.opencerts.certification;

import com.opencerts.certification.response.CertificationQuestionCount;
import com.opencerts.test.TestSession;
import com.opencerts.test.TestSessionService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class QuestionService {

    private final QuestionRepository repository;
    private final TestSessionService testSessionService;
    private final MongoTemplate mongoTemplate;
    private final CertificationService certificationService;

    private List<CertificationQuestionCount.DTO> questionCountCache = null;

    public QuestionService(QuestionRepository repository, TestSessionService testSessionService,
                           MongoTemplate mongoTemplate, CertificationService certificationService) {
        this.repository = repository;
        this.testSessionService = testSessionService;
        this.mongoTemplate = mongoTemplate;
        this.certificationService = certificationService;
    }

    public Question save(Question question) {
        questionCountCache = null;
        return repository.save(question);
    }

    public Question findRandomByCertification(String certificationId, String testIdentifier) {
        Optional<TestSession> test = testSessionService.findByIdentifier(testIdentifier);

        Set<String> answeredIds = test
                .map(TestSession::answeredQuestionIds)
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

    public Question getById(String questionId) {
        return repository.findById(questionId).get();
    }

    public List<Question> findAllById(List<String> ids) {
        return ids.stream().map(q -> repository.findById(q).get()).toList();
    }

    public List<CertificationQuestionCount.DTO> countByCertification() {
        if (questionCountCache != null) {
            return questionCountCache;
        }

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.group("certification").count().as("total"),
                Aggregation.project("total").and("_id").as("certification")
        );

        List<CertificationQuestionCount> questionCount = mongoTemplate
                .aggregate(agg, "questions", CertificationQuestionCount.class)
                .getMappedResults();

        questionCountCache = new ArrayList<>();

        for (var qc : questionCount)
            questionCountCache.add(new CertificationQuestionCount(qc.certification(), qc.total()).toDTO());

        certificationService.listAll().stream()
                .filter(cert -> questionCount.stream().noneMatch(qc -> qc.certification().equals(cert)))
                .forEach(cert -> questionCountCache.add(new CertificationQuestionCount(cert, 0).toDTO()));

        return questionCountCache;
    }

    public List<String> getRandomQuestionIdsForCertification(String certificationId, int numberOfQuestions) {
        List<Question> questions = repository.findByCertification(certificationId);
        Collections.shuffle(questions);
        return questions.stream()
                .limit(numberOfQuestions)
                .map(Question::id)
                .toList();
    }
}
