package com.example.FileApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public class NoFilesException extends Exception {
        public NoFilesException(String message) {
            super(message);
        }
    }

