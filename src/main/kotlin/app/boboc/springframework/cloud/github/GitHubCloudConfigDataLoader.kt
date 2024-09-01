package app.boboc.springframework.cloud.github

import app.boboc.client.github.GitHubContentClient
import org.apache.commons.logging.LogFactory
import org.springframework.boot.context.config.ConfigData
import org.springframework.boot.context.config.ConfigDataLoader
import org.springframework.boot.context.config.ConfigDataLoaderContext

class GitHubCloudConfigDataLoader: ConfigDataLoader<GitHubCloudConfigDataResource> {
    val log = LogFactory.getLog(GitHubCloudConfigDataLoader::class.java)

    override fun load(context: ConfigDataLoaderContext, resource: GitHubCloudConfigDataResource): ConfigData {
        return try {
            log.info("Loading GitHub config data")
            val ghClient = context.bootstrapContext.get(GitHubContentClient::class.java)
            ConfigData(listOf(resource.getPropertySource(ghClient)))
        } catch (e: Exception){
            log.warn("Could not load GitHub config data", e)
            throw e
        }


    }


}
