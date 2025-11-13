package com.opencerts.test;

public record TestSessionDTO(
        String identifier,
        int totalCorrect,
        int totalError
) {

    public TestSessionDTO(TestSession test) {
        this(test.identifier(), test.totalCorrect(), test.totalError());
    }
}
