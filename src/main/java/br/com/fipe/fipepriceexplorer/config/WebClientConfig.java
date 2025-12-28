package br.com.fipe.fipepriceexplorer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    private static final String FIPE_BASE_URL = "https://fipe.parallelum.com.br/api/v2";

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(FIPE_BASE_URL).build();
    }
}
