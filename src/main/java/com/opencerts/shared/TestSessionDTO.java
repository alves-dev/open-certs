package com.opencerts.shared;

import com.opencerts.test.TestSession;

public record TestSessionDTO(
        String identifier,
        int totalCorrect,
        int totalError,
        int percentageCorrect
) {

    public TestSessionDTO(TestSession test) {
        this(test.identifier(), test.totalCorrect(), test.totalError(), test.percentageCorrect());
    }
}
