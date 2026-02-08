package ru.github.rukonpin.dto.command;

import java.util.List;

public class EditPostCommand {

    private final String title;
    private final String content;
    private final List<Long> tagIds;
    private final String imagePath;

    public EditPostCommand(String title, String content, List<Long> tagIds, String imagePath) {
        this.title = title;
        this.content = content;
        this.tagIds = tagIds;
        this.imagePath = imagePath;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public List<Long> getTagIds() { return tagIds; }
    public String getImagePath() { return imagePath; }
}
