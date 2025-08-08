SELECT id, command
FROM %table_prefix%treasures
WHERE world = ? AND x = ? AND y = ? AND z = ?;