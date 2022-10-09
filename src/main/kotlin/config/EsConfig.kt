package config

import org.elasticsearch.client.RestHighLevelClient
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.RestClients
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import javax.net.ssl.SSLContext


//@Configuration
//@ComponentScan
//@EnableElasticsearchRepositories(basePackages = arrayOf("*"))
//class ESConfig : AbstractElasticsearchConfiguration() {
//    private lateinit var sslConfig: SSLConfig
//    init {
//        sslConfig = SSLConfig()
//    }
//
//    override fun elasticsearchClient(): RestHighLevelClient {
//        var sslContext: SSLContext? = null
//        try {
//            sslContext = sslConfig.sSLContext
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        val config: ClientConfiguration = ClientConfiguration.builder()
//            .connectedTo("https//localhost:9200")
//            .usingSsl(sslContext!!)
//            .withBasicAuth("elastic", "bIwjmD149CEkRdYhgnKf")
//            .build()
//        return RestClients.create(config).rest()
//    }
//}