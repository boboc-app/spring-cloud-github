package app.boboc.springframework.cloud.github

data class ResolvedContexts(
    val owner: String,
    val repository: String,
    val ref: String? = null,
    val paths: List<String> = listOf()
)
