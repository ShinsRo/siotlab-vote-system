# Vote Service

WebFlux + R2DBC 기반의 투표 도메인 서비스다. 전체 저장소는 학습 및 실험 목적의 토이 프로젝트로 운영한다.

## 모듈 구조
- `core`: 도메인, 유스케이스, 저장소 어댑터
- `api`: 조회 중심 Public API 애플리케이션
- `adm`: 관리용 Admin API 애플리케이션

## 포트
- `api`: `8088`
- `adm`: `8089`

## 로컬 실행
MySQL만 필요한 경우:

```bash
../scripts/dev-up.sh service
```

애플리케이션 실행:

```bash
./gradlew :api:bootRun
./gradlew :adm:bootRun
```

`api`, `adm` 모두 기본적으로 `localhost:3306`의 로컬 MySQL과 연결한다.

## 도커 이미지 빌드

Dockerfile 방식:

```bash
../scripts/build-images.sh service
```

생성 이미지:
- `vote-base`
- `vote-api`
- `vote-adm`

Gradle 방식:

```bash
./gradlew :api:bootBuildImage
./gradlew :adm:bootBuildImage
```

생성 이미지:
- `vote-api`
- `vote-adm`

## 마이그레이션
- Flyway 마이그레이션은 현재 `api` 모듈 리소스에 있다.
- 위치: `api/src/main/resources/db/migration`

## 참고
- 루트 compose 파일: [docker/compose.yaml](../docker/compose.yaml)
