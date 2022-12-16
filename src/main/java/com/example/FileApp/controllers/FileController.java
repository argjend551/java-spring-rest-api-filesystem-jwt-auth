package com.example.FileApp.controllers;

import com.example.FileApp.dtos.FileDto;
import com.example.FileApp.exceptions.NoFilesException;
import com.example.FileApp.services.FileService;
import com.example.FileApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/file")
public class FileController {
 private final FileService fileService;
 private final UserService userService;


 @Autowired
 public FileController(FileService fileService, UserService userService) {
  this.fileService = fileService;
  this.userService = userService;
 }

 
 @PostMapping("/upload")
 public ResponseEntity<String> upload(@RequestParam(value = "file", required = false) MultipartFile multipartFile,Principal principal) throws Exception {
  var userId = userService.getUserIdByUserName(principal.getName());
  if( multipartFile == null || multipartFile.isEmpty()){
   return ResponseEntity.status(400).body("Please attach a file you want to upload.");
  }
 var file = fileService.upload(multipartFile, userId);
  return ResponseEntity.ok().body("Uploaded the file successfully: " + multipartFile.getOriginalFilename() + "\nDownload Link: " + file.getDownload_url());
 }


 @GetMapping("/download/{fileId}")
 public ResponseEntity<ByteArrayResource> download(@PathVariable String fileId,Principal principal) throws Exception {
  var file = fileService.getFileById(fileId,userService.getUserIdByUserName(principal.getName()));
  return  ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(file.getFileType()))
          .header(HttpHeaders.CONTENT_DISPOSITION,
                  "attachment; filename=\"" + file.getFileName()
                          + "\"")
          .body(new ByteArrayResource(file.getData()));
 }


 @DeleteMapping("/delete/{fileId}")
 public ResponseEntity<String> delete(@PathVariable String fileId, Principal principal) throws Exception {
  fileService.deleteFile(fileId,userService.getUserIdByUserName(principal.getName()));
  return ResponseEntity.ok().body("Deleted the file successfully");
 }


 
@GetMapping("/getAll")
 public ResponseEntity<List<FileDto>> getAllFiles(Principal principal) throws NoFilesException {
 var userId = userService.getUserIdByUserName(principal.getName());
  var files = fileService.getFileList(userId);
  if(files.isEmpty()){
   throw new NoFilesException("You have not uploaded any files.");
  }
  return ResponseEntity.status(200).body(files.stream()
          .map(file -> new FileDto(
                 file.getFileName(),
                 file.getDownload_url()
         ))
          .collect(Collectors.toList()));
 }

}

