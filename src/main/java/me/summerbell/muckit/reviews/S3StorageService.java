package me.summerbell.muckit.reviews;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3StorageService implements StorageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public String uploadFile(MultipartFile mFile){
        File file = convertMultiPartFileToFile(mFile);
        String fileName = System.currentTimeMillis()+"_"+mFile.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        URL fileUrl = s3Client.getUrl(bucketName, fileName);

        file.delete();
        return fileUrl.toString();
    }

    public byte[] downloadFile(String fileName){
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try{
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e){
            e.printStackTrace();;
        }
        return null;
    }

    public String deleteFile(String fileName){
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " removed";
    }


    /**
     * 멀티타입파일을 파일로 변환
     * @param mFile 멀티파트타입 파일
     * @return File 타입 파일
     */
    private File convertMultiPartFileToFile(MultipartFile mFile){
        File convertedFile = new File(mFile.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(mFile.getBytes());
        } catch (IOException e){
            log.error("Error Converting multipartfile to mFile", e);
        }
        return convertedFile;
    }


}
