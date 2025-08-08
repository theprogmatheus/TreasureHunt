package com.github.theprogmatheus.devroom.treasurehunt.database.entity;

import java.util.Objects;

public class TreasureEntity {

    private String id;
    private String world;
    private int x, y, z;
    private String command;
    private long createdAt;

    public TreasureEntity() {
    }

    public TreasureEntity(String id, String world, int x, int y, int z, String command, long createdAt) {
        this.id = id;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.command = command;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        TreasureEntity that = (TreasureEntity) object;
        return x == that.x && y == that.y && z == that.z && createdAt == that.createdAt && Objects.equals(id, that.id) && Objects.equals(world, that.world) && Objects.equals(command, that.command);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, world, x, y, z, command, createdAt);
    }

    @Override
    public String toString() {
        return "TreasureEntity{" +
                "id='" + id + '\'' +
                ", world='" + world + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", command='" + command + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
