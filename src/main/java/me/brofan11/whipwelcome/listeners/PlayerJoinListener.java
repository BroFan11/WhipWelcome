package me.brofan11.whipwelcome.listeners;

import me.brofan11.whipwelcome.Whipwelcome;
import me.brofan11.whipwelcome.database.PlayerMessage;
import me.brofan11.whipwelcome.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    
    private final Whipwelcome plugin;
    
    public PlayerJoinListener(Whipwelcome plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Load player's custom prefix/suffix (or empty defaults)
        PlayerMessage playerMessage = plugin.getDatabaseManager().getPlayerMessage(player.getUniqueId());

        String prefix = playerMessage != null && playerMessage.getPrefix() != null
                ? playerMessage.getPrefix()
                : plugin.getConfig().getString("welcome.default-prefix", "");

        String suffix = playerMessage != null && playerMessage.getSuffix() != null
                ? playerMessage.getSuffix()
                : plugin.getConfig().getString("welcome.default-suffix", "");

        // Only set a join message if at least prefix or suffix is defined
        if (!prefix.isEmpty() || !suffix.isEmpty()) {
            String joinMessage = prefix + "<white>" + player.getName() + "</white>" + suffix;
            Component joinComponent = ColorUtils.translateColors(joinMessage);
            event.joinMessage(joinComponent);
        } else {
            // Hide join message if nothing is configured
            event.joinMessage(null);
        }
    }
}
