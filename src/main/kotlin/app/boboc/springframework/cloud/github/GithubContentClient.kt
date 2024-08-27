package app.boboc.springframework.cloud.github

import app.boboc.springframework.cloud.github.HttpUrlUtil.replaceSegment
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import java.net.URL
import javax.print.attribute.standard.DialogOwner

class GithubContentClient(
    val url : URL?,
    val token : String
) {

    companion object{
        const val DEFAULT_BASE_URL = "https://api.github.com"
        const val CONTENT_SEGMENT = "repos/{owner}/{repository}/contents/{path}"
        val DEFAULT_HTTP_URL = "$DEFAULT_BASE_URL/$CONTENT_SEGMENT".toHttpUrlOrNull()!!
    }

    private val httpClient : OkHttpClient = OkHttpClient().newBuilder().build()
    private val defaultUrl: HttpUrl = HttpUrl.Builder()
        .scheme("https")
        .build()


    fun asd(){
        url?.toHttpUrlOrNull()?.let { http ->  }
        HttpUrl.Builder()
    }

    private fun HttpUrl.replaceOwner(owner: String) : HttpUrl = this.replaceSegment("{owner}", owner)
    private fun HttpUrl.replaceRepository(repository: String) : HttpUrl = this.replaceSegment("{repository}", repository)
    private fun HttpUrl.replacePath(path: String) : HttpUrl = this.replaceSegment("{path}", path)



}

