CREATE TABLE IF NOT EXISTS %table_prefix%treasures (
    id VARCHAR(64) PRIMARY KEY,
    world VARCHAR(64) NOT NULL,
    x INT NOT NULL,
    y INT NOT NULL,
    z INT NOT NULL,
    command TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;


CREATE UNIQUE INDEX idx_treasure_position
ON %table_prefix%treasures(world, x, y, z);

CREATE TABLE IF NOT EXISTS %table_prefix%treasure_claims (
    treasure_id VARCHAR(64) NOT NULL,
    player_uuid CHAR(36) NOT NULL,
    claimed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (treasure_id, player_uuid),
    CONSTRAINT fk_treasure FOREIGN KEY (treasure_id)
        REFERENCES %table_prefix%treasures(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_claim_treasure
ON treasure_claims(treasure_id);