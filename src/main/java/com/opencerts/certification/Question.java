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
    private Response responseCorrect;

    private LocalDateTime createdAt;
    private UUID createBy;

    public Question(String certification, String description, String topic, List<Response> responses, Response correct,
                    User user) {
        this.id = UUID.randomUUID();
        this.certification = certification;
        this.description = description;
        this.topic = topic;
        this.responses = responses;
        this.responseCorrect = correct;
        this.createdAt = LocalDateTime.now();
        this.createBy = user.id();
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

    public boolean checkAnswer(Response answer) {
        return responseCorrect.equals(answer);
    }

    // Modificadores //


    // Outros //

}
