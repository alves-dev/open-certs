package com.opencerts.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String name;
    private String profileImage;
    private String email;
    private String providerId;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public static User create(String name, String profileImage, String email, String providerId) {
        return new User(name, profileImage, email, providerId);
    }

    private User(String name, String profileImage, String email, String providerId) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.profileImage = profileImage;
        this.email = email;
        this.providerId = providerId;
        this.createdAt = LocalDateTime.now();
        this.lastLogin = LocalDateTime.now();
    }

    // Getters //

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String profileImage() {
        return profileImage;
    }

    public String email() {
        return email;
    }


    // Modificadores //

    public void loginDone() {
        this.lastLogin = LocalDateTime.now();
    }


    // Outros //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
