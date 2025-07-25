package com.aura.syntax.pos.management.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.base-view-url}")
    private String baseViewUrl;

    @Value("${cloudinary.base-download-url}")
    private String baseDownloadUrl;

    public List<Map<String, String>> uploadFiles(List<MultipartFile> files) throws IOException {
        List<Map<String, String>> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto",
                    "use_filename", true,
                    "unique_filename", false,
                    "public_id", "file_" + System.currentTimeMillis()
            ));

            String publicId = (String) uploadResult.get("public_id");
            String format = (String) uploadResult.get("format");
            String version = uploadResult.get("version").toString();

            String savedPath = "v" + version + "/" + publicId + "." + format;

            Map<String, String> fileMap = new HashMap<>();
            fileMap.put("public_id", publicId);
            fileMap.put("file_type", (String) uploadResult.get("resource_type"));
            fileMap.put("cloudinary_path", savedPath);
            fileMap.put("view_url", baseViewUrl + savedPath);
            fileMap.put("download_url", baseDownloadUrl + savedPath);

            uploadedFiles.add(fileMap);
        }

        return uploadedFiles;
    }
}
