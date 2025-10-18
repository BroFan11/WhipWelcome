package me.brofan11.whipwelcome.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {
    
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern HEX_PATTERN_NO_AMPERSAND = Pattern.compile("#([A-Fa-f0-9]{6})");
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    
    /**
     * Translates color codes and converts them to MiniMessage format
     * Supports:
     * - Legacy color codes (&a, &f, &l, etc.)
     * - Hex codes (&#ff00ff or #ff00ff)
     * - MiniMessage format (<red>, <gradient>, etc.)
     * 
     * @param text The text to translate
     * @return Component with all colors applied
     */
    public static Component translateColors(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }
        
        // First, convert hex codes to MiniMessage format
        text = translateHexCodes(text);
        
        // Then convert legacy codes to MiniMessage format
        text = translateLegacyCodes(text);
        
        // Finally, parse as MiniMessage
        try {
            return MINI_MESSAGE.deserialize(text);
        } catch (Exception e) {
            // If MiniMessage parsing fails, try legacy serializer as fallback
            return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
        }
    }
    
    /**
     * Converts hex color codes to MiniMessage format
     * &#ff00ff or #ff00ff -> <color:#ff00ff>
     */
    private static String translateHexCodes(String text) {
        // Handle &#RRGGBB format
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String hexColor = matcher.group(1);
            matcher.appendReplacement(sb, "<color:#" + hexColor + ">");
        }
        matcher.appendTail(sb);
        text = sb.toString();
        
        // Handle #RRGGBB format (without &)
        matcher = HEX_PATTERN_NO_AMPERSAND.matcher(text);
        sb = new StringBuffer();
        
        while (matcher.find()) {
            String hexColor = matcher.group(1);
            // Only replace if not already in <color:#...> format
            if (!text.substring(Math.max(0, matcher.start() - 7), matcher.start()).equals("<color:")) {
                matcher.appendReplacement(sb, "<color:#" + hexColor + ">");
            } else {
                matcher.appendReplacement(sb, matcher.group(0));
            }
        }
        matcher.appendTail(sb);
        
        return sb.toString();
    }
    
    /**
     * Converts legacy color codes to MiniMessage format
     * &a -> <green>, &l -> <bold>, etc.
     */
    private static String translateLegacyCodes(String text) {
        // Replace legacy color codes with MiniMessage equivalents
        text = text.replace("&0", "<black>");
        text = text.replace("&1", "<dark_blue>");
        text = text.replace("&2", "<dark_green>");
        text = text.replace("&3", "<dark_aqua>");
        text = text.replace("&4", "<dark_red>");
        text = text.replace("&5", "<dark_purple>");
        text = text.replace("&6", "<gold>");
        text = text.replace("&7", "<gray>");
        text = text.replace("&8", "<dark_gray>");
        text = text.replace("&9", "<blue>");
        text = text.replace("&a", "<green>");
        text = text.replace("&b", "<aqua>");
        text = text.replace("&c", "<red>");
        text = text.replace("&d", "<light_purple>");
        text = text.replace("&e", "<yellow>");
        text = text.replace("&f", "<white>");
        
        // Format codes
        text = text.replace("&k", "<obfuscated>");
        text = text.replace("&l", "<bold>");
        text = text.replace("&m", "<strikethrough>");
        text = text.replace("&n", "<underlined>");
        text = text.replace("&o", "<italic>");
        text = text.replace("&r", "<reset>");
        
        return text;
    }
    
    /**
     * Strips all color codes from text
     */
    public static String stripColors(String text) {
        if (text == null) {
            return "";
        }
        
        // Remove hex codes
        text = HEX_PATTERN.matcher(text).replaceAll("");
        text = HEX_PATTERN_NO_AMPERSAND.matcher(text).replaceAll("");
        
        // Remove legacy codes
        text = text.replaceAll("&[0-9a-fk-or]", "");
        
        // Remove MiniMessage tags
        text = text.replaceAll("<[^>]+>", "");
        
        return text;
    }
}
