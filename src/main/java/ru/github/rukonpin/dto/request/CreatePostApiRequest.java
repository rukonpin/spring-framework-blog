package ru.github.rukonpin.dto.request;

import java.util.List;

public class CreatePostApiRequest {
    private String title;
    private String content;
    private List<Long> tagIds;
    private String imagePath;

    public CreatePostApiRequest() {}

    public CreatePostApiRequest(String title, String content, List<Long> tagIds, String imagePath) {
        this.title = title;
        this.content = content;
        this.tagIds = tagIds;
        this.imagePath = imagePath;
    }

    public String getTitle() {return title;}
    public String getContent() {return content;}
    public List<Long> getTagIds() {return tagIds;}
    public String getImagePath() {return imagePath;}

    public void setTitle(String title) {this.title = title;}
    public void setContent(String content) {this.content = content;}
    public void setTagIds(List<Long> tagIds) {this.tagIds = tagIds;}
    public void setImagePath(String imagePath) {this.imagePath = imagePath;}
}

