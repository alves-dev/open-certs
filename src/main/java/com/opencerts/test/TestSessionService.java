package com.opencerts.test;

import com.opencerts.certification.CertificationService;
import com.opencerts.shared.TestSessionDTO;
import com.opencerts.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TestSessionService {

    private final TestSessionRepository repository;
    private final UserService userService;
    private final CertificationService certificationService;

    public TestSessionService(TestSessionRepository repository, UserService userService, CertificationService certificationService) {
        this.repository = repository;
        this.userService = userService;
        this.certificationService = certificationService;
    }

    public List<ListTestSessionDTO> listByUser() {
        var userId = userService.getUser().id();
        return repository.findByUserId(userId)
                .stream()
                .map(t -> new ListTestSessionDTO(t, certificationService))
                .sorted((a, b) -> b.startedAt().compareTo(a.startedAt()))
                .toList();
    }

    public Optional<TestSession> findByIdentifier(String identifier) {
        var userId = userService.getUser().id();
        return repository.findByUserIdAndIdentifier(userId, identifier);
    }

    public TestSessionDTO findByIdentifierAndCertification(String testIdentifier, String certificationId) {
        TestSession test = createOrGetByIdentifier(testIdentifier, certificationId);
        return new TestSessionDTO(test);
    }

    public TestSessionDTO addQuestionInTest(String testIdentifier, String certificationId, UUID questionId, boolean answerCorrect) {
        TestSession test = createOrGetByIdentifier(testIdentifier, certificationId);
        test.markQuestionResult(questionId, answerCorrect);
        repository.save(test);
        return new TestSessionDTO(test);
    }

    private TestSession createOrGetByIdentifier(String identifier, String certificationId) {
        var userId = userService.getUser().id();
        Optional<TestSession> optionalTestSession = repository.findByUserIdAndIdentifier(userId, identifier);
        if (optionalTestSession.isPresent())
            return optionalTestSession.get();

        var test = new TestSession(userId, certificationId, identifier);
        return repository.save(test);
    }
}
