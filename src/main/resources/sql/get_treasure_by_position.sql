SELECT id, command, created_at
FROM %table_prefix%treasures
WHERE world = ? AND x = ? AND y = ? AND z = ?;