-- Initial schema (draft)

CREATE TABLE vote_campaign (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    description TEXT         NULL,
    start_at    DATETIME(3)  NOT NULL,
    end_at      DATETIME(3)  NOT NULL,
    status      VARCHAR(32)  NOT NULL,
    created_by  VARCHAR(128) NOT NULL COMMENT '사용자 ID',
    created_at  DATETIME(3)  NOT NULL,
    updated_at  DATETIME(3)  NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='투표 캠페인';

CREATE TABLE vote_policy (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(200) NOT NULL,
    type       VARCHAR(64)  NOT NULL COMMENT '정책 타입 키',
    params     JSON         NOT NULL COMMENT '정책 파라미터(JSON)',
    created_at DATETIME(3)  NOT NULL,
    updated_at DATETIME(3)  NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='투표 정책';

CREATE TABLE vote_event (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    campaign_id BIGINT       NULL,
    policy_id   BIGINT       NOT NULL,
    name        VARCHAR(200) NOT NULL,
    description TEXT         NULL,
    start_at    DATETIME(3)  NOT NULL,
    end_at      DATETIME(3)  NOT NULL,
    status      VARCHAR(32)  NOT NULL,
    created_by  VARCHAR(128) NOT NULL COMMENT '사용자 ID',
    created_at  DATETIME(3)  NOT NULL,
    updated_at  DATETIME(3)  NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='투표 이벤트';

CREATE TABLE vote_candidate
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    description TEXT         NULL,
    image_url   VARCHAR(500) NULL,
    created_at  DATETIME(3)  NOT NULL,
    updated_at  DATETIME(3)  NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='후보';

CREATE TABLE vote_event_candidate
(
    event_id      BIGINT      NOT NULL,
    candidate_id  BIGINT      NOT NULL,
    display_order INT         NOT NULL,
    visibility    VARCHAR(16) NOT NULL,
    created_at    DATETIME(3) NOT NULL,
    updated_at    DATETIME(3) NOT NULL,
    PRIMARY KEY (event_id, candidate_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='이벤트-후보 매핑';

CREATE TABLE vote_record
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id     BIGINT       NOT NULL,
    candidate_id BIGINT       NOT NULL,
    user_id      VARCHAR(128) NOT NULL COMMENT '사용자 ID',
    voted_at     DATETIME(3)  NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='투표 기록';
