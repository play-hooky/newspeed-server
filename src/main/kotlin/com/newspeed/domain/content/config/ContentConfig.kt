package com.newspeed.domain.content.config

import com.newspeed.domain.content.application.ContentSearchClient
import com.newspeed.domain.content.application.ContentSearchClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ContentConfig {

    @Bean
    fun searchClients(
        searchClients: List<ContentSearchClient>
    ): ContentSearchClients = ContentSearchClients.builder()
        .addAll(searchClients)
        .build()
}