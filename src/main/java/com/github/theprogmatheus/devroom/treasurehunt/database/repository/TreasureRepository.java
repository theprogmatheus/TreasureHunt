package com.github.theprogmatheus.devroom.treasurehunt.database.repository;

import com.github.theprogmatheus.devroom.treasurehunt.database.DatabaseManager;
import com.github.theprogmatheus.devroom.treasurehunt.database.entity.TreasureClaimEntity;
import com.github.theprogmatheus.devroom.treasurehunt.database.entity.TreasureEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TreasureRepository {

    private final DatabaseManager databaseManager;

    public TreasureRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public boolean createTreasure(String treasureId, String world, int x, int y, int z, String command) {
        String sqlQuery = this.databaseManager.getSqlQueryLoader().getQuery("insert_treasure");
        try (Connection connection = this.databaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setString(1, treasureId);
            statement.setString(2, world);
            statement.setInt(3, x);
            statement.setInt(4, y);
            statement.setInt(5, z);
            statement.setString(6, command);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean deleteTreasure(String treasureId) {
        String sqlQuery = this.databaseManager.getSqlQueryLoader().getQuery("delete_treasure");
        try (Connection connection = this.databaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setString(1, treasureId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public List<TreasureClaimEntity> getClaims(String treasureId) {
        String sqlQuery = this.databaseManager.getSqlQueryLoader().getQuery("get_claimed_players");
        List<TreasureClaimEntity> claims = new ArrayList<>();

        try (Connection connection = this.databaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setString(1, treasureId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    UUID playerId = UUID.fromString(rs.getString("player_uuid"));
                    Timestamp claimedAt = rs.getTimestamp("claimed_at");

                    claims.add(new TreasureClaimEntity(treasureId, playerId, claimedAt.getTime()));
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return claims;
    }

    public TreasureEntity getTreasureByPosition(String world, int x, int y, int z) {
        String sqlQuery = this.databaseManager.getSqlQueryLoader().getQuery("get_treasure_by_position");

        try (Connection connection = this.databaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setString(1, world);
            statement.setInt(2, x);
            statement.setInt(3, y);
            statement.setInt(4, z);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    String command = rs.getString("command");
                    Timestamp createdAt = rs.getTimestamp("created_at");
                    return new TreasureEntity(id, world, x, y, z, command, createdAt.getTime());
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public List<TreasureEntity> getTreasures() {
        String sqlQuery = this.databaseManager.getSqlQueryLoader().getQuery("get_treasures");
        List<TreasureEntity> treasures = new ArrayList<>();

        try (Connection connection = this.databaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id");
                String world = rs.getString("world");
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int z = rs.getInt("z");
                String command = rs.getString("command");
                Timestamp createdAt = rs.getTimestamp("created_at");

                treasures.add(new TreasureEntity(id, world, x, y, z, command, createdAt.getTime()));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return treasures;
    }

    public boolean claim(String treasureId, UUID playerId) {
        String sqlQuery = this.databaseManager.getSqlQueryLoader().getQuery("insert_claim");
        try (Connection connection = this.databaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setString(1, treasureId);
            statement.setString(2, playerId.toString());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException sqlException) {
            if (sqlException.getSQLState().startsWith("23"))
                return false;
            sqlException.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean hasClaimed(String treasureId, UUID playerId) {
        String sqlQuery = this.databaseManager.getSqlQueryLoader().getQuery("has_claimed");
        try (Connection connection = this.databaseManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setString(1, treasureId);
            statement.setString(2, playerId.toString());

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
