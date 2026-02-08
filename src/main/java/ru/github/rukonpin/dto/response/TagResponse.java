package ru.github.rukonpin.dto.response;

public class TagResponse {
    private Long id;
    private String name;
    private String iconSvg;

    public TagResponse(Long id, String name, String iconSvg) {
        this.id = id;
        this.name = name;
        this.iconSvg = iconSvg;
    }

    public Long getId() {return id;}
    public String getName() {return name;}
    public String getIconSvg() {return iconSvg;}

    public void setId(Long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setIconSvg(String iconSvg) {this.iconSvg = iconSvg;}
}
