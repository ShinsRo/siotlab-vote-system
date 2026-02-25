-- Initial schema constraints (draft)

ALTER TABLE vote_campaign
    ADD CONSTRAINT ck_vote_campaign__period CHECK (end_at >= start_at);

ALTER TABLE vote_event
    ADD CONSTRAINT fk_vote_event__campaign FOREIGN KEY (campaign_id) REFERENCES vote_campaign (id)
        ON DELETE SET NULL ON UPDATE RESTRICT,
    ADD CONSTRAINT fk_vote_event__policy FOREIGN KEY (policy_id) REFERENCES vote_policy (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    ADD CONSTRAINT ck_vote_event__period CHECK (end_at >= start_at);

ALTER TABLE vote_event_candidate
    ADD CONSTRAINT fk_vote_event_candidate__event FOREIGN KEY (event_id) REFERENCES vote_event (id)
        ON DELETE CASCADE ON UPDATE RESTRICT,
    ADD CONSTRAINT fk_vote_event_candidate__candidate FOREIGN KEY (candidate_id) REFERENCES vote_candidate (id)
        ON DELETE CASCADE ON UPDATE RESTRICT,
    ADD CONSTRAINT ck_vote_event_candidate__display_order CHECK (display_order >= 0),
    ADD CONSTRAINT uk_vote_event_candidate__event_display_order UNIQUE (event_id, display_order);

ALTER TABLE vote_record
    ADD CONSTRAINT fk_vote_record__event FOREIGN KEY (event_id) REFERENCES vote_event (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    ADD CONSTRAINT fk_vote_record__candidate FOREIGN KEY (candidate_id) REFERENCES vote_candidate (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    ADD CONSTRAINT fk_vote_record__event_candidate FOREIGN KEY (event_id, candidate_id)
        REFERENCES vote_event_candidate (event_id, candidate_id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    ADD KEY idx_vote_record__event_user (event_id, user_id),
    ADD KEY idx_vote_record__event_candidate (event_id, candidate_id);
