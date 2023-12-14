package com.newspeed.domain.content.application

import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.global.exception.content.DuplicateContentPlatformException
import com.newspeed.global.exception.content.UnsupportedContentPlatformException
import java.util.*

class ContentSearchClients(
    private val contentSearchClients: Map<QueryPlatform, ContentSearchClient>
) {
    companion object {
        fun builder(): ContentSearchClientBuilder = ContentSearchClientBuilder()

        data class ContentSearchClientBuilder(
            val contentSearchClients: EnumMap<QueryPlatform, ContentSearchClient> = EnumMap(QueryPlatform::class.java)
        ) {

            fun addAll(
                contentSearchClients: List<ContentSearchClient>
            ): ContentSearchClientBuilder {
                contentSearchClients
                    .forEach { add(it) }

                return this
            }

            fun add(
                contentSearchClient: ContentSearchClient
            ): ContentSearchClientBuilder {
                contentSearchClients.computeIfPresent(contentSearchClient.getQueryPlatform()) { _, _ ->
                    throw DuplicateContentPlatformException()
                }
                contentSearchClients[contentSearchClient.getQueryPlatform()] = contentSearchClient

                return this
            }

            fun build(): ContentSearchClients = ContentSearchClients(contentSearchClients)
        }
    }

    fun getClient(
        queryPlatform: QueryPlatform
    ): ContentSearchClient = contentSearchClients[queryPlatform] ?: throw UnsupportedContentPlatformException()
}