package me.MrRafter.Atlantismc.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import me.MrRafter.Atlantismc.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ProxyPing implements Listener {
    private final Main plugin;

    public ProxyPing(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPing(ProxyPingEvent event) {
        String hostname = event.getConnection().getVirtualHost().getHostName();
        if (!plugin.config.getStringList("whitelist-hosts").contains(hostname)) {
            try {
                String faviconURL = plugin.config.getString("favicon-url");
                if (faviconURL != null && !faviconURL.isEmpty()) {
                    URL url;
                    try {
                        url = new URL(faviconURL);
                    } catch (Exception e) {
                        plugin.getLogger().warning("Invalid favicon URL: " + faviconURL);
                        return;
                    }

                    InputStream stream = null;
                    try {
                        stream = url.openStream();
                        BufferedImage image = ImageIO.read(stream);
                        Favicon favicon = Favicon.create(image);

                        ServerPing newResponse = new ServerPing();
                        String customMessage = ChatColor.translateAlternateColorCodes('&', plugin.config.getString("motd-message"));
                        TextComponent description = new TextComponent(customMessage);
                        String protocolName = plugin.config.getString("protocol.name");
                        int protocolVersion = plugin.config.getInt("protocol.version");
                        ServerPing.Protocol protocol = new ServerPing.Protocol(protocolName, protocolVersion);
                        newResponse.setFavicon(favicon);
                        newResponse.setVersion(protocol);
                        newResponse.setDescriptionComponent(description);
                        newResponse.setPlayers(event.getResponse().getPlayers());

                        event.setResponse(newResponse);
                    } finally {
                        if (stream != null) {
                            stream.close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
