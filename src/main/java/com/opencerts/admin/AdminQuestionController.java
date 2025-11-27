package com.opencerts.admin;

import com.opencerts.certification.Question;
import com.opencerts.certification.QuestionRepository;
import com.opencerts.certification.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/questions")
public class AdminQuestionController {

    private final QuestionRepository questionRepository;

    public AdminQuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping("/export")
    public ResponseEntity<List<OutputQuestion>> exportQuestions() {
        List<Question> questions = questionRepository.findAll();

        List<OutputQuestion> outputQuestions = questions.stream().map(q -> {
            Map<String, Boolean> answers = q.responses().stream()
                    .collect(Collectors.toMap(
                            Response::text,
                            r -> q.checkOneAnswer(r.text()),
                            (v1, v2) -> v1
                    ));
            return new OutputQuestion(
                    q.id().toString(),
                    q.certification(),
                    q.topic(),
                    q.description(),
                    answers
            );
        }).toList();

        return ResponseEntity.ok(outputQuestions);
    }

    @PostMapping("/import")
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

        list.forEach(questionRepository::save);

        return ResponseEntity.ok("Import OK: " + questions.size());
    }

    record InputQuestion(
            String certification,
            String topic,
            String description,
            List<String> responses,
            List<Integer> correctIndexes
    ) {
    }

    record OutputQuestion(
            String questionId,
            String certificationId,
            String topic,
            String description,
            Map<String, Boolean> answers
    ) {
    }
}
