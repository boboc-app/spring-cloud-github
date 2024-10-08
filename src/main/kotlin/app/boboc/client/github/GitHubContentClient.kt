package app.boboc.client.github

import app.boboc.client.github.GitHubClientUtils.add
import app.boboc.client.github.GitHubClientUtils.addQueryParameter
import app.boboc.client.github.GitHubClientUtils.bodyToString
import app.boboc.client.github.GitHubClientUtils.isDir
import app.boboc.client.github.GitHubClientUtils.replaceOwner
import app.boboc.client.github.GitHubClientUtils.replacePath
import app.boboc.client.github.GitHubClientUtils.replaceRepository
import app.boboc.common.Exceptions
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

class GitHubContentClient(
    token: String,
    uri: String?
) {

    companion object {
        const val GITHUB_API_VERSION = "2022-11-28"
        const val DEFAULT_BASE_URL = "https://api.github.com"
        const val CONTENT_SEGMENT = "repos/{owner}/{repository}/contents/{path}"
        val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
        val DIRECTORY_TYPE_REFERENCE = object : TypeReference<List<GitHubDirectoryContent>>() {}
    }

    private val url = "${uri ?: DEFAULT_BASE_URL}/$CONTENT_SEGMENT".toHttpUrlOrNull()!!

    private val httpClient: OkHttpClient = OkHttpClient().newBuilder().build()

    private val header = Headers.Builder()
        .add(GitHubClientUtils.Header.AUTHORIZATION, "Bearer $token")
        .add(GitHubClientUtils.Header.ACCEPT, GitHubClientUtils.AcceptType.RAW)
        .add(GitHubClientUtils.Header.GITHUB_VERSION, GITHUB_API_VERSION)
        .build()

    private fun sendRequest(req: Request): Response {
        return httpClient
                .newCall(req)
                .execute()

    }

    private fun contentRequest(owner: String, repository: String, path: String, branch: String? = null): Request {
        return Request.Builder()
            .url(
                url.replaceSegments(owner, repository, path)
                    .addBranch(branch)
            )
            .headers(header)
            .build()

    }

    fun getContentResponse(owner: String, repository: String, path: String, branch: String? = null): Response {
        return sendRequest(contentRequest(owner, repository, path, branch))
            .also {
                if (!it.isSuccessful) {
                    throw Exceptions.GitHubClientException(it.bodyToString() ?: it.message)
                }
                if (it.body == null) {
                    throw Exceptions.GitHubClientException("Body cannot be null")
                }
            }
    }

    fun getFileContent(owner: String, repository: String, path: String, branch: String? = null): String {
        return getContentResponse(owner, repository, path, branch)
            .also {
                if (it.isDir()) {
                    throw Exceptions.GitHubClientException("Path should be a file.")
                }
            }
            .bodyToString()!!
    }

    fun getDirectoryContents(
        owner: String,
        repository: String,
        path: String,
        branch: String? = null
    ): List<GitHubDirectoryContent>? {
        return getContentResponse(owner, repository, path, branch)
            .also {
                if (!it.isDir()) {
                    throw Exceptions.GitHubClientException("Path should be a directory.")
                }
            }
            .run {
                mapper.readValue(this.body!!.bytes(), DIRECTORY_TYPE_REFERENCE)
            }
    }

    private fun HttpUrl.replaceSegments(owner: String, repository: String, path: String): HttpUrl {
        return this.replaceOwner(owner)
            .replaceRepository(repository)
            .replacePath(path)
    }

    private fun HttpUrl.addBranch(branch: String?): HttpUrl {
        return if (branch != null)
            this.addQueryParameter("refs", branch)
        else
            this
    }


}
