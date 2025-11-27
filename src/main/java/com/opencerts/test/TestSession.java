package com.opencerts.test;

import com.opencerts.certification.Certification;
import com.opencerts.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Document(collection = "tests")
public class TestSession {

    @Id
    private String id;

    @DBRef
    @Indexed
    private User user;

    @DBRef
    private Certification certification;

    private String identifier;
    private LocalDateTime startedAt;
    private List<String> questionsCorrect = new ArrayList<>();
    private List<String> questionsIncorrect = new ArrayList<>();

    public TestSession(User user, Certification certification, String identifier) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.certification = certification;
        this.identifier = identifier;
        this.startedAt = LocalDateTime.now();
    }

    // --- Getters ---
    public Certification certification() {
        return certification;
    }

    public String identifier() {
        return identifier;
    }

    public LocalDateTime startedAt() {
        return startedAt;
    }

    public int totalCorrect() {
        return questionsCorrect.size();
    }

    public int totalError() {
        return questionsIncorrect.size();
    }

    List<String> questionsCorrectIds() {
        return questionsCorrect;
    }

    List<String> questionsIncorrectIds() {
        return questionsIncorrect;
    }

    // --- Métodos de negócio ---

    public void markQuestionResult(String questionId, boolean isCorrect) {
        if (isCorrect) {
            questionsCorrect.add(questionId);
        } else {
            questionsIncorrect.add(questionId);
        }
    }

    public int percentageCorrect() {
        int total = totalCorrect() + totalError();
        return total == 0 ? 0 : (int) Math.round(totalCorrect() * 100.0 / total);
    }

    public Set<String> answeredQuestionIds() {
        Set<String> full = new HashSet<>();
        full.addAll(questionsIncorrect);
        full.addAll(questionsCorrect);
        return full;
    }
}

