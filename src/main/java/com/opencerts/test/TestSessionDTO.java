package com.opencerts.test;

public record TestSessionDTO(
        String identifier,
        int totalCorrect,
        int totalError,
        int percentageCorrect
) {

    public TestSessionDTO(TestSession test) {
        int total = test.totalCorrect() + test.totalError();
        int percentage = total == 0 ? 0 : (int) Math.round(test.totalCorrect() * 100.0 / total);
        this(test.identifier(), test.totalCorrect(), test.totalError(), percentage);
    }
}
