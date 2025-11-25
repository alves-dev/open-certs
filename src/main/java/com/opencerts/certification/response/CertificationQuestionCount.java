package com.opencerts.certification.response;

public record CertificationQuestionCount(
        String certificationId,
        String certificationProvider,
        String certificationDisplayName,
        long total
) {
}
