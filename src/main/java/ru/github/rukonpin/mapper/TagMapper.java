package ru.github.rukonpin.mapper;

import ru.github.rukonpin.dto.response.TagResponse;
import ru.github.rukonpin.model.Tag;

import java.util.List;

public class TagMapper {

    public static TagResponse toResponse(Tag tag) {
        return new TagResponse(
                tag.getId(),
                tag.getName(),
                tag.getIconSvg()
        );
    }

    public static List<TagResponse> toResponseList(List<Tag> tag) {
        return tag.stream()
                .map(TagMapper::toResponse)
                .toList();
    }

}
