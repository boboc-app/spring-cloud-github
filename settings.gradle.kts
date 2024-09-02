pluginManagement {
    val springBootVersion  : String by settings

    plugins{
        id("org.springframework.boot") version springBootVersion
    }
}


plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "spring-cloud-github"

