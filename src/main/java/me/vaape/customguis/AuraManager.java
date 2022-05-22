package me.vaape.customguis;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.styles.DefaultStyles;
import dev.esophose.playerparticles.styles.ParticleStyle;
import dev.esophose.playerparticles.styles.ParticleStyleBlockBreak;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.Bed;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class AuraManager implements Listener {

	public CustomGUIs plugin;
	public PlayerParticlesAPI ppAPI;
	public FileConfiguration config;

	public ArrayList<UUID> playersInGUI = new ArrayList<>();
	public ArrayList<String> ambientAuraPerms = new ArrayList<>();
	public ArrayList<String> tpAuraPerms = new ArrayList<>();
	public ArrayList<String> blockAuraPerms = new ArrayList<>();

	public AuraManager(CustomGUIs passedPlugin) {
		this.plugin = passedPlugin;
		ppAPI = plugin.getPpAPI();
		config = plugin.getConfig();

		ambientAuraPerms.add("auras.aura.blood_wings");
		ambientAuraPerms.add("auras.aura.void_vortex");
		ambientAuraPerms.add("auras.aura.ophidian");
		ambientAuraPerms.add("auras.aura.flare");
		ambientAuraPerms.add("auras.aura.neptune's_grace");
		ambientAuraPerms.add("auras.aura.spark_halo");
		ambientAuraPerms.add("auras.aura.flower_path");
		ambientAuraPerms.add("auras.aura.warrior");
		ambientAuraPerms.add("auras.aura.repute");
		ambientAuraPerms.add("auras.aura.fire_cloud");
		ambientAuraPerms.add("auras.aura.ash_fall");

		tpAuraPerms.add("auras.aura.default_teleport_aura");
		tpAuraPerms.add("auras.aura.frost_helix");
		tpAuraPerms.add("auras.aura.rainbow_ring");

		blockAuraPerms.add("auras.aura.white_ash");
		blockAuraPerms.add("auras.aura.witch");
		blockAuraPerms.add("auras.aura.snowflake");
		blockAuraPerms.add("auras.aura.spore_blossom");
		blockAuraPerms.add("auras.aura.splash");
		blockAuraPerms.add("auras.aura.sizzle");
		blockAuraPerms.add("auras.aura.note");
		blockAuraPerms.add("auras.aura.nautilus");
		blockAuraPerms.add("auras.aura.mini_flame");
		blockAuraPerms.add("auras.aura.warped_spore");
		blockAuraPerms.add("auras.aura.spell");
		blockAuraPerms.add("auras.aura.lava");
		blockAuraPerms.add("auras.aura.reverse_portal");
		blockAuraPerms.add("auras.aura.blue_fire");
		blockAuraPerms.add("auras.aura.big_smoke");
		blockAuraPerms.add("auras.aura.totem");
		blockAuraPerms.add("auras.aura.sweep");
		blockAuraPerms.add("auras.aura.soul");
		blockAuraPerms.add("auras.aura.gleam");
		blockAuraPerms.add("auras.aura.pop");
		blockAuraPerms.add("auras.aura.rainbow");
		blockAuraPerms.add("auras.aura.golden");
		blockAuraPerms.add("auras.aura.thick_smoke");
		blockAuraPerms.add("auras.aura.cloud");
		blockAuraPerms.add("auras.aura.crimson_spore");
		blockAuraPerms.add("auras.aura.breath");
		blockAuraPerms.add("auras.aura.lava_drip");
		blockAuraPerms.add("auras.aura.purple_drip");
		blockAuraPerms.add("auras.aura.colors");
		blockAuraPerms.add("auras.aura.wizard");
		blockAuraPerms.add("auras.aura.sharp");
		blockAuraPerms.add("auras.aura.blemish");
		blockAuraPerms.add("auras.aura.firework");
		blockAuraPerms.add("auras.aura.fishing");
		blockAuraPerms.add("auras.aura.fire");
		blockAuraPerms.add("auras.aura.glow");
		blockAuraPerms.add("auras.aura.heart");
		blockAuraPerms.add("auras.aura.slime");
	}

	public void openAuraGUI(Player player) {
		Inventory hopperGUI = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.DARK_PURPLE + "Auras");
		playersInGUI.add(player.getUniqueId());

		ItemStack ambientItem = new ItemStack(Material.BLAZE_POWDER);
		ambientItem.addEnchantment(Enchantment.BINDING_CURSE, 1);
		ItemMeta ambientMeta = ambientItem.getItemMeta();
		ambientMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Ambient Auras");
		ambientMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ambientItem.setItemMeta(ambientMeta);


		ItemStack tpItem = new ItemStack(Material.ENDER_EYE);
		tpItem.addEnchantment(Enchantment.BINDING_CURSE, 1);
		ItemMeta tpMeta = tpItem.getItemMeta();
		tpMeta.setDisplayName(ChatColor.DARK_PURPLE + "Teleport Auras");
		tpMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		tpItem.setItemMeta(tpMeta);

		ItemStack breakItem = new ItemStack(Material.GOLDEN_PICKAXE);
		breakItem.addEnchantment(Enchantment.BINDING_CURSE, 1);
		ItemMeta breaktMeta = breakItem.getItemMeta();
		breaktMeta.setDisplayName(ChatColor.GOLD + "Block Break Auras");
		breaktMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ambientItem.setItemMeta(breaktMeta);

		hopperGUI.setItem(1, ambientItem);
		hopperGUI.setItem(2, tpItem);
		hopperGUI.setItem(3, breakItem);

		player.openInventory(hopperGUI);
	}

	//Returns a list of aura names that player has permissions to use ["Glacial Teleport Aura", "Easter Aura", "Default Teleport Aura"]
	public ArrayList<String> getOwnedTpAuras(Player player) {
		ArrayList<String> ownedPerms = new ArrayList<>();
		for (String permission : tpAuraPerms) {
			if (player.hasPermission(permission)) ownedPerms.add(permission);
		}

		ArrayList<String> ownedAuras = new ArrayList<>();
		for (String perm : ownedPerms) {
			String auraName = perm.split("\\.")[2];
			//Turns neptune's_grace into neptune's grace
			if (auraName.contains("_")) {
				auraName = auraName.replace("_", " ");
			}
			ownedAuras.add(proper(auraName));
		}

		return ownedAuras;
	}

	//Returns a list of aura names that player has permissions to use ["Glacial Teleport Aura", "Easter Aura", "Default Teleport Aura"]
	public ArrayList<String> getOwnedBlockAuras(Player player) {
		ArrayList<String> ownedPerms = new ArrayList<>();
		for (String permission : blockAuraPerms) {
			if (player.hasPermission(permission)) ownedPerms.add(permission);
		}

		ArrayList<String> ownedAuras = new ArrayList<>();
		for (String perm : ownedPerms) {
			String auraName = perm.split("\\.")[2];
			//Turns neptune's_grace into neptune's grace
			if (auraName.contains("_")) {
				auraName = auraName.replace("_", " ");
			}
			ownedAuras.add(proper(auraName));
		}

		return ownedAuras;
	}

	public ArrayList<String> getOwnedAuras(Player player, String type) {
		ArrayList<String> ownedPerms = new ArrayList<>();
		if (type.equals("tp")) {
			for (String permission : tpAuraPerms) {
				if (player.hasPermission(permission)) ownedPerms.add(permission);
			}
		}
		else if (type.equals("ambient")) {
			for (String permission : ambientAuraPerms) {
				if (player.hasPermission(permission)) ownedPerms.add(permission);
			}
		}
		else if (type.equals("break") || type.equals("place")) {
			for (String permission : blockAuraPerms) {
				if (player.hasPermission(permission)) ownedPerms.add(permission);
			}
		}

		ArrayList<String> ownedAuras = new ArrayList<>();
		for (String perm : ownedPerms) {
			String auraName = perm.split("\\.")[2];
			//Turns neptune's_grace into neptune's grace
			if (auraName.contains("_")) {
				auraName = auraName.replace("_", " ");
			}
			ownedAuras.add(proper(auraName));
		}

		return ownedAuras;
	}

	//Returns a list of aura names that player has permissions to use ["Glacial Teleport Aura", "Easter Aura", "Default Teleport Aura"]
	public ArrayList<String> getOwnedAmbientAuras(Player player) {
		ArrayList<String> ownedPerms = new ArrayList<>();
		for (String permission : ambientAuraPerms) {
			if (player.hasPermission(permission)) ownedPerms.add(permission);
		}

		ArrayList<String> ownedAuras = new ArrayList<>();
		for (String perm : ownedPerms) {
			String auraName = perm.split("\\.")[2];
			//Turns neptune's_grace into neptune's grace
			if (auraName.contains("_")) {
				auraName = auraName.replace("_", " ");
			}
			ownedAuras.add(proper(auraName));
		}

		return ownedAuras;
	}

	public String getAuraType(String auraName) {
		return switch (auraName) {
			case "Blood Wings",
					"Void Vortex",
					"Ophidian", "Flare",
					"Neptune's Grace",
					"Spark Halo",
					"Flower Path",
					"Warrior",
					"Repute",
					"Fire Cloud",
					"Ash Fall"
					-> "ambient";
			case "Default Teleport Aura",
					"Frost Helix",
					"Rainbow Ring"
					-> "tp";
			case "White Ash",
					"Witch",
					"Snowflake",
					"Spore Blossom",
					"Splash",
					"Sizzle",
					"Note",
					"Nautilus",
					"Mini Flame",
					"Warped Spore",
					"Spell",
					"Lava",
					"Reverse Portal",
					"Blue Fire",
					"Big Smoke",
					"Totem",
					"Sweep",
					"Soul",
					"Gleam",
					"Pop",
					"Rainbow",
					"Golden",
					"Thick Smoke",
					"Cloud",
					"Crimson Spore",
					"Breath",
					"Lava Drip",
					"Purple Drip",
					"Colors",
					"Sharp",
					"Wizard",
					"Blemish",
					"Firework",
					"Fishing",
					"Fire",
					"Glow",
					"Heart",
					"Slime"
					-> "block";
			default -> null;
		};

	}

	public ItemStack getAuraGUIItem(String auraName) {
		ItemStack auraGUIItem = null;
		if (getAuraType(auraName).equals("ambient")) {
			auraGUIItem = new ItemStack(Material.BLAZE_POWDER);
		}
		else if (getAuraType(auraName).equals("tp")) {
			auraGUIItem = new ItemStack(Material.ENDER_EYE);
		}
		else if (getAuraType(auraName).equals("block")) {
			auraGUIItem = new ItemStack(Material.AMETHYST_SHARD);
		}
		ItemMeta auraGUIItemMeta = auraGUIItem.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();

		//Set appropriate name and lore
		int tier = getAuraTier(auraName);
		if (tier == 0) {
			auraGUIItemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + auraName);
			lore.add(ChatColor.GOLD + "" + ChatColor.ITALIC + "Unique Aura");
		}
		else if (tier == 1) {
			auraGUIItemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + auraName);
			lore.add(ChatColor.YELLOW + "" + ChatColor.ITALIC + "Tier 1 Aura");
		}
		else if (tier == 2) {
			auraGUIItemMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + auraName);
			lore.add(ChatColor.AQUA + "" + ChatColor.ITALIC + "Tier 2 Aura");
		}
		else if (tier == 3) {
			auraGUIItemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + auraName);
			lore.add(ChatColor.GREEN + "" + ChatColor.ITALIC + "Tier 3 Aura");
		}
		auraGUIItemMeta.setLore(lore);
		auraGUIItem.setItemMeta(auraGUIItemMeta);
		return auraGUIItem;
	}

	public int getAuraTier(String auraName) {
		return switch (auraName) { //TP
			case "Blood Wings",
					"Void Vortex",
					"Ophidian",
					"Flare",
					"Default Teleport Aura",
					"Rainbow Ring",
					"Snowflake",
					"Mini Flame",
					"Lava",
					"Blue Fire",
					"Soul",
					"Gleam",
					"Rainbow",
					"Colors",
					"Blemish",
					"Firework",
					"Heart" -> 1;
			case "Neptune's Grace",
					"Spark Halo",
					"Frost Helix",
					"Flower Path",
					"White ash",
					"Witch",
					"Spore Blossom",
					"Note",
					"Warped Spore",
					"Spell",
					"Reverse portal",
					"Big smoke",
					"Totem",
					"Sweep",
					"Pop",
					"Thick smoke",
					"Cloud",
					"Crimson Spore",
					"Breath",
					"Fishing",
					"Glow" -> 2;
			case "Warrior", "Repute", "Fire Cloud", "Ash Fall",
			 		"White Ash",
			 		 "Splash",
			 		 "Sizzle",
			 		 "Nautilus",
			 		 "Reverse Portal",
			 		 "Big Smoke",
			 		 "Golden",
			 		 "Thick Smoke",
			 		 "Lava Drip",
			 		 "Purple Drip",
			 		 "Sharp",
			 		 "Wizard",
			 		 "Fire",
			 		 "Slime" -> 3;
			default -> 0;
		};
	}

	public ItemStack getPageButton(int page) {
		ItemStack button = new ItemStack(Material.ARROW);
		ItemMeta buttonMeta = button.getItemMeta();
		buttonMeta.setDisplayName(ChatColor.GREEN + "Page " + page);
		button.setItemMeta(buttonMeta);
		return button;
	}

	public ItemStack getRemoveAuraButton() {
		ItemStack button = new ItemStack(Material.REDSTONE);
		ItemMeta buttonMeta = button.getItemMeta();
		buttonMeta.setDisplayName(ChatColor.RED + "Turn off aura");
		button.setItemMeta(buttonMeta);
		return button;
	}

	public ItemStack getGoBackButton() {
		ItemStack button = new ItemStack(Material.ARROW);
		ItemMeta buttonMeta = button.getItemMeta();
		buttonMeta.setDisplayName(ChatColor.RED + "Go back");
		button.setItemMeta(buttonMeta);
		return button;
	}

	public Inventory loadChestButtons(Inventory chest, String guiType, int page, int maxPages) {
		if (page != 1) chest.setItem(18, getPageButton(page - 1));
		else chest.setItem(18, getGoBackButton());
		if (page != maxPages) chest.setItem(26, getPageButton(page + 1));
		if (guiType.equals("tp")) return chest;
		else chest.setItem(22, getRemoveAuraButton());
		return chest;
	}

	public int getPageNumberOnArrow(ItemStack arrow) {
		String page = arrow.getItemMeta().getDisplayName().split(" ")[1];
		if (page.equals("back")) {
			return 0;
		}
		return Integer.parseInt(page);
	}

	public String getAuraNameFromItem(ItemStack guiItem) {
		if (guiItem.getItemMeta().getDisplayName().contains("Turn off aura")) return "Off";
		if (guiItem.getType() == Material.ARROW) return "Arrow";
		return guiItem.getItemMeta().getDisplayName().substring(4);
	}

	//Capitalize first letter
	public String proper(String string) {
		return WordUtils.capitalize(string);
	}

	public void unenchantItems(Inventory inventory) {
		int inventorySize = inventory.getSize();
		for (int i = 0; i < (inventorySize - 1); i++) {
			ItemStack item = inventory.getItem(i);
			if (item == null) continue;
			item.removeEnchantment(Enchantment.BINDING_CURSE);
		}
	}

	public void addEnchantGlow(ItemStack item) {
		item.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
		item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	}

	public void makeSelectedAurasGlow(Player player, Inventory inventory, String type) {
		HashMap<String, String> activeAuras = getActiveAuras(player);
		ItemStack[] contents = inventory.getContents();

		for (ItemStack item : contents) {
			if (item == null) continue;
			if (getAuraNameFromItem(item).equals("Off") || getAuraNameFromItem(item).equals("Arrow")) continue;
			for (String auraKey : activeAuras.keySet()) {
				//Check if auraKey is the type we are looking for (check place, place1, place2, place3, or tp, tp1, tp2, tp3)
				if (!(auraKey.equals(type) || auraKey.equals(type + "1") || auraKey.equals(type + "2") || auraKey.equals(type + "3"))) continue;
				if (activeAuras.get(auraKey).contains(getAuraNameFromItem(item))) {
					addEnchantGlow(item);
				}
			}
		}
	}

	//Returns list of all active tp, ambient, break and place auras
	public HashMap<String, String> getActiveAuras(Player player) {
		String ambientAura = config.getString("auras." + player.getUniqueId() + ".ambient");
		String tpAura = config.getString("auras." + player.getUniqueId() + ".tp");
		String breakAura1 = config.getString("auras." + player.getUniqueId() + ".break1");
		String breakAura2 = config.getString("auras." + player.getUniqueId() + ".break2");
		String breakAura3 = config.getString("auras." + player.getUniqueId() + ".break3");
		String placeAura1 = config.getString("auras." + player.getUniqueId() + ".place1");
		String placeAura2 = config.getString("auras." + player.getUniqueId() + ".place2");
		String placeAura3 = config.getString("auras." + player.getUniqueId() + ".place3");
		HashMap<String, String> activeAuras = new HashMap<>();
		activeAuras.put("ambient", ambientAura);
		activeAuras.put("tp", tpAura);
		activeAuras.put("break1", breakAura1);
		activeAuras.put("break2", breakAura2);
		activeAuras.put("break3", breakAura3);
		activeAuras.put("place1", placeAura1);
		activeAuras.put("place2", placeAura2);
		activeAuras.put("place3", placeAura3);
		return activeAuras;
	}

	public void runDefaultTP(Player player, Location teleportLocation) {
		int defaultID = ppAPI.addActivePlayerParticle(player, ParticleEffect.PORTAL, DefaultStyles.TWINS).getId();
		int defaultToID = ppAPI.createFixedParticleEffect(Bukkit.getConsoleSender(), teleportLocation, ParticleEffect.PORTAL, DefaultStyles.TWINS).getId();

		Multimap<String, Integer> IDs = HashMultimap.create();
		IDs.put("player", defaultID);
		IDs.put("fixed", defaultToID);
		config.set("auras." + player.getUniqueId() + ".current tp particle IDs", IDs);
		plugin.saveConfig();

		startRemoveTPParticlesTimer(player);
	}

	public void runEasterTP(Player player, Location teleportLocation) {
		int dustID = ppAPI.addActivePlayerParticle(player, ParticleEffect.DUST, DefaultStyles.BEAM, OrdinaryColor.RANDOM).getId();
		int dustToID = ppAPI.createFixedParticleEffect(Bukkit.getConsoleSender(), teleportLocation, ParticleEffect.DUST, DefaultStyles.BEAM, OrdinaryColor.RANDOM).getId();


		Multimap<String, Integer> IDs = HashMultimap.create();
		IDs.put("player", dustID);
		IDs.put("fixed", dustToID);
		config.set("auras." + player.getUniqueId() + ".current tp particle IDs", IDs);
		plugin.saveConfig();

		startRemoveTPParticlesTimer(player);
	}

	public void runGlacialTP(Player player, Location teleportLocation) {

		int snowflakeID = ppAPI.addActivePlayerParticle(player, ParticleEffect.SNOWFLAKE, DefaultStyles.QUADHELIX).getId();
		int snowballID = ppAPI.addActivePlayerParticle(player, ParticleEffect.ITEM_SNOWBALL, DefaultStyles.QUADHELIX).getId();
		int snowflakeToID = ppAPI.createFixedParticleEffect(Bukkit.getConsoleSender(), teleportLocation, ParticleEffect.SNOWFLAKE, DefaultStyles.QUADHELIX).getId();
		int snowballToID = ppAPI.createFixedParticleEffect(Bukkit.getConsoleSender(), teleportLocation, ParticleEffect.ITEM_SNOWBALL, DefaultStyles.QUADHELIX).getId();
		Multimap<String, Integer> IDs = HashMultimap.create();
		IDs.put("player", snowflakeID);
		IDs.put("player", snowballID);
		IDs.put("fixed", snowflakeToID);
		IDs.put("fixed", snowballToID);
		config.set("auras." + player.getUniqueId() + ".current tp particle IDs", IDs);
		plugin.saveConfig();

		startRemoveTPParticlesTimer(player);
	}

	public void startRemoveTPParticlesTimer(Player player) {
		new BukkitRunnable() {
			@Override
			public void run() {
				removeTPParticles(player);
			}
		}.runTaskLater(plugin, 5 * 20);
	}

	public void removeTPParticles(Player player) {
		Multimap<String, Integer> tpParticleIDs = (Multimap<String, Integer>) config.get("auras." + player.getUniqueId() + ".current tp particle IDs");
		if (tpParticleIDs == null) return;
		for (String type : tpParticleIDs.keySet()) {
			for (int ID : tpParticleIDs.get(type)) {
				if (type.equals("player")) {
					if (ppAPI.getActivePlayerParticle(player, ID) != null) ppAPI.removeActivePlayerParticle(player, ID);
				}
				else {
					if (ppAPI.getFixedParticleEffect(Bukkit.getConsoleSender(), ID) != null) ppAPI.removeFixedEffect(Bukkit.getConsoleSender(), ID);
				}
			}
		}
		config.set("auras." + player.getUniqueId() + ".current tp particle IDs", null);
		plugin.saveConfig();
	}

	public void selectTPAura(Player player, String aura) {
		config.set("auras." + player.getUniqueId() + ".tp", aura);
		plugin.saveConfig();
	}

	public void selectAmbientAura(Player player, String aura) {
		removeActivePlayerParticles(player);
		config.set("auras." + player.getUniqueId() + ".ambient", aura);
		plugin.saveConfig();
	}

	//Returns true if was selected, false if not
	public boolean selectBlockAura(Player player, String aura, String type) {
		String aura1 = config.getString("auras." + player.getUniqueId() + "." + type + "1");
		String aura2 = config.getString("auras." + player.getUniqueId() + "." + type + "2");
		String aura3 = config.getString("auras." + player.getUniqueId() + "." + type + "3");

		if (aura.equals(aura1) || aura.equals(aura2) || aura.equals(aura3)) {
			deselectBlockAura(player, aura, type);
			return false;
		}

		if (player.hasPermission("auras.apply3blockauras")) {
			if (!aura1.equals("Off") && !aura2.equals("Off") && !aura3.equals("Off")) { //If all 3 are already selected
				player.sendMessage(ChatColor.RED + "You can only apply 3 block auras at a time.");
				return false;
			}
			else {
				if (aura1.equals("Off")) {
					config.set("auras." + player.getUniqueId() + "." + type + "1", aura);
					plugin.saveConfig();
					return true;
				}
				if (aura2.equals("Off")) {
					config.set("auras." + player.getUniqueId() + "." + type + "2", aura);
					plugin.saveConfig();
					return true;
				}
				if (aura3.equals("Off")) {
					config.set("auras." + player.getUniqueId() + "." + type + "3", aura);
					plugin.saveConfig();
					return true;
				}
			}
		}
		else {
			if (!aura1.equals("Off") && !aura2.equals("Off")) { //If both are selected
				player.sendMessage(ChatColor.RED + "You must be at least Emerald rank to select 3 block auras.");
				return false;
			}
			else {
				if (aura1.equals("Off")) {
					config.set("auras." + player.getUniqueId() + "." + type + "1", aura);
					plugin.saveConfig();
					return true;
				}
				if (aura2.equals("Off")) {
					config.set("auras." + player.getUniqueId() + "." + type + "2", aura);
					plugin.saveConfig();
					return true;
				}
			}
		}
		return false;
	}

	public boolean deselectBlockAura(Player player, String aura, String type) {
		if (aura.equals("Off")) return false;

		String aura1 = config.getString("auras." + player.getUniqueId() + "." + type + "1");
		String aura2 = config.getString("auras." + player.getUniqueId() + "." + type + "2");
		String aura3 = config.getString("auras." + player.getUniqueId() + "." + type + "3");

		if (aura.equals(aura1)) {
			config.set("auras." + player.getUniqueId() + "." + type + "1", "Off");
			plugin.saveConfig();
			return true;
		}
		if (aura.equals(aura2)) {
			config.set("auras." + player.getUniqueId() + "." + type + "2", "Off");
			plugin.saveConfig();
			return true;
		}
		if (aura.equals(aura3)) {
			config.set("auras." + player.getUniqueId() + "." + type + "3", "Off");
			plugin.saveConfig();
			return true;
		}
		return false;
	}

	public void turnOffBreakAuras(Player player) {
		config.set("auras." + player.getUniqueId() + ".break1", "Off");
		config.set("auras." + player.getUniqueId() + ".break2", "Off");
		config.set("auras." + player.getUniqueId() + ".break3", "Off");
		plugin.saveConfig();
		ppAPI.removeActivePlayerParticles(player, DefaultStyles.BLOCKBREAK);
	}

	public void turnOffPlaceAuras(Player player) {
		config.set("auras." + player.getUniqueId() + ".place1", "Off");
		config.set("auras." + player.getUniqueId() + ".place2", "Off");
		config.set("auras." + player.getUniqueId() + ".place3", "Off");
		plugin.saveConfig();
		ppAPI.removeActivePlayerParticles(player, DefaultStyles.BLOCKPLACE);
	}

	public boolean isBreakAura(ParticlePair particle) {
		if (particle.getStyle() == DefaultStyles.BLOCKBREAK) return true;
		return false;
	}

	public void activateBlockAura(Player player, String auraName, String type) {
		ParticleStyle style = null;
		if (type.equals("break")) style = DefaultStyles.BLOCKBREAK;
		if (type.equals("place")) style = DefaultStyles.BLOCKPLACE;
		switch (auraName) {
			case "Off":
				removeBlockBreakParticles(player);
				break;
			case "White Ash":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.WHITE_ASH, style);
				break;
			case "Witch":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.WITCH, style);
				break;
			case "Snowflake":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.SNOWFLAKE, style);
				break;
			case "Spore Blossom":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.SPORE_BLOSSOM_AIR, style);
				break;
			case "Splash":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.SPLASH, style);
				break;
			case "Sizzle":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.SMOKE, style);
				break;
			case "Note":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.NOTE, style);
				break;
			case "Nautilus":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.NAUTILUS, style);
				break;
			case "Mini Flame":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.SMALL_FLAME, style);
				break;
			case "Warped Spore":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.WARPED_SPORE, style);
				break;
			case "Spell":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.SPELL, style);
				break;
			case "Lava":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.LAVA, style);
				break;
			case "Reverse Portal":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.REVERSE_PORTAL, style);
				break;
			case "Blue Fire":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.SOUL_FIRE_FLAME, style);
				break;
			case "Big Smoke":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.LARGE_SMOKE, style);
				break;
			case "Totem":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.TOTEM_OF_UNDYING, style);
				break;
			case "Sweep":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.SWEEP_ATTACK, style);
				break;
			case "Soul":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.SOUL, style);
				break;
			case "Gleam":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.END_ROD, style);
				break;
			case "Pop":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.BUBBLE_POP, style);
				break;
			case "Rainbow":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.ENTITY_EFFECT, style, OrdinaryColor.RAINBOW);
			break;
			case "Golden":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.BLOCK, style, Material.GOLD_BLOCK);
				break;
			case "Thick Smoke":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.CAMPFIRE_COSY_SMOKE, style);
				break;
			case "Cloud":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.CLOUD, style);
				break;
			case "Crimson Spore":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.CRIMSON_SPORE, style);
				break;
			case "Breath":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.DRAGON_BREATH, style);
				break;
			case "Lava Drip":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.FALLING_DRIPSTONE_LAVA, style);
				break;
			case "Purple Drip":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.DRIPPING_OBSIDIAN_TEAR, style);
				break;
			case "Colors":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.DUST, style, OrdinaryColor.RAINBOW);
			break;
			case "Sharp":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.ENCHANTED_HIT, style);
				break;
			case "Wizard":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.ENCHANT, style);
				break;
			case "Blemish":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.END_ROD, style);
				break;
			case "Firework":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.FIREWORK, style);
				break;
			case "Fishing":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.FISHING, style);
				break;
			case "Fire":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.FLAME, style);
				break;
			case "Glow":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.GLOW, style);
				break;
			case "Heart":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.HEART, style);
				break;
			case "Slime":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.ITEM_SLIME, style);
				break;
			default:
				break;
		}
	}

	public void deactivateBlockAura(Player player, String auraName, String type) {
		Collection<ParticlePair> playerParticlePairs = ppAPI.getActivePlayerParticles(player);
		for (ParticlePair particlePair : playerParticlePairs) {
			ParticleStyle style = particlePair.getStyle();
			ParticleEffect effect = particlePair.getEffect();
			if (type.equals("break") && style != DefaultStyles.BLOCKBREAK) continue;
			if (type.equals("place") && style != DefaultStyles.BLOCKPLACE) continue;
			switch (auraName) {
				case "White Ash":
					if (effect == ParticleEffect.WHITE_ASH) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Witch":
					if (effect == ParticleEffect.WITCH) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Snowflake":
					if (effect == ParticleEffect.SNOWFLAKE) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Spore Blossom":
					if (effect == ParticleEffect.SPORE_BLOSSOM_AIR) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Splash":
					if (effect == ParticleEffect.SPLASH) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Sizzle":
					if (effect == ParticleEffect.SMOKE) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Note":
					if (effect == ParticleEffect.NOTE) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Nautilus":
					if (effect == ParticleEffect.NAUTILUS) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Mini Flame":
					if (effect == ParticleEffect.SMALL_FLAME) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Warped Spore":
					if (effect == ParticleEffect.WARPED_SPORE) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Spell":
					if (effect == ParticleEffect.SPELL) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Lava":
					if (effect == ParticleEffect.LAVA) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Reverse Portal":
					if (effect == ParticleEffect.REVERSE_PORTAL) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Blue Fire":
					if (effect == ParticleEffect.SOUL_FIRE_FLAME) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Big Smoke":
					if (effect == ParticleEffect.LARGE_SMOKE) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Totem":
					if (effect == ParticleEffect.TOTEM_OF_UNDYING) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Sweep":
					if (effect == ParticleEffect.SWEEP_ATTACK) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Soul":
					if (effect == ParticleEffect.SOUL) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Gleam":
					if (effect == ParticleEffect.HAPPY_VILLAGER) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Pop":
					if (effect == ParticleEffect.BUBBLE_POP) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Rainbow":
					if (effect == ParticleEffect.ENTITY_EFFECT) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Golden":
					if (effect == ParticleEffect.BLOCK) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Thick Smoke":
					if (effect == ParticleEffect.CAMPFIRE_COSY_SMOKE) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Cloud":
					if (effect == ParticleEffect.CLOUD) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Crimson Spore":
					if (effect == ParticleEffect.CRIMSON_SPORE) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Breath":
					if (effect == ParticleEffect.DRAGON_BREATH) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Lava Drip":
					if (effect == ParticleEffect.FALLING_DRIPSTONE_LAVA) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Purple Drip":
					if (effect == ParticleEffect.DRIPPING_OBSIDIAN_TEAR) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Colors":
					if (effect == ParticleEffect.DUST) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Sharp":
					if (effect == ParticleEffect.ENCHANTED_HIT) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Wizard":
					if (effect == ParticleEffect.ENCHANT) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Blemish":
					if (effect == ParticleEffect.END_ROD) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Firework":
					if (effect == ParticleEffect.FIREWORK) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Fishing":
					if (effect == ParticleEffect.FISHING) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Fire":
					if (effect == ParticleEffect.FLAME) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Glow":
					if (effect == ParticleEffect.GLOW) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Heart":
					if (effect == ParticleEffect.HEART) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;
				case "Slime":
					if (effect == ParticleEffect.ITEM) ppAPI.removeActivePlayerParticle(player, particlePair.getId());
					break;


				default:
					break;
			}
		}
	}

	//Removes all active player particles except block break
	public void removeActivePlayerParticles(Player player) {
		for (ParticlePair pair : ppAPI.getActivePlayerParticles(player)) {
			if (pair.getStyle() instanceof ParticleStyleBlockBreak) continue;
			ppAPI.removeActivePlayerParticles(player, pair.getStyle());
		}
	}

	public void removeBlockBreakParticles(Player player) {
		for (ParticlePair pair : ppAPI.getActivePlayerParticles(player)) {
			if (!(pair.getStyle() instanceof ParticleStyleBlockBreak)) continue;
			ppAPI.removeActivePlayerParticles(player, pair.getStyle());
		}
	}

	public String getActiveTPAura(Player player) {
		return config.getString("auras." + player.getUniqueId() + ".tp");
	}

	public void runActiveTPAnimation(Player player, Location teleportLocation) {
		String auraName = getActiveTPAura(player);

		switch (auraName) {
			case "Default Teleport Aura": {
				runDefaultTP(player, teleportLocation);
				break;
			}

			case "Frost Helix": {
				runGlacialTP(player, teleportLocation);
				break;
			}
			case "Rainbow Ring":
				runEasterTP(player, teleportLocation);
				break;
		}
	}

	public void activateAmbientAura(Player player, String auraName) {
		for (ParticlePair particlePair : ppAPI.getActivePlayerParticles(player)) {
			if (particlePair.getStyle() == DefaultStyles.BLOCKBREAK) continue;
			ppAPI.removeActivePlayerParticles(player, particlePair.getStyle());
		}
		switch (auraName) {
			case "Blood Wings":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.ITEM, DefaultStyles.WINGS, Material.FERMENTED_SPIDER_EYE);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.DUST, DefaultStyles.WINGS, new OrdinaryColor(255, 0, 0));
				break;
			case "Void Vortex":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.PORTAL, DefaultStyles.WHIRLWIND);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.REVERSE_PORTAL, DefaultStyles.WHIRLWIND);
				break;
			case "Ophidian":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.FALLING_SPORE_BLOSSOM, DefaultStyles.COMPANION);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.DUST, DefaultStyles.COMPANION, new OrdinaryColor(50, 205, 50));
				break;
			case "Flare":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.FLAME, DefaultStyles.WHIRL);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.FLAME, DefaultStyles.FEET);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.SMALL_FLAME, DefaultStyles.WHIRL);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.SMALL_FLAME, DefaultStyles.WHIRLWIND);
				break;
			case "Neptune's Grace":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.BUBBLE, DefaultStyles.ORBIT);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.BUBBLE_COLUMN_UP, DefaultStyles.RINGS);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.BUBBLE_POP, DefaultStyles.RINGS);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.BUBBLE_POP, DefaultStyles.ORBIT);
				break;
			case "Spark Halo":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.ELECTRIC_SPARK, DefaultStyles.HALO);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.ENCHANT, DefaultStyles.HALO);
				break;
			case "Flower Path":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.BLOCK, DefaultStyles.WHIRLWIND, Material.ACACIA_LEAVES);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.BLOCK, DefaultStyles.WHIRLWIND, Material.AZURE_BLUET);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.BLOCK, DefaultStyles.WHIRLWIND, Material.DANDELION);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.BLOCK, DefaultStyles.WHIRLWIND, Material.BLUE_ORCHID);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.BLOCK, DefaultStyles.WHIRLWIND, Material.POPPY);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.BLOCK, DefaultStyles.WHIRLWIND, Material.CORNFLOWER);
				break;
			case "Warrior":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.CRIT, DefaultStyles.PULSE);
				break;
			case "Repute":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.HAPPY_VILLAGER, DefaultStyles.FEET);
				break;
			case "Fire Cloud":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.CAMPFIRE_COSY_SMOKE, DefaultStyles.OVERHEAD);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.FLAME, DefaultStyles.OVERHEAD);
				break;
			case "Ash Fall":
				ppAPI.addActivePlayerParticle(player, ParticleEffect.ASH, DefaultStyles.OVERHEAD);
				ppAPI.addActivePlayerParticle(player, ParticleEffect.ENTITY_EFFECT, DefaultStyles.OVERHEAD, new OrdinaryColor(0, 0, 0));
				break;
			default:
				break;
		}
	}
}