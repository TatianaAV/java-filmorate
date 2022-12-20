package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({InvalidEmailException.class, IncorrectParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(HttpServletRequest request, Exception ex) {
        log.error("Requested URL=" + request.getRequestURL());
        log.error("Exception Raised=" + ex);
        return getErrorMessage(ex);
    }

    @ExceptionHandler({FilmNotFoundException.class, UserNotFoundException.class, ObjectNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)//404
    public ErrorResponse handleFilmNotFoundException(HttpServletRequest request, Exception ex) {
        log.error("Requested URL=" + request.getRequestURL());
        log.error("404 NOT FOUND {}", ex.getMessage());
        return getErrorMessage(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleFilmNotFoundException(HttpServletRequest request, UserAlreadyExistException ex) {
        log.error("Requested URL=" + request.getRequestURL());
        log.error("404 CONFLICT {}", ex.getMessage());
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class, Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse databaseError(HttpServletRequest request, Exception ex) {
        log.error("Requested URL=" + request.getRequestURL());
        log.error("500 CONFLICT {}", ex.getMessage());
        return new ErrorResponse("Проверьте данные запроса" + ex.getMessage());
    }

    private static ErrorResponse getErrorMessage(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }
}

