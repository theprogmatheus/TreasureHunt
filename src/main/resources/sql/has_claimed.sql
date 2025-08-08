SELECT 1
FROM %table_prefix%treasure_claims
WHERE treasure_id = ? AND player_uuid = ?
LIMIT 1;