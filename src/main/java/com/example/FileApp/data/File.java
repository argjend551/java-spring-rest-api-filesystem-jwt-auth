package com.example.FileApp.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Files")
@Getter
@Setter
@NoArgsConstructor
public class File {
    @Id
    private String file_id;

    private String fileName;
    private String fileType;

    @Lob
    private byte[] data;

    private String download_url;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public File(String file_id, String fileName, String fileType, byte[] data, String download_url, User user) {
        this.file_id = file_id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.download_url = download_url;
        this.user = user;
    }
}
