package com.opencerts.certification;

import com.opencerts.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(collection = "questions")
public class Question {

    @Id
    private UUID id;
    private String certification;
    private String description;
    private String topic;

    private List<Response> responses;
    private List<Response> correctAnswers;

    private LocalDateTime createdAt;
    private UUID createBy;

    public Question(String certification, String description, String topic, List<Response> responses,
                    List<Response> correctAnswers, User user) {
        this.id = UUID.randomUUID();
        this.certification = certification;
        this.description = description;
        this.topic = topic;
        this.responses = responses;
        this.correctAnswers = correctAnswers;
        this.createdAt = LocalDateTime.now();
        this.createBy = user.id();
    }

    public Question(String certification, String description, String topic, List<Response> responses, List<Response> correctAnswers) {
        this.id = UUID.randomUUID();
        this.certification = certification;
        this.description = description;
        this.topic = topic;
        this.responses = responses;
        this.correctAnswers = correctAnswers;
        this.createdAt = LocalDateTime.now();
    }

    public Question() {
    }

    // Getters //
    public UUID id() {
        return id;
    }

    public String description() {
        return description;
    }

    public String topic() {
        return topic;
    }

    public List<Response> responses() {
        return responses;
    }

    public List<String> correctAnswers() {
        return correctAnswers.stream().map(Response::text).toList();
    }

    public boolean isMultipleChoice() {
        return this.correctAnswers.size() > 1;
    }

    public boolean checkAnswerByString(List<String> answers) {
        return checkAnswer(
                answers.stream().map(Response::of).toList()
        );
    }

    public boolean checkAnswer(List<Response> answers) {
        if (this.correctAnswers.size() != answers.size())
            return false;

        for (Response answer : answers) {
            if (!this.correctAnswers.contains(answer))
                return false;
        }

        return true;
    }

    // Modificadores //


    // Outros //

}
