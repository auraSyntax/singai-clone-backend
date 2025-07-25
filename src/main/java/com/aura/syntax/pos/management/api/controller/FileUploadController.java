package com.aura.syntax.pos.management.api.controller;

import com.aura.syntax.pos.management.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/file-upload")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"})
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<List<Map<String, String>>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<Map<String, String>> result = fileUploadService.uploadFiles(files);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(Map.of("error", e.getMessage())));
        }
    }
}
