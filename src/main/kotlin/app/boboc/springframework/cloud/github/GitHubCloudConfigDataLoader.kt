package app.boboc.springframework.cloud.github

import app.boboc.client.github.GitHubContentClient
import org.springframework.boot.context.config.ConfigData
import org.springframework.boot.context.config.ConfigDataLoader
import org.springframework.boot.context.config.ConfigDataLoaderContext

class GitHubCloudConfigDataLoader: ConfigDataLoader<GitHubCloudConfigDataResource> {
    override fun load(context: ConfigDataLoaderContext, resource: GitHubCloudConfigDataResource): ConfigData {

        val ghClient = context.bootstrapContext.get(GitHubContentClient::class.java)

        return ConfigData(listOf(resource.getPropertySource(ghClient)))
    }


}
