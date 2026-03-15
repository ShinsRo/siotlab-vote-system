# SIOTLAB Vote System

투표 캠페인, 이벤트, 후보, 정책을 관리하고 조회하는 백엔드 토이 프로젝트다.

## 구성
- `vote-service`: 투표 도메인 API 서비스
- `vote-gateway`: Spring Cloud Gateway 기반 API 게이트웨이

## 저장소 구조
```text
siolab-vote-system
├── docker/
│   └── compose.yaml
├── scripts/
│   ├── build-images.sh
│   ├── dev-up.sh
│   └── dev-down.sh
├── vote-gateway/
│   ├── README.md
│   └── http/
├── vote-service/
│   ├── README.md
│   ├── adm/
│   ├── api/
│   └── core/
└── README.md
```

## 로컬 개발 흐름
게이트웨이 개발:
1. `./scripts/build-images.sh service`로 `vote-service` 이미지를 빌드한다.
2. `./scripts/dev-up.sh`로 `mysql`, `vote-api`, `vote-adm`를 올린다.
3. `vote-gateway`를 `bootRun`으로 실행한다.
4. 게이트웨이 `8080` 포트로 API를 호출한다.

서비스 개발:
1. `./scripts/dev-up.sh service`로 `mysql`만 올린다.
2. `vote-service`의 `api`, `adm` 중 필요한 앱을 `bootRun`으로 실행한다.

종료:
1. `./scripts/dev-down.sh`

## 문서
- [vote-service/README.md](/Users/ssk/Projects/siolab-vote-system/vote-service/README.md)
- [vote-gateway/README.md](/Users/ssk/Projects/siolab-vote-system/vote-gateway/README.md)
