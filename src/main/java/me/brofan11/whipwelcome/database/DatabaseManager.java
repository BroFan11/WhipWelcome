package me.brofan11.whipwelcome.database;

import me.brofan11.whipwelcome.Whipwelcome;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class DatabaseManager {
    
    private final Whipwelcome plugin;
    private Connection connection;
    
    public DatabaseManager(Whipwelcome plugin) {
        this.plugin = plugin;
    }
    
    public void connect() {
        try {
            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            
            String url = "jdbc:sqlite:" + dataFolder.getAbsolutePath() + File.separator + "whipwelcome.db";
            connection = DriverManager.getConnection(url);
            
            createTable();
            plugin.getLogger().info("Database connected successfully!");
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to database: " + e.getMessage());
        }
    }
    
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                plugin.getLogger().info("Database disconnected!");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to disconnect from database: " + e.getMessage());
        }
    }
    
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS player_messages (" +
                "uuid TEXT PRIMARY KEY," +
                "player_name TEXT NOT NULL," +
                "prefix TEXT," +
                "suffix TEXT," +
                "join_prefix TEXT," +
                "join_suffix TEXT," +
                "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create table: " + e.getMessage());
        }
    }
    
    public PlayerMessage getPlayerMessage(UUID uuid) {
        String sql = "SELECT * FROM player_messages WHERE uuid = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new PlayerMessage(
                    UUID.fromString(rs.getString("uuid")),
                    rs.getString("player_name"),
                    rs.getString("prefix"),
                    rs.getString("suffix"),
                    rs.getString("join_prefix"),
                    rs.getString("join_suffix")
                );
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to get player message: " + e.getMessage());
        }
        
        return null;
    }
    
    public void setPlayerPrefix(UUID uuid, String playerName, String prefix) {
        String sql = "INSERT INTO player_messages (uuid, player_name, prefix) VALUES (?, ?, ?) " +
                "ON CONFLICT(uuid) DO UPDATE SET prefix = ?, player_name = ?, last_updated = CURRENT_TIMESTAMP";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, playerName);
            pstmt.setString(3, prefix);
            pstmt.setString(4, prefix);
            pstmt.setString(5, playerName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to set player prefix: " + e.getMessage());
        }
    }
    
    public void setPlayerSuffix(UUID uuid, String playerName, String suffix) {
        String sql = "INSERT INTO player_messages (uuid, player_name, suffix) VALUES (?, ?, ?) " +
                "ON CONFLICT(uuid) DO UPDATE SET suffix = ?, player_name = ?, last_updated = CURRENT_TIMESTAMP";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, playerName);
            pstmt.setString(3, suffix);
            pstmt.setString(4, suffix);
            pstmt.setString(5, playerName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to set player suffix: " + e.getMessage());
        }
    }
    
    public void setPlayerJoinPrefix(UUID uuid, String playerName, String joinPrefix) {
        String sql = "INSERT INTO player_messages (uuid, player_name, join_prefix) VALUES (?, ?, ?) " +
                "ON CONFLICT(uuid) DO UPDATE SET join_prefix = ?, player_name = ?, last_updated = CURRENT_TIMESTAMP";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, playerName);
            pstmt.setString(3, joinPrefix);
            pstmt.setString(4, joinPrefix);
            pstmt.setString(5, playerName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to set player join prefix: " + e.getMessage());
        }
    }
    
    public void setPlayerJoinSuffix(UUID uuid, String playerName, String joinSuffix) {
        String sql = "INSERT INTO player_messages (uuid, player_name, join_suffix) VALUES (?, ?, ?) " +
                "ON CONFLICT(uuid) DO UPDATE SET join_suffix = ?, player_name = ?, last_updated = CURRENT_TIMESTAMP";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, playerName);
            pstmt.setString(3, joinSuffix);
            pstmt.setString(4, joinSuffix);
            pstmt.setString(5, playerName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to set player join suffix: " + e.getMessage());
        }
    }
    
    public void resetPlayerMessages(UUID uuid) {
        String sql = "DELETE FROM player_messages WHERE uuid = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to reset player messages: " + e.getMessage());
        }
    }
}
