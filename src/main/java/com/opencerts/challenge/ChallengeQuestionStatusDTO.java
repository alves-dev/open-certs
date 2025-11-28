package com.opencerts.challenge;

public record ChallengeQuestionStatusDTO(
        String id,
        String name,
        int totalQuestions,
        int currentQuestionNumber,
        int correctAnswers,
        int wrongAnswers,
        int answeredCount, // Total de questões tentadas (Corretas + Incorretas)
        int percentageAnswered // (answeredCount / totalQuestions) * 100
) {
}
