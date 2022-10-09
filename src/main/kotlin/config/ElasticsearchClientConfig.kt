package config

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.ElasticsearchTransport
import co.elastic.clients.transport.rest_client.RestClientTransport
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories


//
@Configuration
@EnableElasticsearchRepositories(basePackages = arrayOf("*"))
class ElasticsearchClientConfig {


    // INFO: Working With 7.17.6
    @Bean fun elasticSeacrhClient(): ElasticsearchClient {
        val httpClient = RestClient.builder(
            HttpHost("localhost", 9200)
        ).build()

// Create the HLRC

// Create the HLRC
        val hlrc = RestHighLevelClientBuilder(httpClient)
            .setApiCompatibilityMode(true)
            .build()

// Create the Java API Client with the same low level client

// Create the Java API Client with the same low level client
        val transport: ElasticsearchTransport = RestClientTransport(
            httpClient,
            JacksonJsonpMapper()
        )

        return ElasticsearchClient(transport)
    }

}