# SIOTLAB Vote System

투표 캠페인/이벤트/후보/정책을 관리하고 조회하는 백엔드 프로젝트.

## 구성
- `gateway`: API 게이트웨이(TBD)
- `vote-service`: 핵심 도메인 서비스(멀티 모듈)

## 저장소 구조
```text
siolab-vote-system
├── gateway/
│   └── README.md
├── vote-service/
│   ├── README.md
│   ├── adm/    # 쓰기(Admin) API
│   ├── api/    # 읽기(Public) API
│   └── core/   # 도메인/유스케이스/인프라
└── README.md
```

## 문서
- Gateway: `gateway/README.md`
- Vote Service: `vote-service/README.md`
