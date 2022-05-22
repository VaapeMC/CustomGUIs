package me.vaape.customguis;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuraGUI implements Listener, CommandExecutor {

	public CustomGUIs plugin;
	public AuraManager auraManager;
	public PlayerParticlesAPI ppAPI;
	public FileConfiguration config;

	public ArrayList<UUID> playersInGUI = new ArrayList<>();

	public AuraGUI(CustomGUIs passedPlugin) {
		this.plugin = passedPlugin;
		ppAPI = plugin.getPpAPI();
		config = plugin.getConfig();
		auraManager = plugin.getAuraManager();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player player = (Player) sender;

		Location loc = new Location(Bukkit.getWorld("world"), 0, 150, 0);

		if (cmd.getName().equalsIgnoreCase("defaultTP")) {
			if (!player.hasPermission("customguis.defaulttp")) { player.sendMessage(ChatColor.RED + "You do not have permission to do that."); return false; }
			auraManager.runDefaultTP(player, loc); return true; }
		if (cmd.getName().equalsIgnoreCase("easterTP")) {
			if (!player.hasPermission("customguis.eastertp")) { player.sendMessage(ChatColor.RED + "You do not have permission to do that."); return false; }
			auraManager.runEasterTP(player, loc); return true; }
		if (cmd.getName().equalsIgnoreCase("glacialTP")) {
			if (!player.hasPermission("customguis.glacialtp")) { player.sendMessage(ChatColor.RED + "You do not have permission to do that."); return false; }
			auraManager.runGlacialTP(player, loc); return true; }
		if (cmd.getName().equalsIgnoreCase("activeTP")) {
			if (!player.hasPermission("customguis.activetp")) { player.sendMessage(ChatColor.RED + "You do not have permission to do that."); return false; }
			auraManager.runActiveTPAnimation(player, loc); return true; }
		if (cmd.getName().equalsIgnoreCase("aura")) { openAuraGUI(player); return true; }

		return true;

	}

	public void openAuraGUI(Player player) {
		//player.closeInventory();

		Inventory hopperGUI = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Auras");
		playersInGUI.add(player.getUniqueId());

		ItemStack ambientItem = new ItemStack(Material.BLAZE_POWDER);
		ambientItem.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
		ItemMeta ambientMeta = ambientItem.getItemMeta();
		ambientMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Ambient Auras");
		ambientMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ambientItem.setItemMeta(ambientMeta);


		ItemStack tpItem = new ItemStack(Material.ENDER_EYE);
		tpItem.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
		ItemMeta tpMeta = tpItem.getItemMeta();
		tpMeta.setDisplayName(ChatColor.DARK_PURPLE + "Teleport Auras");
		tpMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		tpItem.setItemMeta(tpMeta);

		ItemStack breakItem = new ItemStack(Material.DIAMOND_PICKAXE);
		breakItem.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
		ItemMeta breaktMeta = breakItem.getItemMeta();
		breaktMeta.setDisplayName(ChatColor.GOLD + "Block Auras");
		breaktMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		breakItem.setItemMeta(breaktMeta);

		hopperGUI.setItem(1, ambientItem);
		hopperGUI.setItem(2, tpItem);
		hopperGUI.setItem(3, breakItem);

		player.openInventory(hopperGUI);
	}

	public void openBlockAuraGUI(Player player) {
		playersInGUI.add(player.getUniqueId());

		Inventory hopperGUI = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.GOLD + "" + ChatColor.BOLD + "Block Auras");

		ItemStack blockBreakItem = new ItemStack(Material.GOLDEN_PICKAXE);
		blockBreakItem.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
		ItemMeta blockBreakMeta = blockBreakItem.getItemMeta();
		blockBreakMeta.setDisplayName(ChatColor.GOLD + "Block Break Auras");
		blockBreakMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		blockBreakItem.setItemMeta(blockBreakMeta);

		ItemStack blockPlaceItem = new ItemStack(Material.GRASS_BLOCK);
		blockPlaceItem.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
		ItemMeta blockPlaceMeta = blockPlaceItem.getItemMeta();
		blockPlaceMeta.setDisplayName(ChatColor.YELLOW + "Block Place Auras");
		blockPlaceMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		blockPlaceItem.setItemMeta(blockPlaceMeta);

		hopperGUI.setItem(0, auraManager.getGoBackButton());
		hopperGUI.setItem(2, blockBreakItem);
		hopperGUI.setItem(3, blockPlaceItem);

		player.openInventory(hopperGUI);
	}

	public void openTPAuraGUI(Player player, int page) {
		//player.closeInventory();

		if (page == 0) { openAuraGUI(player); return; }

		Inventory chestGUI = Bukkit.createInventory(null, 27, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Teleport Auras");
		playersInGUI.add(player.getUniqueId());

		//Load auras
		ArrayList<String> ownedAuras = auraManager.getOwnedTpAuras(player);
		List<ItemStack> auraGUIItems = new ArrayList<>();
		String activeAura = config.getString("auras." + player.getUniqueId() + ".tp");

		for (String aura : ownedAuras) {
			ItemStack auraGUIItem = auraManager.getAuraGUIItem(aura);
			if (aura.equals(activeAura)) {
				auraManager.addEnchantGlow(auraGUIItem);
			}
			auraGUIItems.add(auraGUIItem);
		}

		//Load buttons in chest
		int ownedAurasSize = ownedAuras.size();
		int maxPages = ((int) (ownedAurasSize / 18)) + 1;
		chestGUI = auraManager.loadChestButtons(chestGUI, "tp", page, maxPages);

		//Load Aura items in chest
		int start = (page - 1) * 18;
		int end = start + 17;

		for (int i = start; i <= end && i < auraGUIItems.size(); i++) {
			chestGUI.addItem(auraGUIItems.get(i));
		}

		player.openInventory(chestGUI);

	}

	public void openBlockGUI(Player player, int page, String type) {
		//player.closeInventory();

		if (page == 0) { openAuraGUI(player); return; }

		Inventory chestGUI = Bukkit.createInventory(null, 27, ChatColor.GOLD + "" + ChatColor.BOLD + "Block " + auraManager.proper(type) + " Auras");
		playersInGUI.add(player.getUniqueId());

		//Load auras
		ArrayList<String> ownedAuras = auraManager.getOwnedBlockAuras(player);
		List<ItemStack> auraGUIItems = new ArrayList<>();
		String aura1 = config.getString("auras." + player.getUniqueId() + "." + type + "1");
		String aura2 = config.getString("auras." + player.getUniqueId() + "." + type + "2");
		String aura3 = config.getString("auras." + player.getUniqueId() + "." + type + "3");

		for (String aura : ownedAuras) {
			ItemStack auraGUIItem = auraManager.getAuraGUIItem(aura);
			auraGUIItems.add(auraGUIItem);
		}

		//Load buttons in chest
		int ownedAurasSize = ownedAuras.size();
		int maxPages = ((int) (ownedAurasSize / 18)) + 1;
		chestGUI = auraManager.loadChestButtons(chestGUI, type, page, maxPages);

		//If ambient aura is Off, enchant removeAuraButton
		if (aura1.equals("Off") && aura2.equals("Off") && aura3.equals("Off")) {
			ItemStack removeAuraButton = chestGUI.getItem(22);
			auraManager.addEnchantGlow(removeAuraButton);
		}

		//Load Aura items in chest
		int start = (page - 1) * 18;
		int end = start + 17;

		for (int i = start; i <= end && i < auraGUIItems.size(); i++) {
			chestGUI.addItem(auraGUIItems.get(i));
		}
		auraManager.makeSelectedAurasGlow(player, chestGUI, type);

		player.openInventory(chestGUI);

	}

	public void openAmbientAuraGUI(Player player, int page) {
		//player.closeInventory();

		if (page == 0) { openAuraGUI(player); return; }

		Inventory chestGUI = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Ambient Auras");
		playersInGUI.add(player.getUniqueId());

		//Load auras
		ArrayList<String> ownedAuras = auraManager.getOwnedAmbientAuras(player);
		List<ItemStack> auraGUIItems = new ArrayList<>();
		String activeAura = config.getString("auras." + player.getUniqueId() + ".ambient");
		;

		for (String aura : ownedAuras) {
			ItemStack auraGUIItem = auraManager.getAuraGUIItem(aura);
			if (aura.equals(activeAura)) {
				auraManager.addEnchantGlow(auraGUIItem);
			}
			auraGUIItems.add(auraGUIItem);
		}

		//Load buttons in chest
		int ownedAurasSize = ownedAuras.size();
		int maxPages = ((int) (ownedAurasSize / 18)) + 1;
		chestGUI = auraManager.loadChestButtons(chestGUI, "ambient", page, maxPages);

		//If ambient aura is Off, enchant removeAuraButton
		if (activeAura.equals("Off")) {
			ItemStack removeAuraButton = chestGUI.getItem(22);
			auraManager.addEnchantGlow(removeAuraButton);
		}

		//Load Aura items in chest
		int start = (page - 1) * 18;
		int end = start + 17;

		for (int i = start; i <= end && i < auraGUIItems.size(); i++) {
			chestGUI.addItem(auraGUIItems.get(i));
		}

		player.openInventory(chestGUI);

	}

	@EventHandler
	public void onInventoryClick (InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player player)) return;
		if (!playersInGUI.contains(player.getUniqueId())) return;

		event.setCancelled(true);

		ItemStack clickedItem = event.getCurrentItem();
		if (clickedItem == null) return;

		String title = event.getView().getTitle();

		//Main menu
		if (event.getClickedInventory().getType() == InventoryType.HOPPER) {
			if (clickedItem.getType() == Material.BLAZE_POWDER) {
				openAmbientAuraGUI(player, 1);
			}
			else if (clickedItem.getType() == Material.ENDER_EYE) {
				openTPAuraGUI(player, 1);
			}
			else if (clickedItem.getType() == Material.DIAMOND_PICKAXE) {
				openBlockAuraGUI(player);
			}
			else if (clickedItem.getType() == Material.GOLDEN_PICKAXE) {
				openBlockGUI(player, 1, "break");
			}
			else if (clickedItem.getType() == Material.GRASS_BLOCK) {
				openBlockGUI(player, 1, "place");
			}
			else if (clickedItem.getType() == Material.ARROW) {
				openAuraGUI(player);
			}
		}


		if (event.getClickedInventory().getType() == InventoryType.CHEST) {
			//Ambient menu
			if (title.contains("Ambient Auras")) {
				//Arrows are page selectors, only other buttons are BLAZE_POWDER and REDSTONE (off button)
				if (clickedItem.getType() != Material.ARROW) {
					String auraName = auraManager.getAuraNameFromItem(clickedItem); //returns "Off" for off button
					auraManager.unenchantItems(event.getClickedInventory());
					auraManager.addEnchantGlow(clickedItem);
					auraManager.selectAmbientAura(player, auraName);
					auraManager.activateAmbientAura(player, auraName);
				}
				else {
					int newPageNumber = auraManager.getPageNumberOnArrow(clickedItem);
					openAmbientAuraGUI(player, newPageNumber);
				}
			}
			//Teleport menu
			else if (title.contains("Teleport Auras")) {
				if (clickedItem.getType() == Material.ENDER_EYE) {
					String auraName = auraManager.getAuraNameFromItem(clickedItem);
					auraManager.unenchantItems(event.getClickedInventory());
					auraManager.addEnchantGlow(clickedItem);
					auraManager.selectTPAura(player, auraName);
				}
				else if (clickedItem.getType() == Material.ARROW) {
					int newPageNumber = auraManager.getPageNumberOnArrow(clickedItem);
					openTPAuraGUI(player, newPageNumber);
				}
			}

			//Block Break Menu
			else if (title.contains("Block Break Auras")) {
				//Arrows are page selectors, only other buttons are BLAZE_POWDER and REDSTONE (off button)
				if (clickedItem.getType() == Material.AMETHYST_SHARD) {
					String auraName = auraManager.getAuraNameFromItem(clickedItem); //returns "Off" for off button
					if (clickedItem.containsEnchantment(Enchantment.BINDING_CURSE)) { //Aura is already on so turn it off
						auraManager.deselectBlockAura(player, auraName, "break");
						auraManager.deactivateBlockAura(player, auraName, "break");
					}
					else {
						if (auraManager.selectBlockAura(player, auraName, "break")) { //Returns true if selection was successful
							auraManager.activateBlockAura(player, auraName, "break");
						}
					}
					auraManager.unenchantItems(event.getClickedInventory());
					auraManager.makeSelectedAurasGlow(player, event.getClickedInventory(), "break");
				}
				else if (clickedItem.getType() == Material.REDSTONE) {
					auraManager.turnOffBreakAuras(player);
					auraManager.unenchantItems(event.getClickedInventory());
					auraManager.addEnchantGlow(clickedItem);
				}
				else if (clickedItem.getType() == Material.ARROW ){
					int newPageNumber = auraManager.getPageNumberOnArrow(clickedItem);
					openBlockGUI(player, newPageNumber, "break");
				}
			}

			//Block Place Menu
			else if (title.contains("Block Place Auras")) {
				//Arrows are page selectors, only other buttons are BLAZE_POWDER and REDSTONE (off button)
				if (clickedItem.getType() == Material.AMETHYST_SHARD) {
					String auraName = auraManager.getAuraNameFromItem(clickedItem); //returns "Off" for off button
					if (clickedItem.containsEnchantment(Enchantment.BINDING_CURSE)) { //Aura is already on so turn it off
						auraManager.deselectBlockAura(player, auraName, "place");
						auraManager.deactivateBlockAura(player, auraName, "place");
					}
					else {
						if (auraManager.selectBlockAura(player, auraName, "place")) { //Returns true if selection was successful
							auraManager.activateBlockAura(player, auraName, "place");
						}
					}
					auraManager.unenchantItems(event.getClickedInventory());
					auraManager.makeSelectedAurasGlow(player, event.getClickedInventory(), "place");
				}
				else if (clickedItem.getType() == Material.REDSTONE) {
					auraManager.turnOffPlaceAuras(player);
					auraManager.unenchantItems(event.getClickedInventory());
					auraManager.addEnchantGlow(clickedItem);
				}
				else if (clickedItem.getType() == Material.ARROW ){
					int newPageNumber = auraManager.getPageNumberOnArrow(clickedItem);
					openBlockGUI(player, newPageNumber, "place");
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClose (InventoryCloseEvent event) {
		playersInGUI.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onDisconnect (PlayerQuitEvent event) {
		playersInGUI.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onJoin (PlayerJoinEvent event) {
		UUID UUID = event.getPlayer().getUniqueId();
		if (config.get("auras." + UUID + ".ambient") == null) {
			config.set("auras." + UUID + ".ambient", "Off");
		}
		if (config.get("auras." + UUID + ".tp") == null) {
			config.set("auras." + UUID + ".tp", "Default Teleport Aura");
		}
		if (config.get("auras." + UUID + ".break1") == null) {
			config.set("auras." + UUID + ".break1", "Off");
		}
		if (config.get("auras." + UUID + ".break2") == null) {
			config.set("auras." + UUID + ".break2", "Off");
		}
		if (config.get("auras." + UUID + ".break3") == null) {
			config.set("auras." + UUID + ".break3", "Off");
		}
		if (config.get("auras." + UUID + ".place1") == null) {
			config.set("auras." + UUID + ".place1", "Off");
		}
		if (config.get("auras." + UUID + ".place2") == null) {
			config.set("auras." + UUID + ".place2", "Off");
		}
		if (config.get("auras." + UUID + ".place3") == null) {
			config.set("auras." + UUID + ".place3", "Off");
		}
		plugin.saveConfig();
	}
}