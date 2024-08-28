package app.boboc.springframework.cloud.github

class Exceptions {
    abstract class BoBocException(
        override val message: String,
        override val cause: Throwable? = null
    ) : RuntimeException(
        message, cause
    )

    class GitHubClientException(override val message: String, override val cause: Throwable? = null) :
        BoBocException("GitHub client exception - $message", cause)
}