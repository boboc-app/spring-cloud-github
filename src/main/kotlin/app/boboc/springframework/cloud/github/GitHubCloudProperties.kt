package app.boboc.springframework.cloud.github

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(GitHubCloudProperties.PROPERTIES_PREFIX)
data class GitHubCloudProperties(
    val token: String = System.getenv("GITHUB_CLOUD_TOKEN") ?: System.getProperty("GITHUB_CLOUD_TOKEN"),
    val endPointUri: String? = System.getenv("GITHUB_CLOUD_URI") ?: System.getProperty("GITHUB_CLOUD_URI"),
){
    companion object{
        const val PROPERTIES_PREFIX="github.cloud"
    }
}