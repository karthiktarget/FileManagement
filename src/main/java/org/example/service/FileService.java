package org.example.service;

import org.example.exception.FileNotFoundException;
import org.example.model.UploadedFile;
import org.example.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    private final Path rootPath;
    private final FileRepository fileRepository;

    public FileService(@Value("${file.storage.location}") String location, FileRepository fileRepository) {
        this.rootPath = Paths.get(location).toAbsolutePath().normalize();
        this.fileRepository = fileRepository;
        try {
            Files.createDirectories(this.rootPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create storage directory", e);
        }
    }

    public UploadedFile storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        UUID fileId = UUID.randomUUID();
        String storedFileName = fileId + "_" + fileName;
        Path targetLocation = rootPath.resolve(storedFileName);

        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            UploadedFile metadata = new UploadedFile();
            metadata.setId(fileId);
            metadata.setFileName(fileName);
            metadata.setFilePath(targetLocation.toString());
            metadata.setContentType(file.getContentType());
            metadata.setSize(file.getSize());

            return fileRepository.save(metadata);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + fileName, e);
        }
    }

    public List<UploadedFile> getAllFiles() {
        return fileRepository.findAll();
    }

    public ResponseEntity<Resource> downloadFile(UUID id) {
        UploadedFile file = fileRepository.findById(id).orElseThrow(() ->
                new FileNotFoundException("File not found with id " + id));

        Resource resource = new FileSystemResource(file.getFilePath());

        if (!resource.exists()) {
            throw new FileNotFoundException("File not found on disk");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(resource);
    }

    public void deleteFile(UUID id) {
        UploadedFile file = fileRepository.findById(id).orElseThrow(() ->
                new FileNotFoundException("File not found with id " + id));

        try {
            Files.deleteIfExists(Paths.get(file.getFilePath()));
            fileRepository.deleteById(id);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file", e);
        }
    }
}
