# Vote Service

WebFlux + R2DBC 기반의 투표 도메인 서비스.

## 모듈 구조
- `core`: 도메인, 유스케이스(`ReadService`, `WriteService`), Repository Adapter
- `adm`: 쓰기(Admin) API 애플리케이션 및 컨트롤러/DTO/예외 처리
- `api`: 읽기(Public) API 애플리케이션 및 컨트롤러/DTO/예외 처리

## 실행 포트
- `adm`: `8089`
- `api`: `8088`

## 프로젝트 구조
```text
vote-service
├── settings.gradle.kts
├── build.gradle.kts
├── gradle/libs.versions.toml
├── core
│   ├── build.gradle.kts
│   └── src
│       ├── main/kotlin/com/siotman/vote/core
│       │   ├── CoreBasePackage.kt
│       │   ├── campaign/{application, domain, infra}
│       │   ├── candidate/{application, domain, infra}
│       │   ├── policy/{application, domain, infra}
│       │   └── voteevent/{application, domain, infra}
│       ├── main/resources/db/migration
│       └── test/kotlin/com/siotman/vote/core
├── adm
│   ├── build.gradle.kts
│   └── src/main
│       ├── kotlin/com/siotman/vote/adm
│       │   ├── VoteAdmSpringApplication.kt
│       │   ├── campaign/
│       │   ├── candidate/
│       │   ├── policy/
│       │   └── voteevent/
│       └── resources/application.yml
└── api
    ├── build.gradle.kts
    └── src/main
        ├── kotlin/com/siotman/vote/api
        │   ├── VoteApiSpringApplication.kt
        │   ├── campaign/
        │   ├── candidate/
        │   ├── policy/
        │   └── voteevent/
        └── resources/application.yml
```

## 개발 명령어
```bash
./gradlew clean test
./gradlew :adm:bootRun
./gradlew :api:bootRun
```
