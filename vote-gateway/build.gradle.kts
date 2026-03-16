plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

fun isMacOs(): Boolean = System.getProperty("os.name").contains("Mac", ignoreCase = true)

fun nettyMacOsClassifier(): String = when {
    System.getProperty("os.arch").contains("aarch64", ignoreCase = true) -> "osx-aarch_64"
    System.getProperty("os.arch").contains("arm64", ignoreCase = true) -> "osx-aarch_64"
    else -> "osx-x86_64"
}

dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.cloud.starter.gateway.server.webflux)
    implementation(libs.spring.security.oauth2.jose)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.reflect)

    if (isMacOs()) {
        runtimeOnly(libs.netty.resolver.dns.native.macos) {
            artifact {
                classifier = nettyMacOsClassifier()
            }
        }
    }
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.assertj.core)
    testImplementation(libs.reactor.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
