package com.mmd.library.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "messages")
@Data
public class Message {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_email")
    private String userEmail;

    private String title;

    private String question;

    @Column(name = "admin_email")
    private String adminEmail;

    private String response;

    private boolean closed;

    public Message() {}

    public Message(String userEmail, String title, String question) {
        this.userEmail = userEmail;
        this.title = title;
        this.question = question;
    }
}
