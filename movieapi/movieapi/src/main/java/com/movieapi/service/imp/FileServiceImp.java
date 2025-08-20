package com.movieapi.service.imp;

import com.movieapi.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImp implements FileService {
    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {

        // get name of the file
        String fileName = file.getOriginalFilename();

        // get the file path
        String filePath = path + File.separator + fileName;
        System.out.println(filePath);

        // create file object
        File f = new File(path);
        System.out.println(f.toString());
        if(!f.exists()) {
            f.mkdir();
        }
        // copy the file or update the file
        Files.copy(file.getInputStream(),Paths.get(filePath));

        return fileName;
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
        String filePath = path + File.separator + fileName;
        return new FileInputStream(filePath);
    }
}
