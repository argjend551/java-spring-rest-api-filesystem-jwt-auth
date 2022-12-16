package com.example.FileApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class FileNotFoundException extends Exception {
    public FileNotFoundException(String message) {
        super(message);
    }
}
