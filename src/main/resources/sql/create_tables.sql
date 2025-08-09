CREATE TABLE IF NOT EXISTS %table_prefix%treasures (
    id VARCHAR(64) PRIMARY KEY,
    world VARCHAR(64) NOT NULL,
    x INT NOT NULL,
    y INT NOT NULL,
    z INT NOT NULL,
    command TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY idx_treasure_position (world, x, y, z)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS %table_prefix%treasure_claims (
    treasure_id VARCHAR(64) NOT NULL,
    player_uuid CHAR(36) NOT NULL,
    claimed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (treasure_id, player_uuid),
    CONSTRAINT fk_treasure FOREIGN KEY (treasure_id)
        REFERENCES %table_prefix%treasures(id)
        ON DELETE CASCADE,
    KEY idx_claim_treasure (treasure_id)
) ENGINE=InnoDB;