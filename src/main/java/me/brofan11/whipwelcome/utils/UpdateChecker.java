package me.brofan11.whipwelcome.utils;

import me.brofan11.whipwelcome.Whipwelcome;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.logging.Level;

public class UpdateChecker {
    
    private final Whipwelcome plugin;
    private final String githubRepo;
    private String latestVersion;
    private String downloadUrl;
    
    /**
     * Creates an update checker for the plugin
     * @param plugin The plugin instance
     * @param githubRepo Repository in format "owner/repo" (e.g., "brofan11/WhipWelcome")
     */
    public UpdateChecker(Whipwelcome plugin, String githubRepo) {
        this.plugin = plugin;
        this.githubRepo = githubRepo;
    }
    
    /**
     * Checks for updates asynchronously to avoid blocking the main thread
     */
    public void checkForUpdates() {
        if (!plugin.getConfig().getBoolean("update-checker.enabled", true)) {
            return;
        }
        
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String apiUrl = "https://api.github.com/repos/" + githubRepo + "/releases/latest";
                URI uri = URI.create(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
                connection.setRequestProperty("User-Agent", "WhipWelcome-UpdateChecker");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                
                int responseCode = connection.getResponseCode();
                
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    JSONObject json = new JSONObject(response.toString());
                    String tagName = json.getString("tag_name");
                    
                    // Extract version from tag (supports: v1.0.0, 1.0.0, WhipWelcome-1.0.0, etc.)
                    latestVersion = extractVersion(tagName);
                    downloadUrl = json.getString("html_url");
                    
                    String currentVersion = plugin.getDescription().getVersion();
                    
                    if (isNewerVersion(currentVersion, latestVersion)) {
                        notifyUpdate();
                    } else {
                        plugin.getLogger().info("You are running the latest version!");
                    }
                } else {
                    plugin.getLogger().warning("Failed to check for updates. Response code: " + responseCode);
                }
                
                connection.disconnect();
                
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Could not check for updates: " + e.getMessage());
            }
        });
    }
    
    /**
     * Extracts version number from various tag formats
     * Supports: v1.0.0, 1.0.0, WhipWelcome-1.0.0, plugin-v1.0.0, etc.
     */
    private String extractVersion(String tagName) {
        // Remove common prefixes
        tagName = tagName.replaceFirst("^v", ""); // v1.0.0 -> 1.0.0
        tagName = tagName.replaceFirst("^WhipWelcome-", ""); // WhipWelcome-1.0.0 -> 1.0.0
        tagName = tagName.replaceFirst("^whipwelcome-", ""); // whipwelcome-1.0.0 -> 1.0.0
        
        // Extract the version number (X.Y.Z pattern)
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+\\.\\d+\\.\\d+[\\w\\-\\.]*)");
        java.util.regex.Matcher matcher = pattern.matcher(tagName);
        
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        return tagName; // Return as-is if no pattern matches
    }
    
    /**
     * Compares version strings to determine if an update is available
     * Supports semantic versioning (e.g., 1.0.0, 1.2.3, 2.0.0-beta)
     */
    private boolean isNewerVersion(String current, String latest) {
        try {
            // Remove any non-numeric prefixes for comparison
            String currentClean = current.split("-")[0];
            String latestClean = latest.split("-")[0];
            
            String[] currentParts = currentClean.split("\\.");
            String[] latestParts = latestClean.split("\\.");
            
            int maxLength = Math.max(currentParts.length, latestParts.length);
            
            for (int i = 0; i < maxLength; i++) {
                int currentPart = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
                int latestPart = i < latestParts.length ? Integer.parseInt(latestParts[i]) : 0;
                
                if (latestPart > currentPart) {
                    return true;
                } else if (latestPart < currentPart) {
                    return false;
                }
            }
            
            return false; // Versions are equal
        } catch (NumberFormatException e) {
            plugin.getLogger().warning("Could not parse version numbers for comparison");
            return false;
        }
    }
    
    /**
     * Notifies console and online operators about available updates
     */
    private void notifyUpdate() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            plugin.getLogger().info("╔════════════════════════════════════════╗");
            plugin.getLogger().info("║  WhipWelcome Update Available!        ║");
            plugin.getLogger().info("║                                        ║");
            plugin.getLogger().info("║  Current: " + String.format("%-28s", plugin.getDescription().getVersion()) + " ║");
            plugin.getLogger().info("║  Latest:  " + String.format("%-28s", latestVersion) + " ║");
            plugin.getLogger().info("║                                        ║");
            plugin.getLogger().info("║  Download: " + String.format("%-27s", downloadUrl) + "║");
            plugin.getLogger().info("╚════════════════════════════════════════╝");
            
            // Notify online ops
            if (plugin.getConfig().getBoolean("update-checker.notify-ops", true)) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.isOp()) {
                        player.sendMessage("§6[WhipWelcome] §eA new version is available: §a" + latestVersion);
                        player.sendMessage("§6[WhipWelcome] §7Download: §b" + downloadUrl);
                    }
                }
            }
        });
    }
    
    /**
     * Gets the latest version string (if checked)
     */
    public String getLatestVersion() {
        return latestVersion;
    }
    
    /**
     * Gets the download URL for the latest version
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }
}
