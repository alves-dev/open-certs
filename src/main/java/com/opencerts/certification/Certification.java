package com.opencerts.certification;

import java.util.Arrays;

public enum Certification {
    AWS_CLOUD_PRACTITIONER("aws-certi", "AWS", "Certified Cloud Practitioner"),
    AWS_SOLUTIONS_ARCHITECT("aws-solutions", "AWS", "Solutions Architect – Associate"),
    GOOGLE_CLOUD_LEADER("google-cloud", "Google", "Cloud Digital Leader");

    private String identifier;
    private String provider;
    private String name;

    Certification(String identifier, String provider, String name) {
        this.identifier = identifier;
        this.provider = provider;
        this.name = name;
    }

    public static Certification getById(String identifier) {
        return Arrays.stream(Certification.values())
                .filter(c -> c.identifier.equals(identifier))
                .findFirst().get();
    }

    public String id() {
        return identifier;
    }

    public String displayName() {
        return provider.concat(" - ").concat(name);
    }
}
