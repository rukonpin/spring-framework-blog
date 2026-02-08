package ru.github.rukonpin.model;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private String name;
    private String content;
    private String imagePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Comment() {}

    public Comment(Long id, String name, String content, String imagePath, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {return id;}
    public String getName() {return name;}
    public String getContent() {return content;}
    public String getImagePath() {return imagePath;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public LocalDateTime getUpdatedAt() {return updatedAt;}

    public void setId(Long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setContent(String content) {this.content = content;}
    public void setImagePath(String imagePath) {this.imagePath = imagePath;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}
}


