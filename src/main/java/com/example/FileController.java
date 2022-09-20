package com.example;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;

import java.io.IOException;

@Controller
public class FileController {
    @Inject
    S3Adapter s3Adapter;

    @Post(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA})
    public boolean uploadFile(@Part CompletedFileUpload file) throws IOException {
        s3Adapter.uploadFile(file.getFilename(), file.getBytes());
        return true;
    }

    @Get(value = "/download{?fileName}", consumes = {MediaType.MULTIPART_FORM_DATA})
    public HttpResponse<byte[]> downLoadFile(@Nullable String fileName) throws IOException {
        return HttpResponse.ok(s3Adapter.getFile(fileName))
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"");
    }
}