package me.MrRafter.Atlantismc.listeners;

import me.MrRafter.Atlantismc.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PlayerJoin implements Listener {
    private final Main plugin;

    public PlayerJoin(Main plugin) {
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PreLoginEvent event) {
        String hostname = event.getConnection().getVirtualHost().getHostName();
        if (!plugin.config.getStringList("whitelist-hosts").contains(hostname)) {
            String message = ChatColor.translateAlternateColorCodes('&', plugin.config.getString("disconnect-message"));

            if (plugin.config.getBoolean("log")) {
                String mensajePrevenido = plugin.config.getString("log.mensaje-log");
                String playerName = event.getConnection().getName();
                String replacedText = mensajePrevenido.replace("%player%", playerName);
                System.out.println(replacedText);

                TextComponent component = new TextComponent(message);
                event.getConnection().disconnect(component);
            }
        }
    }
}
