package org.lessons.springilmiofotoalbum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    //ATTRIBUTI ------------------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name must not be null or blank")
    @Size(min=3, max=255, message = "Name must have min 3 and max 255 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max=500, message = "Description must have max 500 characters")
    @Lob
    private String description;

    @JsonIgnore //per evitare ricorsioni
    @ManyToMany(mappedBy = "categories")
    private List<Photo> photos = new ArrayList<>();

    //GETTER E SETTER ------------------------------------------------------------------------------
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<Photo> getPhotos() {
        return photos;
    }
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
