package com.opencerts.test;

import com.opencerts.certification.CertificationService;
import com.opencerts.certification.Question;
import com.opencerts.certification.QuestionService;
import com.opencerts.certification.response.CertificationDTO;

import java.util.List;

record TestSessionDetailsDTO(
        String identifier,
        CertificationDTO certification,
        int totalCorrect,
        int totalError,
        int percentageCorrect,
        List<Question> correctQuestions,
        List<Question> wrongQuestions
) {

    TestSessionDetailsDTO(TestSession test, CertificationService certificationService, QuestionService questionService) {
        this(test.identifier(),
                CertificationDTO.of(certificationService.getById(test.certificationId())),
                test.totalCorrect(),
                test.totalError(),
                test.percentageCorrect(),
                questionService.findAllById(test.questionsCorrectIds()),
                questionService.findAllById(test.questionsIncorrectIds())
        );
    }
}
