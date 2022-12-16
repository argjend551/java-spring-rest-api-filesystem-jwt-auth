package com.example.FileApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class NoPermissionFileException extends Exception{

    public NoPermissionFileException(String message) {
        super(message);
    }
}
