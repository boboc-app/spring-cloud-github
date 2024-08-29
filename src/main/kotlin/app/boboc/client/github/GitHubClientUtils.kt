package app.boboc.client.github

import app.boboc.springframework.cloud.github.Exceptions
import com.fasterxml.jackson.annotation.JsonProperty
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.Response

object GitHubClientUtils {
    fun HttpUrl.replaceSegment(src: String, dst: String): HttpUrl {
        return newBuilder().apply {
            val idx = pathSegments.indexOf(src).takeIf { it != -1 }
                ?: throw Exceptions.GitHubClientException("Fail replaceSegment. src: $src, dst: $dst")
            setPathSegment(idx, dst)
        }.build()
    }

    fun HttpUrl.addQueryParameter(key: String, value: String): HttpUrl =
        this.newBuilder().addQueryParameter(key, value).build()

    fun HttpUrl.addBranch(branch: String): HttpUrl = this.addQueryParameter("ref", branch)

    fun Headers.Builder.add(header: Header, value: String): Headers.Builder {
        return this.add(header.value, value)
    }

    fun Headers.Builder.add(header: Header, value: AcceptType): Headers.Builder {
        return this.add(header.value, value.value)
    }

    fun HttpUrl.replaceOwner(owner: String): HttpUrl = this.replaceSegment("{owner}", owner)
    fun HttpUrl.replaceRepository(repository: String): HttpUrl = this.replaceSegment("{repository}", repository)
    fun HttpUrl.replacePath(path: String): HttpUrl = this.replaceSegment("{path}", path)

    fun Headers.isDir(): Boolean = this.values("Content-Type").contains("application/json")
    fun Response.isDir(): Boolean = this.headers.isDir()

    fun Response.bodyToString() = body?.bytes()?.toString(Charsets.UTF_8)

    enum class Header(val value: String) {
        AUTHORIZATION("Authorization"),
        ACCEPT("Accept"),
        GITHUB_VERSION("X-GitHub-Api-Version"),
    }

    enum class AcceptType(val value: String) {
        OBJECT("application/vnd.github.object+json"),
        HTML("application/vnd.github.html+json"),
        RAW("application/vnd.github.raw+json"),
    }

    enum class GitHubContentType {
        @JsonProperty("file")
        FILE,

        @JsonProperty("dir")
        DIR;
    }
}