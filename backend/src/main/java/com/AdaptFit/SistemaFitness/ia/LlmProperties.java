package com.AdaptFit.SistemaFitness.ia;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "llm")
public class LlmProperties {
    private String baseUrl = "http://localhost:11434";
    private String model = "llama3.2:1b";
    private Double temperature = 0.7;
    private Long timeout = 300000L;
    private Integer maxTokens = 512;
}
