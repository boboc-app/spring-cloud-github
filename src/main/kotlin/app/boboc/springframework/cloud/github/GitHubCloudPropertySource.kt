package app.boboc.springframework.cloud.github

import app.boboc.client.github.GitHubContentClient
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.core.env.EnumerablePropertySource

class GitHubCloudPropertySource(
    private val gitHubContentClient: GitHubContentClient,
    private val owner: String,
    private val repository: String,
    private val path: String,
    private val ref: String? = null,
) : EnumerablePropertySource<GitHubContentClient>(
    "$PROPERTY_SOURCE_PREFIX/$owner/$repository" +( if (ref != null) "/$ref:" else ":" )+ path,
    gitHubContentClient
) {
    companion object {
        private val objectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
        const val PROPERTY_SOURCE_PREFIX = "github-cloud"
        val PROPERTY_TYPE_REFERENCE = object : TypeReference<Map<String, Any>>() {}
    }


    private val properties = loadProperties()

    private fun loadProperties(): Map<String, Any> {
        return gitHubContentClient.getFileContent(owner, repository, path, ref)
            .let { content ->
                runCatching {
                    objectMapper.readValue(content, PROPERTY_TYPE_REFERENCE)
                }.recover {
                    when (it) {
                        is JsonParseException -> {
                            mapOf(path.split("/").last() to content)
                        }

                        else -> throw it
                    }
                }.getOrThrow()
            }

    }

    override fun getProperty(name: String): Any? = properties[name]

    override fun getPropertyNames(): Array<String> = properties.keys.toTypedArray()
}