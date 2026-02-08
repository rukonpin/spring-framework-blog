package ru.github.rukonpin.exception.common;

public abstract class BusinessException extends RuntimeException{
    protected BusinessException(String message) {
        super(message);
    }
}
