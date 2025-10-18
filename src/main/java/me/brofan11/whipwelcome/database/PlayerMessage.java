package me.brofan11.whipwelcome.database;

import java.util.UUID;

public class PlayerMessage {
    
    private final UUID uuid;
    private final String playerName;
    private final String prefix;
    private final String suffix;
    private final String joinPrefix;
    private final String joinSuffix;
    
    public PlayerMessage(UUID uuid, String playerName, String prefix, String suffix, String joinPrefix, String joinSuffix) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.joinPrefix = joinPrefix;
        this.joinSuffix = joinSuffix;
    }
    
    public UUID getUuid() {
        return uuid;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public String getSuffix() {
        return suffix;
    }
    
    public String getJoinPrefix() {
        return joinPrefix;
    }
    
    public String getJoinSuffix() {
        return joinSuffix;
    }
}
