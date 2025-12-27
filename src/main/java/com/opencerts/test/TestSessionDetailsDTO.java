package com.opencerts.test;

import com.opencerts.certification.Question;
import com.opencerts.certification.QuestionService;
import com.opencerts.certification.response.CertificationDTO;
import com.opencerts.util.PromptUtil;

import java.util.List;

record TestSessionDetailsDTO(
        String identifier,
        boolean finished,
        CertificationDTO certification,
        int totalCorrect,
        int totalError,
        int percentageCorrect,
        long timeSpentSeconds,
        List<Question> correctQuestions,
        List<Question> wrongQuestions,
        String wrongQuestionsPrompt
) {

    TestSessionDetailsDTO(TestSession test, QuestionService questionService) {
        List<Question> incorrectQuestions = questionService.findAllById(test.questionsIncorrectIds());

        this(test.identifier(),
                test.isFinished(),
                new CertificationDTO(test.certification()),
                test.totalCorrect(),
                test.totalError(),
                test.percentageCorrect(),
                test.timeSpentSeconds(),
                questionService.findAllById(test.questionsCorrectIds()),
                incorrectQuestions,
                PromptUtil.generateBy(incorrectQuestions)
        );
    }
}
