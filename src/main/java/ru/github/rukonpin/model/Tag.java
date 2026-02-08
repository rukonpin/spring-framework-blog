package ru.github.rukonpin.model;

public class Tag {

    private Long id;
    private String name;
    private String iconSvg;

    public Tag() {}

    public Tag(Long id, String name, String iconSvg) {
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

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                '}';
    }
}
