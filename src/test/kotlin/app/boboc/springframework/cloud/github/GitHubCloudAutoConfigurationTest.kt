package app.boboc.springframework.cloud.github

import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.runner.ApplicationContextRunner


class GitHubCloudAutoConfigurationTest {

    companion object {
        val contextRunner: ApplicationContextRunner = ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(GitHubCloudAutoConfiguration::class.java))
    }

    @Test
    fun withPropertyValuesTest(){
        contextRunner
            .withPropertyValues("github.cloud.token=TEST_TOKEN")
            .run { context ->
                assertThat(context.containsBean("gitHubContentClient")).isTrue()
            }
    }

    @Test
    fun withSystemPropertiesTest(){
        contextRunner
            .withSystemProperties("GITHUB_CLOUD_TOKEN=TEST_TOKEN")
            .run { context ->
                println(System.getProperty("GITHUB_CLOUD_TOKEN"))
                assertThat(context.containsBean("gitHubContentClient")).isTrue()
            }
    }

    @Test
    fun tokenIsEmptyException(){
        contextRunner
            .run { context ->
                assertThrows(IllegalStateException::class.java) {
                    context.containsBean("gitHubContentClient")
                }
            }
    }

}