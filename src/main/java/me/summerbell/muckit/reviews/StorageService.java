package me.summerbell.muckit.reviews;

import org.springframework.web.multipart.MultipartFile;

// 모듈화를 위해 interface 만들어 두고 갈아낄 수 있도록 설계.
public interface StorageService {
     String uploadFile(MultipartFile mFile);
    byte[] downloadFile(String fileName);
    String deleteFile(String fileName);

}
