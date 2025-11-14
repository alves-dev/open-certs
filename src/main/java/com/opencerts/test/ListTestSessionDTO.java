package com.opencerts.test;

import java.time.LocalDateTime;

public record ListTestSessionDTO(
        String identifier,
        String certification,
        int totalCorrect,
        int totalError,
        int percentageCorrect,
        LocalDateTime startedAt
) {

    public ListTestSessionDTO(TestSession test) {
        this(test.identifier(), test.certificationId(), test.totalCorrect(), test.totalError(), test.percentageCorrect(), test.startedAt());
    }
}
