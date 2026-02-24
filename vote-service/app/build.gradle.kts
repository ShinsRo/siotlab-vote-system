plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

// Mockito inline mock-maker dynamic agent warnings 방지용 에이전트 설정
val mockitoAgent by configurations.creating

dependencies {
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.flyway.core)
    runtimeOnly(libs.mysql.connector.j)
    developmentOnly(libs.spring.boot.docker.compose)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.assertj.core)
    testImplementation(libs.reactor.test)
    testRuntimeOnly(libs.junit.platform.launcher)

    // 테스트 JVM에 -javaagent로 주입하기 위한 mockito-core (transitive 제외)
    mockitoAgent("org.mockito:mockito-core") { isTransitive = false }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    jvmArgs("-javaagent:${mockitoAgent.singleFile.absolutePath}")
}
