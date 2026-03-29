package com.bindglam.agora.post.censor;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "agora.censorserver")
@Component
@Getter
@Setter
public class CensorServerConfig {
    private String type;
}
