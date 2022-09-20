package com.example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.multipart.MultipartBody;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


@MicronautTest
class StreamUploadTest {

    @Inject
    @Client("/upload")
    HttpClient client;

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Test
    void returns_httpResponseOK_if_upload_was_successful() throws IOException {
        //prepare
        var tempFile = File.createTempFile("data", ".txt");
        List<String> content = Arrays.asList("Line 1", "Line 2", "Line 3");

        // writes few lines
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            for (String s : content) {
                bw.write(s);
                bw.write(System.lineSeparator()); // new line
            }
        }

        var requestBody = MultipartBody.builder()
                .addPart(
                        "file",
                        tempFile.getName(),
                        MediaType.TEXT_PLAIN_TYPE,
                        tempFile
                ).build();
        //when
        var request = HttpRequest.POST("/", requestBody)
                .contentType(MediaType.MULTIPART_FORM_DATA_TYPE);

        var response = client.toBlocking().exchange(request, String.class);
        //then
        Assertions.assertEquals(response.status(), HttpStatus.OK);

    }

}
