package org.test.sotfgen.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Component
@RequiredArgsConstructor
@Slf4j
public class MinioBucketInitializer {

    private final S3Client s3Client;
    private static final String BUCKET_NAME = "files";

    @PostConstruct
    public void init() {
        try {
            boolean exists = s3Client.listBuckets().buckets().stream()
                    .anyMatch(bucket -> bucket.name().equals(BUCKET_NAME));

            if (!exists) {
                s3Client.createBucket(b -> b.bucket(BUCKET_NAME));
                log.debug("Bucket created: " + BUCKET_NAME);
            } else {
                log.debug("Bucket already exists: " + BUCKET_NAME);
            }
        } catch (S3Exception e) {
            log.error("Failed to create bucket: {}", e.awsErrorDetails().errorMessage());
        }
    }
}