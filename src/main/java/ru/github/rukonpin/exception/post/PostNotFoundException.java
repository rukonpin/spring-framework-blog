package ru.github.rukonpin.exception.post;

import ru.github.rukonpin.exception.common.NotFoundException;

public class PostNotFoundException extends NotFoundException {
    public PostNotFoundException(Long id) {
        super("Пост с id=" + id + " не найден");
    }
}
