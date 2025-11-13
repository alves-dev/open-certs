package com.opencerts.test;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "tests")
public class TestSession {

    @Id
    private String id;

    private UUID userId;
    private String certificationId;
    private String identifier;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    private Status status;

    private int totalCorrect;
    private int totalError;

    private List<UUID> questionsCorrect = new ArrayList<>();
    private List<UUID> questionsIncorrect = new ArrayList<>();

    public enum Status {
        IN_PROGRESS,
        COMPLETED
    }

    public TestSession(UUID userId, String certificationId, String identifier) {
        this.userId = userId;
        this.certificationId = certificationId;
        this.identifier = identifier;
        this.startedAt = LocalDateTime.now();
        this.status = Status.IN_PROGRESS;
    }

    // --- Getters ---
    public String identifier(){
        return identifier;
    }

    public int totalCorrect(){
        return totalCorrect;
    }

    public int totalError(){
        return totalError;
    }

    // --- Métodos de negócio ---

    public void markQuestionResult(UUID questionId, boolean isCorrect) {
        if (isCorrect) {
            questionsCorrect.add(questionId);
            totalCorrect++;
        } else {
            questionsIncorrect.add(questionId);
            totalError++;
        }
    }

//    public void finish() {
//        this.finishedAt = LocalDateTime.now();
//        this.status = Status.COMPLETED;
//    }
}

