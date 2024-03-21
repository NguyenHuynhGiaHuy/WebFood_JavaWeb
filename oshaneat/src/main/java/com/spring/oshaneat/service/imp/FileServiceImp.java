package com.spring.oshaneat.service.imp;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface FileServiceImp {
    public boolean saveFile(MultipartFile file);
    public Resource loadFile(String filename);
}
