package app.boboc.springframework.cloud.github

import app.boboc.client.github.GitHubContentClient
import app.boboc.springframework.cloud.github.GitHubClientTestHelper.dirObjList
import app.boboc.springframework.cloud.github.GitHubClientTestHelper.fileBody
import app.boboc.springframework.cloud.github.GitHubClientTestHelper.mockResponseDir
import app.boboc.springframework.cloud.github.GitHubClientTestHelper.mockResponseDirFail
import app.boboc.springframework.cloud.github.GitHubClientTestHelper.mockResponseFile
import app.boboc.springframework.cloud.github.GitHubClientTestHelper.om
import app.boboc.client.github.GitHubClientUtils.bodyToString
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class GitHubContentClientTest {
    private val mockWebServer: MockWebServer= MockWebServer()
    lateinit var baseUrl: String

    @BeforeEach
    fun setUp(){
        mockWebServer.start()
        baseUrl = mockWebServer.url("").toUrl().toString()
        println(baseUrl)
    }

    @Test
    fun getContentResponse() {
        val gitHubContentClient = GitHubContentClient(token =  GitHubClientTestHelper.API_KEY, uri = baseUrl)
        mockWebServer.enqueue(mockResponseDir)
        val r = gitHubContentClient.getContentResponse("asd","zxc","qwe")

        Assertions.assertEquals(
            om.writeValueAsString(dirObjList), r.bodyToString()
        )
    }

    @Test
    fun getContentResponse_throw() {
        val gitHubContentClient = GitHubContentClient(token =  GitHubClientTestHelper.API_KEY, uri = baseUrl)
        mockWebServer.enqueue(mockResponseDirFail)

        Assertions.assertThrows(Exceptions.GitHubClientException::class.java) {
            gitHubContentClient.getContentResponse("asd", "zxc", "qwe")
        }
    }

    @Test
    fun getFileContent() {
        val gitHubContentClient = GitHubContentClient(token =  GitHubClientTestHelper.API_KEY, uri = baseUrl)
        mockWebServer.enqueue(mockResponseFile)
        val r = gitHubContentClient.getFileContent("asd","zxc","qwe")

        Assertions.assertEquals(
            fileBody, r
        )
    }

    @Test
    fun getDirectoryContents() {
        val gitHubContentClient = GitHubContentClient(token =  GitHubClientTestHelper.API_KEY, uri = baseUrl)
        mockWebServer.enqueue(mockResponseDir)
        println(mockResponseDir.headers)
        val r = gitHubContentClient.getDirectoryContents("asd","zxc","qwe")

        Assertions.assertEquals(
            dirObjList, r
        )
    }
}