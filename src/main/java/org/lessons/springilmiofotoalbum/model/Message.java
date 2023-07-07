package org.lessons.springilmiofotoalbum.model;

import jakarta.persistence.*;

@Entity
@Table(name = "messages")
public class Message {
    //ATTRIBUTI ------------------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String email;

    @Lob
    @Column(nullable = false)
    private String message;

    //GETTER E SETTER ------------------------------------------------------------------------------
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
