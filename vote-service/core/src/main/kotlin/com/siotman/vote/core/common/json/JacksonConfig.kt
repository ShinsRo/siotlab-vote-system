package com.siotman.vote.core.common.json

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        return CoreJson.objectMapper.copy()
    }
}
