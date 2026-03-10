# Vote Gateway

Spring Cloud Gateway 기반 API 게이트웨이 프로젝트다.

## 역할
- 인증/인가 처리
- 서비스 라우팅
- 레이트 리미팅
- 공통 필터 적용

## 실행
```bash
./gradlew bootRun
```

기본 포트는 `8080`이며, 예시 라우트는 `/vote-service/**` 요청을 `http://localhost:8088`의 `vote-service`로 전달한다.

## TODO
- JWT/OAuth2 인증 방식 확정
- 서비스별 라우트와 필터 정책 확장
- 장애 대응(타임아웃, 재시도, 서킷 브레이커) 추가
