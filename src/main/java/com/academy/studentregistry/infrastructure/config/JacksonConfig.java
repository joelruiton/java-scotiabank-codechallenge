package com.academy.studentregistry.infrastructure.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class JacksonConfig implements WebFluxConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

        configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
        configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(mapper));
    }
}