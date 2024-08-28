package app.boboc.springframework.cloud.github

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.Headers
import okhttp3.mockwebserver.MockResponse
import java.net.URI
import java.net.URL

object GitHubClientTestHelper {
    val om = ObjectMapper().registerKotlinModule()
    val dirHeader = Headers.Builder().add("Content-Type", "application/json")
        .add("Content-Type", "charset=utf-8").build()

    val fileHeader = Headers.Builder().add("Content-Type", "application/vnd.github.raw+json")
        .add("Content-Type", "charset=utf-8").build()
    const val API_KEY = "123456789"
    const val fileBody = "test: TT"
    val dir1 = GitHubDirectoryContent(
        name = "folderTest",
        path = "test/folderTest",
        sha = "123",
        size = 13,
        url = URI.create("https://api.github.com/repos/test/propertyTest/contents/test/folderTest?ref=main").toURL(),
        gitUrl = URI.create("https://api.github.com/repos/test/propertyTest/contents/test/folderTest?ref=main").toURL(),
        type = GitHubClientUtils.GitHubContentType.DIR,
        htmlUrl = URI.create("https://api.github.com/repos/test/propertyTest/contents/test/folderTest?ref=main")
            .toURL(),
        links = Links(
            URI.create("https://api.github.com/repos/test/propertyTest/contents/test/folderTest?ref=main").toURL(),
            URI.create("https://api.github.com/repos/test/propertyTest/contents/test/folderTest?ref=main").toURL(),
            URI.create("https://api.github.com/repos/test/propertyTest/contents/test/folderTest?ref=main").toURL()
        ),
        downloadUrl = URI.create("https://api.github.com/repos/test/propertyTest/contents/test/folderTest?ref=main")
            .toURL()
    )

    val dir2 = GitHubDirectoryContent(
        name = "folderTest2",
        path = "test/folderTest",
        sha = "456",
        size = 13,
        url = URI.create("https://api.github.com/repos/test/propertyTest/contents/test/folderTest?ref=main").toURL(),
        gitUrl = URI.create("https://api.github.com/repos/test/propertyTest/contents/test/folderTest?ref=main").toURL(),
        type = GitHubClientUtils.GitHubContentType.DIR,
        htmlUrl = URI.create("https://api.github.com/repos/test/propertyTest/contents/test/folderTest?ref=main")
            .toURL(),
        links = Links(
            URI.create("https://api.github.com/repos/test/propertyTest/contents/test/folderTest?ref=main").toURL(),
            URI.create("https://api.github.com/repos/test/propertyTest/contents/test/folderTest?ref=main").toURL(),
            URI.create("https://api.github.com/repos/test/propertyTest/contents/test/folderTest?ref=main").toURL()
        ),
        downloadUrl = URI.create("https://api.github.com/repos/test/propertyTest/contents/test/folderTest?ref=main")
            .toURL()
    )

    val dirObjList = listOf(dir1, dir2)


    val mockResponseDir = MockResponse()
        .setResponseCode(200)
        .setBody(om.writeValueAsString(dirObjList))
        .setHeaders(dirHeader)

    val mockResponseDirFail = MockResponse().setResponseCode(400).setBody(
        """{
            "message": "Bad credentials",
            "documentation_url": "https://docs.github.com/rest",
            "status": "401"
        }"""
    ).setHeaders(dirHeader)

    val mockResponseFile = MockResponse()
        .setResponseCode(200)
        .setBody(fileBody)
        .setHeaders(fileHeader)
}