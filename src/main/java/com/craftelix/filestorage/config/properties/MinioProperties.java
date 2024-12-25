package com.craftelix.filestorage.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String url;

    private String accessKey;

    private String secretKey;

    private String bucket;

}
