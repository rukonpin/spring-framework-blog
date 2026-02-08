package ru.github.rukonpin.dto.response;

import ru.github.rukonpin.model.Tag;

import java.util.List;

public class PostDetailsResponse {
    private Long id;
    private String title;
    private String content;
    private List<Tag> tags;
    private String imagePath;
    private Integer likesCount;

    public PostDetailsResponse(Long id, String title, String content, List<Tag> tags, String imagePath,
                               Integer likesCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.imagePath = imagePath;
        this.likesCount = likesCount;
    }

    public Long getId() {return id;}
    public String getTitle() {return title;}
    public String getContent() {return content;}
    public List<Tag> getTags() {return tags;}
    public String getImagePath() {return imagePath;}
    public Integer getLikesCount() {return likesCount;}

    public void setId(Long id) {this.id = id;}
    public void setTitle(String title) {this.title = title;}
    public void setContent(String content) {this.content = content;}
    public void setTags(List<Tag> tags) {this.tags = tags;}
    public void setImagePath(String imagePath) {this.imagePath = imagePath;}
    public void setLikesCount(Integer likesCount) {this.likesCount = likesCount;}
}
