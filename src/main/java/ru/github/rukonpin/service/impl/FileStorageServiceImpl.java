package ru.github.rukonpin.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.github.rukonpin.service.FileStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);
    private final Path uploadDir;

    public FileStorageServiceImpl(@Value("${app.upload-dir}") String uploadDirProperty) {
        this.uploadDir = Paths.get(uploadDirProperty)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create upload directory " + uploadDir, e);
        }
    }

    @Override
    public String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String filename = UUID.randomUUID() + "-" +
                Paths.get(file.getOriginalFilename()).getFileName().toString();

        Path target = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/images/" + filename;
    }
}
