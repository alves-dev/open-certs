package com.opencerts.challenge;

import com.opencerts.certification.Certification;
import com.opencerts.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Document("challenges")
public class Challenge {

    @Id
    private String id;

    private String name;

    @DBRef
    private Certification certification;

    @DBRef
    @Indexed
    private User createBy;

    private List<String> questionIds;
    private LocalDateTime createdAt;
    private Map<String, ChallengeProgress> progressByUser = new java.util.HashMap<>();

    Challenge() {
    }

    public Challenge(String name, Certification certification, User user, List<String> questionIds) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.certification = certification;
        this.createBy = user;
        this.createdAt = LocalDateTime.now();
        this.questionIds = questionIds;
        progressByUser.put(user.id(), ChallengeProgress.init());
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public User createBy() {
        return createBy;
    }

    public Certification certification() {
        return certification;
    }

    public List<String> questionIds() {
        return questionIds;
    }

    public String getNextQuestionIdForUser(String userId) {
        ChallengeProgress progress = progressByUser.get(userId);

        List<String> unansweredQuestions = questionIds.stream()
                .filter(id -> !progress.questionsAnswered().contains(id))
                .toList();

        if (unansweredQuestions.isEmpty())
            return null;

        return unansweredQuestions.get(new Random().nextInt(unansweredQuestions.size()));
    }

    public Map<String, ChallengeProgress> progressByUser() {
        return progressByUser;
    }

    public void addParticipant(User currentUser) {
        progressByUser.put(currentUser.id(), ChallengeProgress.init());
    }

    public void withAnsweredQuestion(String questionId, boolean isCorrect, String userId) {
        ChallengeProgress progress = progressByUser.get(userId);
        if (progress.finished) return;

        progress.withAnsweredQuestion(questionId, isCorrect);
        if (progress.questionsAnswered().size() == questionIds.size())
            progress.finished();
    }

    public String status() {
        boolean allFinished = progressByUser.values().stream()
                .allMatch(ChallengeProgress::isFinished);
        return allFinished ? "FINALIZADO" : "EM ANDAMENTO";
    }

    static class ChallengeProgress {
        private List<String> questionsCorrect;
        private List<String> questionsIncorrect;
        private boolean finished;

        public ChallengeProgress() {
            this.questionsCorrect = new ArrayList<>();
            this.questionsIncorrect = new ArrayList<>();
            this.finished = false;
        }

        public List<String> questionsCorrect() {
            return questionsCorrect;
        }

        public List<String> questionsIncorrect() {
            return questionsIncorrect;
        }

        public boolean isFinished() {
            return finished;
        }

        public int score(int totalQuestions) {
            return questionsCorrect.size() * 100 / totalQuestions;
        }

        public List<String> questionsAnswered() {
            List<String> answered = new ArrayList<>(questionsCorrect);
            answered.addAll(questionsIncorrect);
            return answered;
        }

        private static ChallengeProgress init() {
            return new ChallengeProgress();
        }

        private void finished() {
            this.finished = true;
        }

        private void withAnsweredQuestion(String questionId, boolean isCorrect) {
            if (isCorrect)
                questionsCorrect.add(questionId);
            else
                questionsIncorrect.add(questionId);
        }
    }
}
