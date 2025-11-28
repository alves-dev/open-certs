package com.opencerts.challenge;

public record ChallengeDTO(
        String id,
        String name,
        String certificationName,
        int totalQuestions,
        int answeredQuestions,
        int participantCount,
        String status
) {
}
