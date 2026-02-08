package ru.github.rukonpin.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private Long id;
    private String title;
    private String preview;
    private String content;
    private List<Tag> tags = new ArrayList<>();;
    private String imagePath;
    private Integer likesCount;
    private Integer commentsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Post() {}

    public Post(Long id, String title, String preview, String content, List<Tag> tags, String imagePath,
                Integer likesCount, Integer commentsCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.preview = preview;
        this.content = content;
        this.tags = tags;
        this.imagePath = imagePath;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setPreview(String preview) { this.preview = preview; }
    public void setContent(String content) { this.content = content; }
    public void setTags(List<Tag> tags) { this.tags = tags; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public void setLikesCount(Integer likesCount) { this.likesCount = likesCount; }
    public void setCommentsCount(Integer commentsCount) { this.commentsCount = commentsCount; }
    public void setCreateAt(LocalDateTime createAt) { this.createdAt = createAt; }
    public void setUpdateAt(LocalDateTime updateAt) { this.updatedAt = updateAt; }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getPreview() { return preview; }
    public String getContent() { return content; }
    public List<Tag> getTags() { return tags; }
    public String getImagePath() { return imagePath; }
    public Integer getLikesCount() { return likesCount; }
    public Integer getCommentsCount() { return commentsCount; }
    public LocalDateTime getCreateAt() { return createdAt; }
    public LocalDateTime getUpdateAt() { return updatedAt; }
}
