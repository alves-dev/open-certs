package com.opencerts.certification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/questions/import")
public class QuestionImportController {

    private final QuestionRepository repo;

    public QuestionImportController(QuestionRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<String> importQuestions(@RequestBody List<InputQuestion> questions) {
        var list = questions.stream().map(q -> {
            var responses = q.responses().stream().map(Response::new).toList();
            var correctAnswers = q.correctIndexes().stream().map(responses::get).toList();
            return new Question(
                    q.certification(),
                    q.description(),
                    q.topic(),
                    responses,
                    correctAnswers
            );
        }).toList();

        list.forEach(repo::save);

        return ResponseEntity.ok("Import OK: " + questions.size());
    }

    public record InputQuestion(
            String certification,
            String topic,
            String description,
            List<String> responses,
            List<Integer> correctIndexes
    ) {}
}
