package com.opencerts.test;

import com.opencerts.certification.Question;
import com.opencerts.certification.QuestionService;
import com.opencerts.certification.response.CertificationDTO;
import com.opencerts.util.PromptUtil;

import java.util.List;

record TestSessionDetailsDTO(
        String identifier,
        CertificationDTO certification,
        int totalCorrect,
        int totalError,
        int percentageCorrect,
        List<Question> correctQuestions,
        List<Question> wrongQuestions,
        String wrongQuestionsPrompt
) {

    TestSessionDetailsDTO(TestSession test, QuestionService questionService) {
        List<Question> incorrectQuestions = questionService.findAllById(test.questionsIncorrectIds());

        this(test.identifier(),
                new CertificationDTO(test.certification()),
                test.totalCorrect(),
                test.totalError(),
                test.percentageCorrect(),
                questionService.findAllById(test.questionsCorrectIds()),
                incorrectQuestions,
                PromptUtil.generateBy(incorrectQuestions)
        );
    }
}
