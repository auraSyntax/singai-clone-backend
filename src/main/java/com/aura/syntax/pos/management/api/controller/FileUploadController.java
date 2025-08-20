package com.aura.syntax.pos.management.api.controller;

import com.aura.syntax.pos.management.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/file-upload")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"})
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.getContentType().matches("image/(jpeg|png|jpg|gif)")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file type");
            }

            Map uploadResult = fileUploadService.upload(file);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of(
                            "url", uploadResult.get("secure_url"),
                            "public_id", uploadResult.get("public_id")
                    )
            ));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image");
        }
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteFile(@RequestParam(value = "url") String url) {
        String message = fileUploadService.deleteFile(url);
        return ResponseEntity.ok(Collections.singletonMap("message", message));
    }
}
