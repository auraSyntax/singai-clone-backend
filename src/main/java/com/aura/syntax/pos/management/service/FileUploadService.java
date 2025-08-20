package com.aura.syntax.pos.management.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final Cloudinary cloudinary;

    public Map upload(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "use_filename", true,
                        "public_id", "custom_" + System.currentTimeMillis()
                )
        );
    }

    public String deleteFile(String imageUrl) {
        try {
            String publicId = extractPublicId(imageUrl);
            if (publicId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image URL");
            }

            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            if (!"ok".equals(result.get("result"))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to delete image: " + result.get("result"));
            }

            return "Image deleted successfully";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private String extractPublicId(String url) {
        try {
            String[] parts = url.split("/");
            String fileNameWithExt = parts[parts.length - 1];
            return fileNameWithExt.substring(0, fileNameWithExt.lastIndexOf('.'));
        } catch (Exception e) {
            return null;
        }
    }
}
