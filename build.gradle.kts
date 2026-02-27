plugins {
    kotlin("jvm") version "2.3.10"
}

group = "io.github.guenzelsen"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.opensearch.client:opensearch-java:3.6.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
