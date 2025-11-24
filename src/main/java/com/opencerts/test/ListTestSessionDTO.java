package com.opencerts.test;

import com.opencerts.certification.CertificationService;
import com.opencerts.certification.response.CertificationDTO;

import java.time.LocalDateTime;

public record ListTestSessionDTO(
        String identifier,
        CertificationDTO certification,
        int totalCorrect,
        int totalError,
        int percentageCorrect,
        LocalDateTime startedAt
) {

    public ListTestSessionDTO(TestSession test, CertificationService certificationService) {
        this(test.identifier(),
                CertificationDTO.of(certificationService.getById(test.certificationId())),
                test.totalCorrect(),
                test.totalError(),
                test.percentageCorrect(),
                test.startedAt()
        );
    }
}
