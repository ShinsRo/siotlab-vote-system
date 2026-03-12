# Vote Gateway

Spring Cloud Gateway 기반 API 게이트웨이 프로젝트다. 토이 프로젝트 환경에서 라우팅과 로컬 개발 흐름을 검증하는 용도로 두고 있다.

## 역할
- `vote-api`, `vote-adm` 라우팅
- 로컬 개발 시 의존 서비스 compose 기동
- 추후 인증/인가, 공통 필터, 레이트 리미팅 확장

## 포트
- `vote-gateway`: `8080`

## 로컬 실행
```bash
./gradlew bootRun
```

실행 시 `docker-local/compose.yaml`을 통해 아래 서비스가 함께 올라간다.
- `mysql`
- `vote-api`
- `vote-adm`

전제:
- `vote-api`, `vote-adm` 이미지는 미리 빌드돼 있어야 한다.

## 라우트
- `/vote-api/**` -> `vote-api`
- `/vote-adm/**` -> `vote-adm`

기본 로컬 타깃:
- `VOTE_API_URI=http://localhost:8088`
- `VOTE_ADM_URI=http://localhost:8089`

## 개발용 파일
- compose: [docker-local/compose.yaml](/Users/ssk/Projects/siolab-vote-system/vote-gateway/docker-local/compose.yaml)
- HTTP 예제: [http/api.v1.vote-api.http](/Users/ssk/Projects/siolab-vote-system/vote-gateway/http/api.v1.vote-api.http)
- HTTP 예제: [http/api.v1.vote-adm.http](/Users/ssk/Projects/siolab-vote-system/vote-gateway/http/api.v1.vote-adm.http)
- actuator 예제: [http/actuator.http](/Users/ssk/Projects/siolab-vote-system/vote-gateway/http/actuator.http)

## TODO
- JWT/OAuth2 인증 방식 확정
- 서비스별 필터 정책 확장
- 타임아웃, 재시도, 서킷 브레이커 추가
