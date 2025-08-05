package com.aura.syntax.pos.management.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
}
