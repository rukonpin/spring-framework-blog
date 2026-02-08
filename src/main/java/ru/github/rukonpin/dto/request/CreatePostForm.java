package ru.github.rukonpin.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class CreatePostForm {

    private String title;
    private String content;
    private List<Long> tagIds;
    private MultipartFile image;

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public List<Long> getTagIds() { return tagIds; }
    public MultipartFile getImage() { return image; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setTagIds(List<Long> tagIds) { this.tagIds = tagIds; }
    public void setImage(MultipartFile image) { this.image = image; }
}
