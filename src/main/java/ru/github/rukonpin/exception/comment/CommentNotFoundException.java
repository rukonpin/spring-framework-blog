package ru.github.rukonpin.exception.comment;

import ru.github.rukonpin.exception.common.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException(Long id) {
        super("Комментарий с id=" + id + " не найден");
    }
}
