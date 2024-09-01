package app.boboc.springframework.cloud.github

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.SpringApplication
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext
import kotlin.test.Test

class GitHubCloudConfigDataLoaderIntegrationTest {

    private val mockWebServer : MockWebServer = MockWebServer()

    companion object{
        lateinit var url: String
        const val YAML_RESOURCE = "path/resource1"
        const val PLAIN_TEXT_RESOURCE = "path/resource2"
        const val JSON_RESOURCE = "path/resource3"
        const val NEED_FLATTEN_RESOURCE = "path/resource4"
    }

    @BeforeEach
    fun setup() {

        url = mockWebServer.url("").toUrl().toString()
        mockWebServer.dispatcher = object: Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                return if(request.path!!.endsWith(YAML_RESOURCE)){
                    MockResponse()
                        .setResponseCode(200)
                        .setBody("""
                            first: 1
                            inner.second: 2
                        """.trimIndent())
                        .setHeader("Content-Type", "application/vnd.github.raw+json")
                }
                else if(request.path!!.endsWith(PLAIN_TEXT_RESOURCE)){
                    MockResponse()
                        .setResponseCode(200)
                        .setBody("plain text message")
                        .setHeader("Content-Type", "application/vnd.github.raw+json")
                }
                else if(request.path!!.endsWith(JSON_RESOURCE)){
                    MockResponse()
                        .setResponseCode(200)
                        .setBody("""
                            {
                                "first": 1,
                                "second": 2
                            }
                        """.trimIndent())
                        .setHeader("Content-Type", "application/vnd.github.raw+json")
                }
                else if(request.path!!.endsWith(NEED_FLATTEN_RESOURCE)){
                    MockResponse()
                        .setResponseCode(200)
                        .setBody("""
                            first: 1
                            inner:
                                second: 2
                        """.trimIndent())
                        .setHeader("Content-Type", "application/vnd.github.raw+json")
                }
                else MockResponse().setResponseCode(404)
            }
        }

    }

    @Test
    fun resolveProperties() {
        val owner = "owner"
        val repo = "repository"
        val path = YAML_RESOURCE
        val app = WrappedApplication()
            .withSpringImport("github-cloud/$owner/$repo:$path")

        val context = app.run()

        Assertions.assertEquals(context.environment.getProperty("first"), "1")
        Assertions.assertEquals(context.environment.getProperty("inner.second"), "2")
    }

    @Test
    fun resolvePlainTextProperties() {
        val owner = "owner"
        val repo = "repository"
        val path = PLAIN_TEXT_RESOURCE
        val app = WrappedApplication()
            .withSpringImport("github-cloud/$owner/$repo:$path")

        val context = app.run()

        Assertions.assertEquals(context.environment.getProperty("resource2"), "plain text message")
    }

    @Test
    fun resolveMultipleProperties() {
        val owner = "owner"
        val repo = "repository"
        val path = "$PLAIN_TEXT_RESOURCE;$YAML_RESOURCE"
        val app = WrappedApplication()
            .withSpringImport("github-cloud/$owner/$repo:$path")

        val context = app.run()

        Assertions.assertEquals(context.environment.getProperty("resource2"), "plain text message")
        Assertions.assertEquals(context.environment.getProperty("first"), "1")
        Assertions.assertEquals(context.environment.getProperty("inner.second"), "2")
    }

    @Test
    fun resolveJsonProperties() {
        val owner = "owner"
        val repo = "repository"
        val path = JSON_RESOURCE
        val app = WrappedApplication()
            .withSpringImport("github-cloud/$owner/$repo:$path")

        val context = app.run()

        Assertions.assertEquals(context.environment.getProperty("first"), "1")
        Assertions.assertEquals(context.environment.getProperty("second"), "2")
    }

    @Test
    fun resolveNeedFlattenPropertied(){
        val owner = "owner"
        val repo = "repository"
        val path = NEED_FLATTEN_RESOURCE
        val app = WrappedApplication()
            .withSpringImport("github-cloud/$owner/$repo:$path")

        val context = app.run()

        Assertions.assertEquals(context.environment.getProperty("first"), "1")
        Assertions.assertEquals(context.environment.getProperty("inner.second"), "2")
    }

    class WrappedApplication(
        private val app: SpringApplication = SpringApplication(TestApp::class.java),
        private val appArgument: Array<String> = arrayOf(
            "--github.cloud.token=TEST_TOKEN",
            "--github.cloud.end-point-uri=$url"
        )
    ) {
        init {
            app.apply {
                webApplicationType = WebApplicationType.NONE
            }
        }

        fun withSpringImport(configValue: String): WrappedApplication {
            return withArgument("spring.config.import", configValue)
        }

        fun withArgument(key: String, value: String): WrappedApplication {
            return WrappedApplication(app, appArgument + "--${key}=${value}")
        }

        fun run(): ConfigurableApplicationContext {
            return app.run(*appArgument)
        }
    }

    @SpringBootApplication
    class TestApp()


}
