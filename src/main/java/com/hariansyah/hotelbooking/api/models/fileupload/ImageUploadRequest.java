package com.hariansyah.hotelbooking.api.models.fileupload;

import org.springframework.web.multipart.MultipartFile;

public class ImageUploadRequest {

    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "ImageUploadRequest{" +
                "file=" + file +
                '}';
    }
}
