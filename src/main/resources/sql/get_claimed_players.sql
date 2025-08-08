SELECT player_uuid, claimed_at
FROM %table_prefix%treasure_claims
WHERE treasure_id = ?;