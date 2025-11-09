package com.opencerts.certification;

import java.util.Arrays;
import java.util.UUID;

public enum Certification {
    AWS_CLOUD_PRACTITIONER(UUID.fromString("061b23d7-8311-4608-82b0-e6f5ff49938e"),
            "AWS", "AWS Certified Cloud Practitioner"),
    AWS_SOLUTIONS_ARCHITECT(UUID.fromString("9ec6487a-1b33-431c-b962-1aae60a37cd7"),
            "AWS", "AWS Solutions Architect – Associate"),
    GOOGLE_CLOUD_LEADER(UUID.fromString("0fab4c57-3d4a-4ac5-868c-3e3253a8b878"),
            "Google", "Google Cloud Digital Leader");

    private UUID id;
    private String provider;
    private String name;

    Certification(UUID id, String provider, String name) {
        this.id = id;
        this.provider = provider;
        this.name = name;
    }

    public static Certification getById(UUID certificationId) {
        return Arrays.stream(Certification.values())
                .filter(c -> c.id.equals(certificationId))
                .findFirst().get();
    }

    public UUID id() {
        return id;
    }

    public String displayName() {
        return provider.concat(" - ").concat(name);
    }
}
