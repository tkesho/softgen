package org.test.sotfgen.minio;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "minio")
public class MinioProps {
    private String bucket;
    private String url;
    private String accessKey;
    private String secretKey;
}