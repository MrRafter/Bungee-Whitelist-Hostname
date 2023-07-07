package me.MrRafter.Atlantismc;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

public class Main extends Plugin {
    public Configuration config;
    @Override
    public void onEnable() {
        Logger log = getLogger();

        log.info(ChatColor.AQUA + "Loading config...");
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getProxy().getPluginManager().registerListener(this, new me.MrRafter.Atlantismc.listeners.PlayerJoin(this));
        getProxy().getPluginManager().registerListener(this, new me.MrRafter.Atlantismc.listeners.ProxyPing(this));
        log.info(ChatColor.AQUA + "Plugin Activated Successfully");
    }
}
