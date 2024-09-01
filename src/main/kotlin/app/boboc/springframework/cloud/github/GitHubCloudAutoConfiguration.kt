package app.boboc.springframework.cloud.github

import app.boboc.client.github.GitHubContentClient
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@AutoConfiguration
@EnableConfigurationProperties(GitHubCloudProperties::class)
class GitHubCloudAutoConfiguration {

    val log: Log = LogFactory.getLog(GitHubCloudAutoConfiguration::class.java)

    @ConditionalOnMissingBean
    @Bean
    fun gitHubContentClient(properties: GitHubCloudProperties): GitHubContentClient {
        log.info("Create gitHubContentClient bean")
        return try {
            GitHubContentClient(
                properties.token,
                properties.endPointUri,
            )
        } catch (e: Exception){
            log.warn("Exception while creating GitHubContentClient", e)
            throw e
        }
    }
}
