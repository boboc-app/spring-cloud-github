package app.boboc.springframework.cloud.github

import app.boboc.client.github.GitHubContentClient
import app.boboc.common.Exceptions
import org.apache.commons.logging.LogFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@AutoConfiguration
@EnableConfigurationProperties(GitHubCloudProperties::class)
class GitHubCloudAutoConfiguration {

    val log = LogFactory.getLog(javaClass)

    @ConditionalOnMissingBean
    @Bean
    fun gitHubContentClient(properties: GitHubCloudProperties): GitHubContentClient {

        return try {
            GitHubContentClient(
                properties.getToken(),
                properties.getEndPointUri(),
            )
        } catch (e: Exception){
            log.warn("Exception while creating GitHubContentClient", e)
            throw e
        }
    }

    private fun GitHubCloudProperties.getToken() = try {
        this.token ?: System.getenv("GITHUB_CLOUD_TOKEN") ?: System.getProperty("GITHUB_CLOUD_TOKEN") ?: throw Exceptions.TokenCannotBeEmptyException()
    } catch (e: Exception) {
        throw Exceptions.TokenCannotBeEmptyException(
            "GITHUB_CLOUD_TOKEN should be set on env or property"
        )
    }

    private fun GitHubCloudProperties.getEndPointUri() =
        this.endPointUri ?: System.getenv("GITHUB_CLOUD_URL") ?: System.getProperty("GITHUB_CLOUD_URL")


}