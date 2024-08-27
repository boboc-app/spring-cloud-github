package app.boboc.springframework.cloud.github

data class GithubContentClientParam(
    val url: String,
    val token: String? = null,
    val username: String? = null,
    val password: String? = null,
)
