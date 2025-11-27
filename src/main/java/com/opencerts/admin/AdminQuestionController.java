package com.opencerts.admin;

import com.opencerts.certification.Certification;
import com.opencerts.certification.CertificationService;
import com.opencerts.certification.Question;
import com.opencerts.certification.QuestionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/questions")
public class AdminQuestionController {

    private final QuestionRepository questionRepository;
    private final CertificationService certificationService;

    public AdminQuestionController(QuestionRepository questionRepository, CertificationService certificationService) {
        this.questionRepository = questionRepository;
        this.certificationService = certificationService;
    }

    @GetMapping("/export")
    public ResponseEntity<List<OutputQuestion>> exportQuestions() {
        List<Question> questions = questionRepository.findAll();

        List<OutputQuestion> outputQuestions = questions.stream().map(q -> {
            Map<String, Boolean> answers = q.answers().stream()
                    .collect(Collectors.toMap(
                            Question.Answer::text,
                            Question.Answer::correct
                    ));

            return new OutputQuestion(
                    q.id(),
                    q.certification().id(),
                    q.topic(),
                    q.description(),
                    answers
            );
        }).toList();

        return ResponseEntity.ok(outputQuestions);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importQuestions(@RequestBody List<OutputQuestion> questions) {
        var list = questions.stream().map(q -> {
            Certification certification = certificationService.getById(q.certificationId).get();

            List<Question.Answer> answers = new ArrayList<>();
            q.answers.forEach(
                    (text, correct) -> answers.add(new Question.Answer(text, correct))
            );

            return new Question(certification, q.description(), q.topic(), answers, null);
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
