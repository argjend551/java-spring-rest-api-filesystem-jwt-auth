package com.example.FileApp.services;

import com.example.FileApp.data.File;
import com.example.FileApp.exceptions.FileNotFoundException;
import com.example.FileApp.exceptions.NoPermissionFileException;
import com.example.FileApp.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    FileRepository fileRepository;
    UserService userService;

    @Autowired
    public FileService(FileRepository fileRepository,UserService userService){
        this.fileRepository = fileRepository;
        this.userService = userService;
    }


    public File upload(MultipartFile multipartFile, String userid) throws Exception {
        String fileId = UUID.randomUUID().toString();
        var user = userService.findUserbyId(userid);

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        try {
            if(fileName.contains("..")) {
                throw  new Exception("Filename contains invalid path sequence "
                        + fileName);
            }

            var file = new File(
                    fileId,
                    fileName,
                    multipartFile.getContentType(),
                    multipartFile.getBytes(),
                    "http://localhost:8080/api/file/download/" + fileId,
                    user.get());
            return fileRepository.save(file);

        } catch (Exception e) {
            throw new Exception("Could not save File: " + fileName);
        }
    }



    public File getFileById(String id, String userId) throws NoPermissionFileException, FileNotFoundException {
        Optional<File> fileOptional = fileRepository.findById(id);
        if(fileOptional.isEmpty()){
            throw new FileNotFoundException("File not found");
        }else if(!fileOptional.get().getUser().getUserId().equals(userId)){
            throw new NoPermissionFileException("You have no permission to download this file");
        };

        return fileOptional.get();

    }

    public List<File> getFileList(String userId){
        return fileRepository.findAllById(userId);
    }


    public void deleteFile(String fileId,String userId) throws FileNotFoundException, NoPermissionFileException {
        Optional<File> fileOptional = fileRepository.findById(fileId);
        if(fileOptional.isEmpty()) {
            throw new FileNotFoundException("File not found");
        }else if(!fileOptional.get().getUser().getUserId().equals(userId)){
            throw new NoPermissionFileException("You have no permission to this file");
        };

        fileRepository.deleteById(fileId);
    }

}
