package com.spring.oshaneat.service;

import com.spring.oshaneat.service.imp.FileServiceImp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileService implements FileServiceImp {

    @Value("${fileUpload.rootPath}")
    private String rootPath;

    private Path root;

    public void init() {
        try {
            root = Paths.get(rootPath);
            if(Files.notExists(root)) {
                Files.createDirectories(root);
            }
        } catch (Exception e) {
            System.out.println("Error creating directory " + e.getMessage());
        }
    }

    @Override
    public boolean saveFile(MultipartFile file) {
        try {
            init();
            Files.copy(file.getInputStream(), root.resolve(Objects.requireNonNull(file.getOriginalFilename())), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            System.out.println("Error saving file " + e.getMessage());
            return false;
        }
    }

    @Override
    public Resource loadFile(String filename) {
        try {
            init();
            Path filePath = root.resolve(filename);
            Resource res = new UrlResource(filePath.toUri());
            if (res.exists() && res.isReadable()) {
               return res;
            }
        } catch (Exception e) {
            System.out.println("Error loading file " + e.getMessage());
        }
        return null;
    }
}
