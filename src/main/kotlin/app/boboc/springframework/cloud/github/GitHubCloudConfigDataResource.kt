package app.boboc.springframework.cloud.github

import app.boboc.client.github.GitHubContentClient
import org.springframework.boot.context.config.ConfigDataResource

class GitHubCloudConfigDataResource(
    private val owner: String,
    private val repository: String,
    private val ref: String? = null,
    private val path: String,
) : ConfigDataResource(){
    fun getPropertySource(ghClient: GitHubContentClient): GitHubCloudPropertySource {
        return GitHubCloudPropertySource(ghClient, owner, repository, ref, path)
    }

}