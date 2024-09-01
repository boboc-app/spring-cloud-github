package app.boboc.springframework.cloud.github

import app.boboc.client.github.GitHubContentClient
import org.springframework.boot.BootstrapRegistry
import org.springframework.boot.context.config.ConfigDataLocation
import org.springframework.boot.context.config.ConfigDataLocationResolver
import org.springframework.boot.context.config.ConfigDataLocationResolverContext
import org.springframework.boot.context.properties.bind.Bindable
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.boot.logging.DeferredLogFactory
import kotlin.reflect.KClass

class GitHubCloudConfigDataLocationResolver(
    deferredLogFactory: DeferredLogFactory
) : ConfigDataLocationResolver<GitHubCloudConfigDataResource> {

    val log = deferredLogFactory.getLog(GitHubCloudConfigDataLocationResolver::class.java)


    companion object {
        const val PREFIX = "github-cloud/"
    }

    override fun isResolvable(
        context: ConfigDataLocationResolverContext,
        location: ConfigDataLocation
    ): Boolean = location.hasPrefix(PREFIX)


    override fun resolve(
        context: ConfigDataLocationResolverContext,
        location: ConfigDataLocation
    ): List<GitHubCloudConfigDataResource> {
        return try {
            log.info("Resolving - ${location.value}")
            val properties = context.binder.loadProperties()
            context.registerBean(GitHubCloudProperties::class, properties)
            context.registerBean(GitHubContentClient::class, createGitHubContentClient(properties))
            resolveContexts(location.getNonPrefixedValue(PREFIX))
                .buildResources()
        } catch (e: Exception) {
            log.info("Fail to resolve. cause of - [${e.message}]")
            throw e
        }
    }

    private fun resolveContexts(value: String): ResolvedContexts {
        val (targets, pathsString) = value.split(":")
        val seperatedTargets = targets.split("/")
        return ResolvedContexts(
            seperatedTargets[0], seperatedTargets[1], seperatedTargets.getOrNull(2), pathsString.split(";")
        )
    }

    private fun ResolvedContexts.buildResources(): List<GitHubCloudConfigDataResource> {
        return paths.map { path ->
            GitHubCloudConfigDataResource(
                owner, repository, ref, path
            )
        }
    }

    private fun Binder.loadProperties(): GitHubCloudProperties {
        return bind(GitHubCloudProperties.PROPERTIES_PREFIX, Bindable.of(GitHubCloudProperties::class.java))
            .orElseGet {
                GitHubCloudProperties()
            }
    }

    private fun <T : Any> ConfigDataLocationResolverContext.registerBean(clazz: KClass<T>, value: T) {
        bootstrapContext.registerIfAbsent(
            clazz.java,
            BootstrapRegistry.InstanceSupplier.of(value)
        )
    }

    private fun createGitHubContentClient(prop: GitHubCloudProperties): GitHubContentClient {
        return GitHubContentClient(prop.token, prop.endPointUri)
    }
}
