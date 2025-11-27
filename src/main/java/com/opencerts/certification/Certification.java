package com.opencerts.certification;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "certifications")
public class Certification {

    @Id
    private String id;
    private String provider;
    private String name;
    private String level;

    Certification() {
    }

    public Certification(String provider, String name, String level) {
        this.id = provider
                .concat("-").concat(name)
                .concat("-").concat(level)
                .toLowerCase().replace(" ", "-");
        this.provider = provider;
        this.name = name;
        this.level = level;
    }

    // Getters //
    public String id() {
        return id;
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

    public String displayNameWithoutProvider() {
        return name.concat(" - ").concat(level);
    }

    // Modificadores //


    // Outros //

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Certification that = (Certification) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
