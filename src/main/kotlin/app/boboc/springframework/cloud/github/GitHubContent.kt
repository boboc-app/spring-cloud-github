package app.boboc.springframework.cloud.github

import com.fasterxml.jackson.annotation.JsonProperty
import app.boboc.springframework.cloud.github.GitHubClientUtils.GitHubContentType
import java.net.URL


data class GitHubDirectoryContent(
    val name: String,
    val path: String,
    val sha: String,
    val size: Int,
    val url: URL,
    @JsonProperty("html_url")
    val htmlUrl: URL,
    @JsonProperty("git_url")
    val gitUrl: URL,
    @JsonProperty("download_url")
    val downloadUrl: URL,
    val type: GitHubContentType,
    @JsonProperty("_links")
    val links: Links
)

data class Links(
    val self: URL,
    val html: URL,
    val git: URL,
)
