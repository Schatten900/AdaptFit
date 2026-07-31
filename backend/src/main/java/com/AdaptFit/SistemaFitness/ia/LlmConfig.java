package com.AdaptFit.SistemaFitness.ia;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class LlmConfig {

    @Bean
    public RestTemplate llmRestTemplate(LlmProperties llmProperties) {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(llmProperties.getTimeout()))
                .setReadTimeout(Duration.ofMillis(llmProperties.getTimeout()))
                .build();
    }
}
