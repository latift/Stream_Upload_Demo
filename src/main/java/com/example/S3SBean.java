package com.example;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Value;
import io.micronaut.context.env.Environment;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.Optional;


@Singleton
public class S3SBean {

    @Inject
    Environment env;
    @Value("${aws.clientId}")
    String awsClientId;
    @Value("${aws.clientSecret}")
    String awsClientSecret;

    @Bean
    public S3Client getS3Client() {

        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                Optional.of(awsClientId).get(),
                Optional.of(awsClientSecret).get()
        );

        S3Client client = S3Client.builder().region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(credentials)).build();
        return client;
    }


}