package org.example.repository;


import org.example.model.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileRepository extends JpaRepository<UploadedFile, UUID> {
}

