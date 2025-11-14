package com.opencerts.certification;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "certification")
public class Certification {

    @Id
    private String identifier;
    private String provider;
    private String name;
    private String level;

    Certification() {
    }

    public Certification(String provider, String name, String level) {
        this.identifier = provider
                .concat("-").concat(name)
                .concat("-").concat(level)
                .toLowerCase().replace(" ", "-");
        this.provider = provider;
        this.name = name;
        this.level = level;
    }

    // Getters //
    public String id() {
        return identifier;
    }

    public String identifier() {
        return identifier;
    }

    public String provider() {
        return provider;
    }

    public String name() {
        return name;
    }

    public String level() {
        return level;
    }

    public String displayName() {
        return provider.concat(" - ").concat(name).concat(" | ").concat(level);
    }

    // Modificadores //


    // Outros //

}
