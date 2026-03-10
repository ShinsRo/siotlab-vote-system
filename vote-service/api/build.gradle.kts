import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.spring")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

fun isMacOs(): Boolean = System.getProperty("os.name").contains("Mac", ignoreCase = true)

fun nettyMacOsClassifier(): String = when {
    System.getProperty("os.arch").contains("aarch64", ignoreCase = true) -> "osx-aarch_64"
    System.getProperty("os.arch").contains("arm64", ignoreCase = true) -> "osx-aarch_64"
    else -> "osx-x86_64"
}

dependencies {
    implementation(project(":core"))
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.springdoc.openapi.starter.webflux.ui)
    if (isMacOs()) {
        runtimeOnly(libs.netty.resolver.dns.native.macos) {
            artifact {
                classifier = nettyMacOsClassifier()
            }
        }
    }
    developmentOnly(libs.spring.boot.docker.compose)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.assertj.core)
    testImplementation(libs.reactor.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named<BootBuildImage>("bootBuildImage") {
    imageName.set("vote-api")
}
