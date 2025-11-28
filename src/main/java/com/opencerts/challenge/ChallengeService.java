package com.opencerts.challenge;

import com.opencerts.certification.CertificationService;
import com.opencerts.certification.Question;
import com.opencerts.certification.QuestionService;
import com.opencerts.shared.UserDTO;
import com.opencerts.user.User;
import com.opencerts.user.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserService userService;
    private final QuestionService questionService;
    private final CertificationService certificationService;

    public ChallengeService(ChallengeRepository challengeRepository, UserService userService, QuestionService questionService, CertificationService certificationService) {
        this.challengeRepository = challengeRepository;
        this.userService = userService;
        this.questionService = questionService;
        this.certificationService = certificationService;
    }

    public Challenge create(String name, String certificationId, int numberOfQuestions) {
        List<String> questionIds = questionService.getRandomQuestionIdsForCertification(certificationId, numberOfQuestions);
        var challenge = new Challenge(name, certificationService.getById(certificationId).get(), userService.getCurrent(), questionIds);
        return challengeRepository.save(challenge);
    }

    public Challenge findById(String challengeId) {
        return challengeRepository.findById(challengeId).orElseThrow();
    }

    public List<ChallengeDTO> findForCurrentUser() {
        var currentUser = userService.getCurrent();
        return challengeRepository.findByUser(currentUser.id()).stream()
                .map(challenge -> new ChallengeDTO(
                        challenge.id(),
                        challenge.name(),
                        challenge.certification().displayName(),
                        challenge.questionIds().size(),
                        challenge.progressByUser().get(currentUser.id()).questionsCorrect().size()
                                + challenge.progressByUser().get(currentUser.id()).questionsIncorrect().size(),
                        challenge.progressByUser().size(),
                        challenge.status()
                ))
                .toList();
    }

    public ChallengeInviteDTO getChallengeInviteDTO(String challengeId) {
        var challenge = challengeRepository.findById(challengeId).orElseThrow();

        List<UserDTO> participants = new ArrayList<>();
        challenge.progressByUser().forEach(
                (userId, progress) -> participants.add(userService.getDTOById(userId))
        );

        return new ChallengeInviteDTO(challenge, participants);
    }

    public void acceptInvite(String challengeId) {
        var challenge = challengeRepository.findById(challengeId).orElseThrow();
        var currentUser = userService.getCurrent();
        challenge.addParticipant(currentUser);
        challengeRepository.save(challenge);
    }

    public Question getNextQuestion(String challengeId) {
        User currentUser = userService.getCurrent();
        Challenge challenge = findById(challengeId);
        String questionId = challenge.getNextQuestionIdForUser(currentUser.id());
        if (questionId == null)
            return null;

        return questionService.getById(questionId);
    }

    public void submitAnswer(String challengeId, String questionId, List<String> selectedOptions) {
        User currentUser = userService.getCurrent();
        Challenge challenge = findById(challengeId);
        Question question = questionService.getById(questionId);

        boolean isCorrect = question.checkAnswers(selectedOptions);
        challenge.withAnsweredQuestion(questionId, isCorrect, currentUser.id());
        challengeRepository.save(challenge);
    }
}
