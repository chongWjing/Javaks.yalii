package com.lostfound.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final int MAX_FILES = 6;
    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;

    private final Path uploadRoot;

    public FileStorageService(@Value("${app.upload-dir}") String uploadDir) {
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public List<String> storeImages(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new RuntimeException("请选择要上传的图片");
        }
        if (files.size() > MAX_FILES) {
            throw new RuntimeException("最多上传 " + MAX_FILES + " 张图片");
        }

        List<String> urls = new ArrayList<>();
        LocalDate today = LocalDate.now();
        Path targetDir = uploadRoot.resolve(String.valueOf(today.getYear()))
                .resolve(String.format("%02d", today.getMonthValue()))
                .resolve(String.format("%02d", today.getDayOfMonth()));

        try {
            Files.createDirectories(targetDir);
        } catch (IOException e) {
            throw new RuntimeException("创建上传目录失败: " + e.getMessage());
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new RuntimeException("图片不能为空");
            }
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new RuntimeException("单张图片不能超过 5MB");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("仅支持图片格式");
            }

            String extension = resolveExtension(contentType, file.getOriginalFilename());
            String filename = UUID.randomUUID().toString().replace("-", "") + extension;
            Path target = targetDir.resolve(filename);

            try {
                Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("保存图片失败: " + e.getMessage());
            }

            String relativePath = uploadRoot.relativize(target).toString().replace("\\", "/");
            urls.add("/uploads/" + relativePath);
        }

        return urls;
    }

    private String resolveExtension(String contentType, String originalName) {
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf('.'));
        }

        if (!extension.isBlank() && extension.length() > 1) {
            return extension.toLowerCase();
        }

        return switch (contentType) {
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> ".img";
        };
    }
}
