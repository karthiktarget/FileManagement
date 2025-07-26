package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class UploadedFile {

    @Id
    private UUID id;

    private String fileName;
    private String filePath;
    private String contentType;
    private long size;
    private LocalDateTime uploadTime;

    public UploadedFile() {
        this.id = UUID.randomUUID();
        this.uploadTime = LocalDateTime.now();
    }
}

