package com.opencerts.test;

import com.opencerts.certification.response.CertificationDTO;

import java.time.LocalDateTime;

record ListTestSessionDTO(
        String identifier,
        CertificationDTO certification,
        int totalAnswers,
        int percentageCorrect,
        LocalDateTime startedAt,
        long timeSpentSeconds,
        boolean finished
) {

    ListTestSessionDTO(TestSession test) {
        this(test.identifier(),
                new CertificationDTO(test.certification()),
                test.totalCorrect() + test.totalError(),
                test.percentageCorrect(),
                test.startedAt(),
                test.timeSpentSeconds(),
                test.isFinished()
        );
    }
}
