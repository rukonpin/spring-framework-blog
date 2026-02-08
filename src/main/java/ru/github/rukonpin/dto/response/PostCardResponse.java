package ru.github.rukonpin.dto.response;

import ru.github.rukonpin.model.Tag;

import java.util.List;

public class PostCardResponse {
    private Long id;
    private String title;
    private String preview;
    private List<Tag> tags;
    private String imagePath;
    private Integer likesCount;
    private Integer commentsCount;

    public PostCardResponse() {}

    public PostCardResponse(Long id, String title, String preview, List<Tag> tags, String imagePath, Integer likesCount, Integer commentsCount) {
        this.id = id;
        this.title = title;
        this.preview = preview;
        this.tags = tags;
        this.imagePath = imagePath;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
    }

    public Long getId() {return id;}
    public String getTitle() {return title;}
    public String getPreview() {return preview;}
    public List<Tag> getTags() {return tags;}
    public String getImagePath() {return imagePath;}
    public Integer getLikesCount() {return likesCount;}
    public Integer getCommentsCount() {return commentsCount;}

    public void setId(Long id) {this.id = id;}
    public void setTitle(String title) {this.title = title;}
    public void setPreview(String preview) {this.preview = preview;}
    public void setTags(List<Tag> tags) {this.tags = tags;}
    public void setImagePath(String imagePath) {this.imagePath = imagePath;}
    public void setLikesCount(Integer likesCount) {this.likesCount = likesCount;}
    public void setCommentsCount(Integer commentsCount) {this.commentsCount = commentsCount;}
}
