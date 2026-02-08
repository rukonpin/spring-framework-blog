package ru.github.rukonpin.dto.response;

public class CommentResponse {
    private Long id;
    private String name;
    private String content;
    private String imagePath;
    private String createdAt;

    public CommentResponse(Long id, String name, String content, String imagePath, String createdAt) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
    }

    public Long getId() {return id;}
    public String getName() {return name;}
    public String getContent() {return content;}
    public String getImagePath() {return imagePath;}
    public String getCreatedAt() {return createdAt;}

    public void setId(Long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setContent(String content) {this.content = content;}
    public void setImagePath(String imagePath) {this.imagePath = imagePath;}
    public void setCreatedAt(String createdAt) {this.createdAt = createdAt;}
}