package com.opencerts.challenge;

import com.opencerts.certification.response.CertificationDTO;

public record ChallengeResultDTO(
        String id,
        String name,
        CertificationDTO certification,
        int percentageCorrect,
        int totalCorrect,
        int totalError,
        String wrongQuestionsPrompt
) {
}
