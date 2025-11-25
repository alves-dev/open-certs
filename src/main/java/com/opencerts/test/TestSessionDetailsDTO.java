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
        List<Question> wrongQuestions,
        String wrongQuestionsPrompt
) {

    private static final String PROMPT_BASE = """
            Analise esta lista de perguntas de certificação as quais respondi errado.
            Com base nela, identifique os principais temas/serviços/topicos em que estou tendo mais dificuldade.
            Em seguida, gere uma tabela mostrando os 5 tópicos mais problemáticos, ordenados do mais crítico para o menos crítico.
            Para cada tópico/assunto, inclua quantidade de erros.
            
            Aqui estão as perguntas respondidas incorretamente:
            {list_questions}
            """;

    TestSessionDetailsDTO(TestSession test, CertificationService certificationService, QuestionService questionService) {
        List<Question> incorrectQuestions = questionService.findAllById(test.questionsIncorrectIds());

        StringBuilder wrongQuestionsDescriptions = new StringBuilder();
        for (int i = 0; i < incorrectQuestions.size(); i++) {
            wrongQuestionsDescriptions
                    .append(i + 1).append(". ")
                    .append(incorrectQuestions.get(i).description())
                    .append("\n\n");
        }

        this(test.identifier(),
                CertificationDTO.of(certificationService.getById(test.certificationId())),
                test.totalCorrect(),
                test.totalError(),
                test.percentageCorrect(),
                questionService.findAllById(test.questionsCorrectIds()),
                incorrectQuestions,
                PROMPT_BASE.replace("{list_questions}", wrongQuestionsDescriptions)
        );
    }
}
