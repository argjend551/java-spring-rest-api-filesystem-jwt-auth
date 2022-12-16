package com.example.FileApp.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


    @AllArgsConstructor
    @Getter
    @Setter
    public class FileDto {
        private String fileName;
        private String download_url;
    }

