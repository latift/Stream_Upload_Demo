package com.example;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.utils.IoUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Singleton
public class S3Adapter {
    @Inject
    S3SBean amazonS3Client;

    @Value("${s3.bucket.name}")
    String defaultBucketName;

    @Value("${s3.default.folder}")
    String defaultBaseFolder;

    public List<Bucket> getAllBuckets() {
        return amazonS3Client.getS3Client().listBuckets().buckets();
    }


    public void uploadFile(File uploadFile) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(defaultBucketName)
                .key(defaultBaseFolder + "/" + uploadFile.getName()).build();
        amazonS3Client.getS3Client().putObject(request, uploadFile.toPath());
    }


    public void uploadFile(String fileName, byte[] fileBytes) {
        File file = new File("../" + fileName);
        file.canWrite();
        file.canRead();
        FileOutputStream iofs = null;
        try {
            iofs = new FileOutputStream(file);
            iofs.write(fileBytes);
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(defaultBucketName)
                    .key(defaultBaseFolder + "/" + file.getName()).build();
            amazonS3Client.getS3Client().putObject(request, file.toPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public byte[] getFile(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(defaultBucketName).key(defaultBaseFolder + "/" + key).build();
        ResponseInputStream<GetObjectResponse> obj = amazonS3Client.getS3Client().getObject(getObjectRequest);
        try {
            byte[] content = IoUtils.toByteArray(obj);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
