package com.opencerts.util;

import com.opencerts.certification.Question;

import java.util.List;

public class PromptUtil {

    private PromptUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final String PROMPT_BASE = """
            <system_prompt>
            Analise esta lista de perguntas de certificação as quais respondi errado.
            Com base nela, identifique os principais temas/serviços/topicos em que estou tendo mais dificuldade.
            Em seguida, gere uma tabela mostrando os tópicos mais problemáticos, ordenados do mais crítico para o menos crítico.
            </system_prompt>
            
            <questions>
            Aqui estão as perguntas respondidas incorretamente:
            
            {list_questions}
            </questions>
            """;

    public static String generateBy(List<Question> incorrectQuestions) {
        StringBuilder wrongQuestionsDescriptions = new StringBuilder();
        for (int i = 0; i < incorrectQuestions.size(); i++) {
            wrongQuestionsDescriptions
                    .append(i + 1).append(". ")
                    .append(incorrectQuestions.get(i).description())
                    .append("\n\n");
        }

        return PROMPT_BASE.replace("{list_questions}", wrongQuestionsDescriptions.toString().trim());
    }
}
