package com.github.theprogmatheus.devroom.treasurehunt.database.entity;

import java.util.Objects;
import java.util.UUID;

public class TreasureClaimEntity {

    private String treasureId;
    private UUID playerId;
    private long claimedAt;

    public TreasureClaimEntity() {
    }

    public TreasureClaimEntity(String treasureId, UUID playerId, long claimedAt) {
        this.treasureId = treasureId;
        this.playerId = playerId;
        this.claimedAt = claimedAt;
    }

    public String getTreasureId() {
        return treasureId;
    }

    public void setTreasureId(String treasureId) {
        this.treasureId = treasureId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public long getClaimedAt() {
        return claimedAt;
    }

    public void setClaimedAt(long claimedAt) {
        this.claimedAt = claimedAt;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        TreasureClaimEntity that = (TreasureClaimEntity) object;
        return claimedAt == that.claimedAt && Objects.equals(treasureId, that.treasureId) && Objects.equals(playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(treasureId, playerId, claimedAt);
    }

    @Override
    public String toString() {
        return "TreasureClaimEntity{" +
                "treasureId='" + treasureId + '\'' +
                ", playerId=" + playerId +
                ", claimedAt=" + claimedAt +
                '}';
    }
}
