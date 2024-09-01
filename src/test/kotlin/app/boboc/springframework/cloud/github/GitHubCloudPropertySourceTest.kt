package app.boboc.springframework.cloud.github

import app.boboc.client.github.GitHubContentClient
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class GitHubCloudPropertySourceTest {
    private val gitHubContentClient = mock(GitHubContentClient::class.java)
    @Test
    fun getPropertyTest() {
        `when`(gitHubContentClient.getFileContent(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(TestHelper.contentResponse)

        val expect = TestHelper.contentMap
        val propertySource = GitHubCloudPropertySource(
            gitHubContentClient,
            "owner",
            "repository",
            "branch",
            "test"
        )
        Assertions.assertEquals(expect["test1"], propertySource.getProperty("test1"))
        Assertions.assertEquals(expect["test2"], propertySource.getProperty("test2"))
        Assertions.assertEquals(expect.keys.toList(), propertySource.propertyNames.toList() )
        Assertions.assertEquals("github-cloud/owner/repository/branch:test", propertySource.name)
    }


}
