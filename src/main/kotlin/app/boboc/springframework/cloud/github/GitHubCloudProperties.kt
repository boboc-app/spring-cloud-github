package app.boboc.springframework.cloud.github

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(GitHubCloudProperties.PROPERTIES_PREFIX)
data class GitHubCloudProperties(
    val token: String? = null,
    val endPointUri: String? = null,
){
    companion object{
        const val PROPERTIES_PREFIX="github.cloud"
    }
}