package com.movieapi.controller;

import com.movieapi.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api")
public class FileController {

    private final FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

    @Value("${project.poster}")
    private String path;

    @PostMapping("/update")
    public ResponseEntity<String> updateFile(@RequestPart("file") MultipartFile file) throws IOException {
        String uploadedFile = service.uploadFile(path, file);
        return ResponseEntity.ok("File Uploaded :" + uploadedFile);
    }

    @GetMapping("/{fileName}")
    public void getFile(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        InputStream resourceFile = service.getResourceFile(path, fileName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resourceFile,response.getOutputStream());
    }
}
