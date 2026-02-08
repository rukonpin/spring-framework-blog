package ru.github.rukonpin.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.github.rukonpin.controller.PostViewController;
import ru.github.rukonpin.exception.common.NotFoundException;
import ru.github.rukonpin.exception.common.ValidationException;

@ControllerAdvice(assignableTypes = {
        PostViewController.class
        })
public class MvcExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(MvcExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NotFoundException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "errors/404";
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidation(ValidationException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "errors/400";
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleDataAccessException(DataAccessException e, Model model) {
        model.addAttribute("message", e.getMessage());
        logger.error("DB error", e);
        return "errors/500";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneric(Exception e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "errors/500";
    }
}
