package com.opencerts.certification;

import com.opencerts.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(collection = "questions")
public class Question {

    @Id
    private String id;

    @DBRef
    @Indexed
    private Certification certification;

    private String description;
    private String topic;
    private List<Answer> answers;
    private LocalDateTime createdAt;

    @DBRef
    private User createBy;

    public record Answer(
            String text,
            boolean correct
    ) {
    }

    public Question(Certification certification, String description, String topic, List<Answer> answers,
                    User createBy) {
        this.id = UUID.randomUUID().toString();
        this.certification = certification;
        this.description = description;
        this.topic = topic;
        this.answers = answers;
        this.createdAt = LocalDateTime.now();
        this.createBy = createBy;
    }

    public Question() {
    }

    // Getters //
    public String id() {
        return id;
    }

    public String description() {
        return description;
    }

    public String topic() {
        return topic;
    }

    public Certification certification() {
        return certification;
    }

    public List<Answer> answers(){
        return answers;
    }

    public List<String> answersText() {
        return this.answers.stream().map(Answer::text).toList();
    }

    public List<String> correctAnswers() {
        return this.answers.stream()
                .filter(a -> a.correct)
                .map(a -> a.text)
                .toList();
    }

    // Modificadores //


    // Outros //

    public boolean isMultipleChoice() {
        return this.answers.stream()
                .filter(a -> a.correct)
                .count() > 1;
    }

    public boolean checkAnswers(List<String> answers) {
        for (Answer answer : this.answers) {
            if (answer.correct && !answers.contains(answer.text))
                return false;
        }

        return true;
    }
}
