package ru.github.rukonpin.exception.common;

public abstract class NotFoundException extends BusinessException{
    protected NotFoundException(String message) {
        super(message);
    }
}
