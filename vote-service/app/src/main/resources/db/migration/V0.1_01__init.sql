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
    updated_at  DATETIME(3)  NOT NULL,
    CHECK (end_at >= start_at)
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
    updated_at  DATETIME(3)  NOT NULL,
    CONSTRAINT fk_vote_event_campaign FOREIGN KEY (campaign_id) REFERENCES vote_campaign (id),
    CONSTRAINT fk_vote_event_policy FOREIGN KEY (policy_id) REFERENCES vote_policy (id),
    CHECK (end_at >= start_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='투표 이벤트';

CREATE TABLE category
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    description TEXT         NULL,
    created_at  DATETIME(3)  NOT NULL,
    updated_at  DATETIME(3)  NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='재사용 카테고리';

CREATE TABLE vote_event_category
(
    event_id      BIGINT      NOT NULL,
    category_id   BIGINT      NOT NULL,
    display_order INT         NOT NULL,
    visibility    VARCHAR(16) NOT NULL,
    created_at    DATETIME(3) NOT NULL,
    updated_at    DATETIME(3) NOT NULL,
    PRIMARY KEY (event_id, category_id),
    CONSTRAINT fk_event_category_event FOREIGN KEY (event_id) REFERENCES vote_event (id),
    CONSTRAINT fk_event_category_category FOREIGN KEY (category_id) REFERENCES category (id),
    UNIQUE KEY uk_event_category_order (event_id, display_order)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='이벤트-카테고리 매핑';

CREATE TABLE candidate
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    description TEXT         NULL,
    image_url   VARCHAR(500) NULL,
    created_at  DATETIME(3)  NOT NULL,
    updated_at  DATETIME(3)  NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='후보';

CREATE TABLE category_candidate
(
    category_id   BIGINT      NOT NULL,
    candidate_id  BIGINT      NOT NULL,
    display_order INT         NOT NULL,
    visibility    VARCHAR(16) NOT NULL,
    created_at    DATETIME(3) NOT NULL,
    updated_at    DATETIME(3) NOT NULL,
    PRIMARY KEY (category_id, candidate_id),
    CONSTRAINT fk_category_candidate_category FOREIGN KEY (category_id) REFERENCES category (id),
    CONSTRAINT fk_category_candidate_candidate FOREIGN KEY (candidate_id) REFERENCES candidate (id),
    UNIQUE KEY uk_category_candidate_order (category_id, display_order)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='카테고리-후보 매핑';

CREATE TABLE vote_record
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id     BIGINT       NOT NULL,
    category_id  BIGINT       NOT NULL,
    candidate_id BIGINT       NOT NULL,
    user_id      VARCHAR(128) NOT NULL COMMENT '사용자 ID',
    voted_at     DATETIME(3)  NOT NULL,
    CONSTRAINT fk_vote_event FOREIGN KEY (event_id) REFERENCES vote_event (id),
    CONSTRAINT fk_vote_category FOREIGN KEY (category_id) REFERENCES category (id),
    CONSTRAINT fk_vote_candidate FOREIGN KEY (candidate_id) REFERENCES candidate (id),
    CONSTRAINT fk_vote_event_category FOREIGN KEY (event_id, category_id)
        REFERENCES vote_event_category (event_id, category_id),
    CONSTRAINT fk_vote_category_candidate FOREIGN KEY (category_id, candidate_id)
        REFERENCES category_candidate (category_id, candidate_id),
    KEY idx_vote_event_user (event_id, user_id),
    KEY idx_vote_category_candidate (category_id, candidate_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='투표 기록';
