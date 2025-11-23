package com.opencerts.test;

import java.time.LocalDateTime;

public record ListTestSessionDTO(
        String identifier,
        String certificationId,
        String certificationName,
        int totalCorrect,
        int totalError,
        int percentageCorrect,
        LocalDateTime startedAt
) {

    public ListTestSessionDTO(TestSession test) {
        this(test.identifier(),
                test.certificationId(),
                test.certificationId().replace("-", " ").toUpperCase(), //TODO: colocar o nome da certificação
                test.totalCorrect(),
                test.totalError(),
                test.percentageCorrect(),
                test.startedAt()
        );
    }
}
