package app.boboc.springframework.cloud.github

object TestHelper {
    val contentResponse = """
        test1: value1
        test2: value2
    """.trimIndent()

    val contentMap = mapOf(
        "test1" to "value1",
        "test2" to "value2",
    )
}