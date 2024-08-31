package app.boboc.springframework.cloud.github

import org.springframework.boot.context.config.ConfigDataLocation
import org.springframework.boot.context.config.ConfigDataLocationResolver
import org.springframework.boot.context.config.ConfigDataLocationResolverContext

class GitHubCloudConfigDataLocationResolver : ConfigDataLocationResolver<GitHubCloudConfigDataResource> {

    companion object {
        const val PREFIX = "github-cloud"
    }

    override fun isResolvable(
        context: ConfigDataLocationResolverContext,
        location: ConfigDataLocation
    ): Boolean = location.hasPrefix(PREFIX)


    override fun resolve(
        context: ConfigDataLocationResolverContext,
        location: ConfigDataLocation
    ): MutableList<GitHubCloudConfigDataResource> {
        TODO("Not yet implemented")
    }
}