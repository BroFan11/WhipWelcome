package me.brofan11.whipwelcome;

import me.brofan11.whipwelcome.commands.WelcomeCommand;
import me.brofan11.whipwelcome.database.DatabaseManager;
import me.brofan11.whipwelcome.listeners.PlayerJoinListener;
import me.brofan11.whipwelcome.utils.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;

public final class Whipwelcome extends JavaPlugin {

    private static Whipwelcome instance;
    private DatabaseManager databaseManager;
    private UpdateChecker updateChecker;

    @Override
    public void onEnable() {
        instance = this;
        
        // Save default config
        saveDefaultConfig();
        
        // Initialize database
        databaseManager = new DatabaseManager(this);
        databaseManager.connect();
        
        // Register events
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        
        // Register commands
        WelcomeCommand welcomeCommand = new WelcomeCommand(this);
        getCommand("welcome").setExecutor(welcomeCommand);
        getCommand("welcome").setTabCompleter(welcomeCommand);
        
        // Check for updates
        updateChecker = new UpdateChecker(this, "brofan11/WhipWelcome");
        updateChecker.checkForUpdates();
        
        getLogger().info("WhipWelcome has been enabled!");
    }

    @Override
    public void onDisable() {
        // Disconnect database
        if (databaseManager != null) {
            databaseManager.disconnect();
        }
        
        getLogger().info("WhipWelcome has been disabled!");
    }
    
    public static Whipwelcome getInstance() {
        return instance;
    }
    
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
