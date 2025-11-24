package com.opencerts.certification.response;

import com.opencerts.certification.Certification;

import java.util.Optional;

public record CertificationDTO(
        String id,
        String provider,
        String name,
        String level
) {

    public String displayName() {
        return provider.concat(" - ").concat(name).concat(" | ").concat(level);
    }

    public CertificationDTO(Certification certification) {
        this(
                certification.id(),
                certification.provider(),
                certification.name(),
                certification.level()
        );
    }

    public static CertificationDTO of(Optional<Certification> optCertification) {
        return optCertification.map(CertificationDTO::new).orElseGet(CertificationDTO::new);

    }

    private CertificationDTO() {
        this(null, null, null, null);
    }
}