package org.test.sotfgen.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.test.sotfgen.minio.MinioProps;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Component
@RequiredArgsConstructor
@Slf4j
public class MinioBucketInitializer {

    private final S3Client s3Client;
    private final MinioProps minioProps;

    @PostConstruct
    public void init() {
        try {
            boolean exists = s3Client.listBuckets().buckets().stream()
                    .anyMatch(bucket -> bucket.name().equals(minioProps.getBucket()));

            if (!exists) {
                s3Client.createBucket(b -> b.bucket(minioProps.getBucket()));
                log.debug("Bucket created: " + minioProps.getBucket());
            } else {
                log.debug("Bucket already exists: " + minioProps.getBucket());
            }
        } catch (S3Exception e) {
            log.error("Failed to create bucket: {}", e.awsErrorDetails().errorMessage());
        }
    }
}