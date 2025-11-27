package com.opencerts.certification.response;

import com.opencerts.certification.Certification;

public record CertificationDTO(
        String id,
        String provider,
        String name,
        String level
) {

    public CertificationDTO(Certification certification) {
        this(
                certification.id(),
                certification.provider(),
                certification.name(),
                certification.level()
        );
    }
}