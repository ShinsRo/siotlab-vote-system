plugins {
    id("io.spring.dependency-management")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.spring")
    `java-library`
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

// Mockito inline mock-maker dynamic agent warnings 방지용 에이전트 설정
val mockitoAgent by configurations.creating

dependencies {
    api(platform("org.springframework.boot:spring-boot-dependencies:${libs.versions.spring.boot.get()}"))

    api(libs.spring.boot.starter.webflux)
    api(libs.spring.boot.starter.data.r2dbc)
    api(libs.spring.boot.starter.flyway)
    api(libs.flyway.core)
    api(libs.kotlin.reflect)

    runtimeOnly(libs.flyway.mysql)
    runtimeOnly(libs.mysql.connector.j)
    runtimeOnly(libs.r2dbc.mysql)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.assertj.core)
    testImplementation(libs.reactor.test)
    testRuntimeOnly(libs.junit.platform.launcher)

    mockitoAgent(libs.mockito.core) { isTransitive = false }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    jvmArgs("-javaagent:${mockitoAgent.singleFile.absolutePath}")
}
