package com.opencerts.certification;

import com.opencerts.certification.response.CertificationDTO;
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

    private List<CertificationQuestionCount> questionCountCache = null;

    public QuestionService(QuestionRepository repository, TestSessionService testSessionService,
                           MongoTemplate mongoTemplate, CertificationService certificationService) {
        this.repository = repository;
        this.testSessionService = testSessionService;
        this.mongoTemplate = mongoTemplate;
        this.certificationService = certificationService;
    }

    public Question save(Question question) {
        return repository.save(question);
    }

    public Question findRandomByCertification(String certificationId, String testIdentifier) {
        Optional<TestSession> test = testSessionService.findByIdentifier(testIdentifier);

        Set<UUID> answeredIds = test
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

    public Question getQuestionById(UUID questionId) {
        return repository.findById(questionId).get();
    }

    public List<Question> findAllById(List<UUID> uuids) {
        return uuids.stream().map(q -> repository.findById(q).get()).toList();
    }

    public List<CertificationQuestionCount> countByCertification() {
        if (questionCountCache != null) {
            return questionCountCache;
        }

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.group("certification").count().as("total"),
                Aggregation.project("total").and("_id").as("certificationId")
        );

        List<CertificationQuestionCount> questionCount = mongoTemplate.aggregate(agg, "questions", CertificationQuestionCount.class).getMappedResults();

        List<CertificationDTO> certificationDTOList = certificationService.listAll();
        questionCountCache = new ArrayList<>();

        for (var qc : questionCount) {
            var cert = certificationDTOList.stream()
                    .filter(c -> c.id().equals(qc.certificationId()))
                    .findFirst().get();
            questionCountCache.add(new CertificationQuestionCount(qc.certificationId(), cert.provider(), cert.displayNameWithoutProvider(), qc.total()));
        }

        certificationDTOList.stream()
                .filter(cert -> questionCount.stream().noneMatch(qc -> qc.certificationId().equals(cert.id())))
                .forEach(cert -> questionCountCache.add(new CertificationQuestionCount(cert.id(), cert.provider(), cert.displayNameWithoutProvider(), 0)));

        return questionCountCache;
    }
}
