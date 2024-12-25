package com.craftelix.filestorage.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "app.user")
public class DefaultUserProperties {

    private String name;

    private String password;

}
