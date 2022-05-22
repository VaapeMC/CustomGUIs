package me.vaape.customguis;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class CustomGUIs extends JavaPlugin{

    private final FileConfiguration config = this.getConfig();

    public CustomGUIs instance;
    public CustomGUIs getInstance() {
        return instance;
    }

    public PlayerParticlesAPI ppAPI;
    public PlayerParticlesAPI getPpAPI() { return ppAPI; }

    public AuraManager auraManager;
    public AuraManager getAuraManager() { return auraManager; }

    public void onEnable() {
        loadConfiguration();

        if (Bukkit.getPluginManager().isPluginEnabled("PlayerParticles")) {
            this.ppAPI = PlayerParticlesAPI.getInstance();
        }

        auraManager = new AuraManager(this);

        instance = this;
        AuraGUI auraGUI = new AuraGUI(getInstance());
        TeleportManager teleportManager = new TeleportManager(getInstance());
        instance.getCommand("defaultTP").setExecutor(auraGUI);
        instance.getCommand("easterTP").setExecutor(auraGUI);
        instance.getCommand("glacialTP").setExecutor(auraGUI);
        instance.getCommand("activeTP").setExecutor(auraGUI);
        instance.getCommand("aura").setExecutor(auraGUI);
        this.getServer().getPluginManager().registerEvents(auraGUI, instance);
        this.getServer().getPluginManager().registerEvents(teleportManager, instance);

        getLogger().info(ChatColor.GREEN + "CustomGUIs has been enabled!");

    }

    public void loadConfiguration() {
        config.set(("time of server start"), new Date());
        config.options().copyDefaults(true);
        saveConfig();
    }

    public void onDisable() {
        saveConfig();
        instance = null;
    }

    @EventHandler
    public void onFirstJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (config.get("auras." + player.getUniqueId() + ".ambient") == null);

        config.set("auras." + player.getUniqueId() + ".tp", "Default Teleport Aura");
        config.set("auras." + player.getUniqueId() + ".ambient", "Off");
        config.set("auras." + player.getUniqueId() + ".break1", "Off");
        config.set("auras." + player.getUniqueId() + ".break2", "Off");
        config.set("auras." + player.getUniqueId() + ".break3", "Off");
        config.set("auras." + player.getUniqueId() + ".place1", "Off");
        config.set("auras." + player.getUniqueId() + ".place2", "Off");
        config.set("auras." + player.getUniqueId() + ".place3", "Off");
        saveConfig();
    }
}