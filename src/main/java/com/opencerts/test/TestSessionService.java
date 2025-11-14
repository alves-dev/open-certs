package com.opencerts.test;

import com.opencerts.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TestSessionService {

    private final TestSessionRepository repository;
    private final UserService userService;

    public TestSessionService(TestSessionRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public List<ListTestSessionDTO> listByUser(){
        var userId = userService.getUser().id();
        return repository.findByUserId(userId).stream().map(ListTestSessionDTO::new).toList();
    }

    public TestSessionDTO findByIdentifierAndCertification(String testIdentifier, String certificationId){
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
