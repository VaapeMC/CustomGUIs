package me.vaape.customguis;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import net.ess3.api.events.teleport.TeleportWarmupEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.UUID;

public class TeleportManager implements Listener {

	public CustomGUIs plugin;
	public AuraManager auraManager;
	public PlayerParticlesAPI ppAPI;
	public FileConfiguration config;

	public ArrayList<UUID> playersTeleporting = new ArrayList<>();

	public TeleportManager(CustomGUIs passedPlugin) {
		this.plugin = passedPlugin;
		ppAPI = plugin.getPpAPI();
		config = plugin.getConfig();
		auraManager = plugin.getAuraManager();
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		ArrayList<String> tpCommands = new ArrayList<String>();
		tpCommands.add("/wild");
		tpCommands.add("/g home");
		tpCommands.add("/g h");

		for (String command : tpCommands) {

			if (event.getMessage().startsWith(command)) {
				startTeleporting(event.getPlayer(), new Location(Bukkit.getWorld("world_the_end"), 0, 63, 9));
				return;
			}
		}
	}

	@EventHandler
	public void onTeleport(TeleportWarmupEvent event) {
		startTeleporting(event.getTeleportee().getBase().getPlayer(), event.getTarget().getLocation());
	}

	public void startTeleporting(Player player, Location targetLocation) {
		if (player.hasPermission("customguis.bypasstp") && player.hasPotionEffect(PotionEffectType.INVISIBILITY)) return;
		playersTeleporting.add(player.getUniqueId());
		auraManager.removeTPParticles(player);
		auraManager.runActiveTPAnimation(player, targetLocation);
	}

	@EventHandler
	public void onDisconnect (PlayerQuitEvent event) {
		auraManager.removeTPParticles(event.getPlayer());
		playersTeleporting.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (!playersTeleporting.contains(event.getPlayer().getUniqueId())) return;
		Location from = event.getFrom();
		Location to = event.getTo();
		if ((Math.round(from.getX() * 100) / 100) == (Math.round(to.getX() * 100) /100) && //they can move 0.001 blocks
			(Math.round(from.getY() * 100) / 100) == (Math.round(to.getY() * 100) / 100) &&
			(Math.round(from.getZ() * 100) / 100) == (Math.round(to.getZ() * 100) / 100)) {
			return;
		}
		Player player = event.getPlayer();
		auraManager.removeTPParticles(player);
		playersTeleporting.remove(player.getUniqueId());
	}
}