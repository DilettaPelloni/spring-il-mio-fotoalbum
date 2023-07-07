package org.lessons.springilmiofotoalbum.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    //ATTRIBUTI ------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    //GETTER & SETTER ------------------------------------------------------------------
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}