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

    public Map<String, ChallengeProgress> progressByUser() {
        return progressByUser;
    }

    public void addParticipant(User currentUser) {
        progressByUser.put(currentUser.id(), ChallengeProgress.init());
    }

    record ChallengeProgress(
            List<String> questionsCorrect,
            List<String> questionsIncorrect,
            boolean finished
    ) {

        public int score(int totalQuestions) {
            return questionsCorrect.size() * 100 / totalQuestions;
        }

        private static ChallengeProgress init() {
            return new ChallengeProgress(
                    new ArrayList<>(),
                    new ArrayList<>(),
                    false
            );
        }

        public List<String> questionsAnswered() {
            List<String> answered = new ArrayList<>(questionsCorrect);
            answered.addAll(questionsIncorrect);
            return answered;
        }
    }
}
