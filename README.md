# Side Project: Vote System (투표 시스템)

투표 이벤트를 생성/운영하고, 카테고리 하위의 대상(후보)에 대한 투표 및 랭킹을 제공하는 시스템.

## Architecture

- Gateway
  - Authentication
  - Routing
  - Rate limiting
  - (Optinal) Circuit breaking
- Vote Service
  - Voting (category -> candidates)
  - Vote event management (기간/이름/참여 조건)
  - Ranking management
  - Redis 등 외부 컴포넌트 포함하여 추후 확장(캐시/락/큐/집계 에 대한 STEP2)
- DB (MySQL)
  - Candidate(투표 대상) 데이터 관리
  - Vote result(투표 결과) 데이터 관리

> 세부 설계/정책은 각 컴포넌트 폴더의 README.md에서 다룬다.
> - `gateway/README.md`
> - `vote-service/README.md`

## Repository Structure
```
├─ gateway/
│  └─ README.md
├─ vote-service/
│  └─ README.md
├─ db/
│  ├─ schema/
│  └─ README.md
└─ README.md
```

## Key Concepts (용어)

- Category: 투표 대상이 속한 분류(예: 영화/음악/상품군 등)
- Candidate: 카테고리 하위의 투표 대상(후보)
- Vote Event: 특정 기간 동안 진행되는 투표 이벤트
- Vote Campaign: Event 의 상위 기획 단위
- Participation Policy: 참여 조건(인증 필요 여부, 1인 1표/일일 1표 등)
- Ranking: 집계된 결과를 기준으로 한 순위(이벤트별/카테고리별 등)

## Requirements (High-level)

### Functional
- 투표 이벤트 생성/조회/수정/종료(기간 관리)
- 이벤트 내 카테고리/후보에 대한 투표
- 참여 조건(정책)에 따른 투표 허용/차단
- 결과 집계 및 랭킹 조회 (TODO: 추후 분리)

### Non-functional
- 높은 동시성에서 중복 투표 방지 및 일관성 보장(정책에 따라 다름)
- 안정적인 트래픽 제어(레이트 리밋, 서킷 브레이킹)
- 장애 격리(게이트웨이/서비스/DB/Redis)
- 관측 가능성(로그/메트릭/트레이싱) - TODO

## Interfaces (Draft)

- External clients -> Gateway -> Vote Service
- Vote Service -> MySQL
- Vote Service -> Redis (캐시/락/집계/큐 용도) - TODO

## Documentation

- Gateway details: `gateway/README.md`
- Vote service details: `vote-service/README.md`
- Database schema: `db/README.md` + `db/schema/`

## TODO
- API 스펙(엔드포인트/요청/응답/에러코드)
- 데이터 모델(테이블/인덱스)
- 참여 정책 정의(1인1표, 1일1표, 중복방지 키 등)
- 랭킹 산정 방식(실시간/배치, 스코어 정의)
- Redis 사용 목적 확정(락/카운터/캐시/스트림 등)
- 운영 요구사항(모니터링, 알림, 배포, 롤백)
