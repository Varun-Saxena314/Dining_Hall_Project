package com.mealmind.backend.entity;

import jakarta.persistence.*; //java persistence. Allows for Id, Column, Entity, and more
import java.time.Instant; //Gets current instant of time to track creation of entities

@Entity //class is database entity
@Table(name = "users") //entity goes into table 'users'
public class User { //user class

    @Id //primart key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-increment ID
    private Long id; //store in variable

    @Column(nullable = false, unique = true, length = 64) //username column cannot be null, must be unique, and have a max length of 64
    private String username;

    @Column(nullable = false, length = 255) //password cannot be null, and length is a max of 255 for a hash password (60 chars around)
    private String passwordHash;

    @Column(nullable = false) //instant of creation cannot be false
    private Instant createdAt = Instant.now(); //variable is the current instant

    protected User() {} //Hibernate and JPA need a no args constructor to instantiate the entity when loading from the DB. Wont actually use

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash; //make user object
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; } //getter methods for ID, username, password, and time of creation of user
    public Instant getCreatedAt() { return createdAt; }

    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; } //setters for only two mutable features
}
