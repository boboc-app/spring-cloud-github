
val springCloudVersion : String by properties
val springBootVersion : String by properties
val okHttpVersion: String by properties

plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management") version "1.1.6"
    id("com.gradleup.nmcp").version("0.0.7")
    signing
    `maven-publish`
}


repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}
dependencyManagement {
    dependencies {
        dependency("org.springframework.cloud:spring-cloud-context:$springCloudVersion")
    }
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.springframework:spring-core")
    implementation("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.cloud:spring-cloud-context")


    testImplementation(kotlin("test"))
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:$okHttpVersion")
    testImplementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

val javadocJar = tasks.register("javadocJar", Jar::class) {
    archiveClassifier = "javadoc"
    sourceSets["main"].allSource
    group = "deploy"
}

val sourcesJar = tasks.register("sourcesJar", Jar::class) {
    archiveClassifier = "sources"
    sourceSets["main"].allSource
    group = "deploy"
}

val deployJar = tasks.register("deployJar", Jar::class) {

    archiveClassifier = "sources"
    sourceSets["main"].allSource
    group = "deploy"
}

tasks.jar{
    archiveClassifier = ""
}


group = "app.boboc"
version = "0.0.2"

val groupName = group.toString()
val artifactId = "spring-cloud-github"
val artifactVersion = version.toString()
val taskName = "SpringCloudGitHub"


val publishing = project.extensions.getByType(PublishingExtension::class)

publishing.publications {
    create<MavenPublication>(taskName) {
        groupId = groupName
        artifactId = artifactId
        version = artifactVersion

        from(project.components["java"])

        artifact(tasks.kotlinSourcesJar)
        artifact(javadocJar)

        pom {
            name = artifactId
            description = "Spring Cloud for GitHub"
            url = "https://github.com/boboc-app/spring-cloud-github"
            issueManagement {
                url = "https://github.com/boboc-app/spring-cloud-github/issues"
            }
            developers {
                developer {
                    id = "bo.kang"
                    email = "ebfks0301@gmail.com"
                    name = "Bo Chan Kang"
                }
            }
            scm {
                url = "https://github.com/boboc-app/spring-cloud-github"
                connection = "scm:git:github.com/boboc-app/spring-cloud-github.git"
            }
            licenses {
                license {
                    name = "MIT License"
                    url = "https://github.com/boboc-app/spring-cloud-github/blob/master/LICENSE"
                }
            }
        }
    }

    project.extensions.getByType(SigningExtension::class.java).apply {
        useGpgCmd()
        sign(publishing.publications)
    }
}


nmcp {
    publish(taskName) {
        username = System.getenv("MAVEN_CENTRAL_USERNAME")
        password = System.getenv("MAVEN_CENTRAL_PASSWORD")
        publicationType = "USER_MANAGED"
        version = artifactVersion
    }
}

