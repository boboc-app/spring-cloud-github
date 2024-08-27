package app.boboc.springframework.cloud.github

import okhttp3.HttpUrl

object HttpUrlUtil {
    fun HttpUrl.replaceSegment(src : String, dst : String): HttpUrl {
        return newBuilder().apply {
            val idx = pathSegments.indexOf(src).takeIf { it!=-1 } ?: throw Exception()
            setPathSegment(idx, dst)
        }.build()
    }
}