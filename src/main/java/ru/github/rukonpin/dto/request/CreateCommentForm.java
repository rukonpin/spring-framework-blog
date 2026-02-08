package ru.github.rukonpin.dto.request;

public class CreateCommentForm {
    private String content;

    public CreateCommentForm() {}

    public CreateCommentForm(String content) {
        this.content = content;
    }

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}
}
