package com.opencerts.test;

import com.opencerts.certification.response.CertificationDTO;

import java.time.LocalDateTime;

record ListTestSessionDTO(
        String identifier,
        CertificationDTO certification,
        int totalCorrect,
        int totalError,
        int percentageCorrect,
        LocalDateTime startedAt
) {

    ListTestSessionDTO(TestSession test) {
        this(test.identifier(),
                new CertificationDTO(test.certification()),
                test.totalCorrect(),
                test.totalError(),
                test.percentageCorrect(),
                test.startedAt()
        );
    }
}
