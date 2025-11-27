package com.opencerts.certification.response;

import com.opencerts.certification.Certification;

public record CertificationQuestionCount(
        Certification certification,
        long total
) {

    public DTO toDTO() {
        return new DTO(
                certification.id(),
                certification.provider(),
                certification.displayNameWithoutProvider(),
                total
        );
    }

    public record DTO(
            String certificationId,
            String certificationProvider,
            String certificationDisplayName,
            long total
    ) {
    }
}
