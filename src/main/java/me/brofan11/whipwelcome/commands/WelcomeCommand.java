package me.brofan11.whipwelcome.commands;

import me.brofan11.whipwelcome.Whipwelcome;
import me.brofan11.whipwelcome.database.PlayerMessage;
import me.brofan11.whipwelcome.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WelcomeCommand implements CommandExecutor, TabCompleter {
    
    private final Whipwelcome plugin;
    
    public WelcomeCommand(Whipwelcome plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command can only be used by players!", NamedTextColor.RED));
            return true;
        }
        
        if (args.length == 0) {
            sendHelp(player);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "setprefix" -> {
                if (!player.hasPermission("whipwelcome.customize")) {
                    player.sendMessage(Component.text("You don't have permission to use this command!", NamedTextColor.RED));
                    return true;
                }
                
                if (args.length < 2) {
                    player.sendMessage(Component.text("Usage: /welcome setprefix <prefix>", NamedTextColor.RED));
                    return true;
                }
                
                String prefix = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                
                // Check length limit
                int maxLength = plugin.getConfig().getInt("welcome.max-prefix-length", 30);
                if (maxLength > 0) {
                    String strippedText = ColorUtils.stripColors(prefix);
                    if (strippedText.length() > maxLength) {
                        player.sendMessage(Component.text("Prefix is too long! Maximum length: " + maxLength + " characters (excluding color codes)", NamedTextColor.RED));
                        player.sendMessage(Component.text("Your text length: " + strippedText.length() + " characters", NamedTextColor.YELLOW));
                        return true;
                    }
                }
                
                plugin.getDatabaseManager().setPlayerPrefix(player.getUniqueId(), player.getName(), prefix);
                player.sendMessage(Component.text("Welcome prefix set to: ", NamedTextColor.GREEN)
                        .append(ColorUtils.translateColors(prefix)));
                return true;
            }
            
            case "setsuffix" -> {
                if (!player.hasPermission("whipwelcome.customize")) {
                    player.sendMessage(Component.text("You don't have permission to use this command!", NamedTextColor.RED));
                    return true;
                }
                
                if (args.length < 2) {
                    player.sendMessage(Component.text("Usage: /welcome setsuffix <suffix>", NamedTextColor.RED));
                    return true;
                }
                
                String suffix = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                
                // Check length limit
                int maxLength = plugin.getConfig().getInt("welcome.max-suffix-length", 30);
                if (maxLength > 0) {
                    String strippedText = ColorUtils.stripColors(suffix);
                    if (strippedText.length() > maxLength) {
                        player.sendMessage(Component.text("Suffix is too long! Maximum length: " + maxLength + " characters (excluding color codes)", NamedTextColor.RED));
                        player.sendMessage(Component.text("Your text length: " + strippedText.length() + " characters", NamedTextColor.YELLOW));
                        return true;
                    }
                }
                
                plugin.getDatabaseManager().setPlayerSuffix(player.getUniqueId(), player.getName(), suffix);
                player.sendMessage(Component.text("Welcome suffix set to: ", NamedTextColor.GREEN)
                        .append(ColorUtils.translateColors(suffix)));
                return true;
            }
            
            case "view" -> {
                PlayerMessage message = plugin.getDatabaseManager().getPlayerMessage(player.getUniqueId());
                
                if (message == null || (message.getPrefix() == null && message.getSuffix() == null)) {
                    player.sendMessage(Component.text("You haven't set any custom messages yet!", NamedTextColor.YELLOW));
                    player.sendMessage(Component.text("Using default messages from config.", NamedTextColor.GRAY));
                } else {
                    player.sendMessage(Component.text("=== Your Custom Join Message ===", NamedTextColor.GOLD));
                    
                    if (message.getPrefix() != null) {
                        player.sendMessage(Component.text("Prefix: ", NamedTextColor.AQUA)
                                .append(ColorUtils.translateColors(message.getPrefix())));
                    } else {
                        player.sendMessage(Component.text("Prefix: ", NamedTextColor.AQUA)
                                .append(Component.text("(not set)", NamedTextColor.GRAY)));
                    }
                    
                    if (message.getSuffix() != null) {
                        player.sendMessage(Component.text("Suffix: ", NamedTextColor.AQUA)
                                .append(ColorUtils.translateColors(message.getSuffix())));
                    } else {
                        player.sendMessage(Component.text("Suffix: ", NamedTextColor.AQUA)
                                .append(Component.text("(not set)", NamedTextColor.GRAY)));
                    }
                    
                    // Show preview
                    String prefix = message.getPrefix() != null ? message.getPrefix() : "";
                    String suffix = message.getSuffix() != null ? message.getSuffix() : "";
                    String preview = prefix + "<white>" + player.getName() + "</white>" + suffix;
                    player.sendMessage(Component.text("Preview: ", NamedTextColor.GOLD)
                            .append(ColorUtils.translateColors(preview)));
                }
                return true;
            }
            
            case "reset" -> {
                if (!player.hasPermission("whipwelcome.customize")) {
                    player.sendMessage(Component.text("You don't have permission to use this command!", NamedTextColor.RED));
                    return true;
                }
                
                plugin.getDatabaseManager().resetPlayerMessages(player.getUniqueId());
                player.sendMessage(Component.text("Your custom join messages have been reset!", NamedTextColor.GREEN));
                player.sendMessage(Component.text("You will now use the default messages from config.", NamedTextColor.GRAY));
                return true;
            }
            
            default -> {
                sendHelp(player);
                return true;
            }
        }
    }
    
    private void sendHelp(Player player) {
        player.sendMessage(Component.text("=== WhipWelcome Commands ===", NamedTextColor.GOLD));
        player.sendMessage(Component.text("/welcome setprefix <prefix>", NamedTextColor.AQUA)
                .append(Component.text(" - Set your welcome prefix (used in join message)", NamedTextColor.GRAY)));
        player.sendMessage(Component.text("/welcome setsuffix <suffix>", NamedTextColor.AQUA)
                .append(Component.text(" - Set your welcome suffix (used in join message)", NamedTextColor.GRAY)));
        player.sendMessage(Component.text("/welcome view", NamedTextColor.AQUA)
                .append(Component.text(" - View your current custom messages", NamedTextColor.GRAY)));
        player.sendMessage(Component.text("/welcome reset", NamedTextColor.AQUA)
                .append(Component.text(" - Reset to default messages", NamedTextColor.GRAY)));
    }
    
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("setprefix", "setsuffix", "view", "reset");
            List<String> completions = new ArrayList<>();
            
            for (String subCmd : subCommands) {
                if (subCmd.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subCmd);
                }
            }
            
            return completions;
        }
        
        return new ArrayList<>();
    }
}
