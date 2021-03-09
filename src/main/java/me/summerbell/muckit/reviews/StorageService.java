package me.summerbell.muckit.reviews;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
     String uploadFile(MultipartFile mFile);
    byte[] downloadFile(String fileName);
    String deleteFile(String fileName);

}
