package xronbo.ronbolobby;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xronbo.common.Values;

public class OptionsManager implements Listener {

	@EventHandler
	public void oninteract(PlayerInteractEvent event) {
		if(event.getItem() != null && event.getItem().getType() == Material.REDSTONE_TORCH_ON) {
			showMenu(event.getPlayer());
			event.setCancelled(true);
		}
	}
	
	public static HashSet<String> hidden = new HashSet<String>();
	public static HashSet<String> filter = new HashSet<String>();
	
	public void showMenu(Player p) {
		Inventory inventory = Bukkit.createInventory(p, 9, ChatColor.BLACK + "            " + ChatColor.UNDERLINE + "Hub Options");
		ItemStack item = null;
		ItemMeta im = null;
		ArrayList<String> lore = null;
//		Stats stats = RonboLobby.getStats(p);
		
		item = new ItemStack(Material.INK_SACK, 1, DyeColor.MAGENTA.getDyeData());
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Solo Mode");
		lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Click to Activate");
		lore.add("");
		lore.add(ChatColor.YELLOW + "This will hide all players from you.");
		im.setLore(lore);
		item.setItemMeta(im);
		inventory.setItem(0, Values.removeAttributes(item));
		
		item = new ItemStack(Material.INK_SACK, 1, DyeColor.LIME.getDyeData());
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Community Mode");
		lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Click to Activate");
		lore.add("");
		lore.add(ChatColor.YELLOW + "This will show all players to you.");
		im.setLore(lore);
		item.setItemMeta(im);
		inventory.setItem(1, Values.removeAttributes(item));
		
//		item = new ItemStack(Material.INK_SACK, 1, DyeColor.GRAY.getData());
//		im = item.getItemMeta();
//		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Chat Filter (Enable)");
//		lore = new ArrayList<String>();
//		lore.add(ChatColor.GRAY + "Click to Activate");
//		lore.add("");
//		lore.add(ChatColor.YELLOW + "You will only see chat from VIPs.");
//		im.setLore(lore);
//		item.setItemMeta(im);
//		inventory.setItem(2, Values.removeAttributes(item));
//		
//		item = new ItemStack(Material.INK_SACK, 1, DyeColor.GRAY.getData());
//		im = item.getItemMeta();
//		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Chat Filter (Disable)");
//		lore = new ArrayList<String>();
//		lore.add(ChatColor.GRAY + "Click to Activate");
//		lore.add("");
//		lore.add(ChatColor.YELLOW + "You will see all chat.");
//		im.setLore(lore);
//		item.setItemMeta(im);
//		inventory.setItem(3, Values.removeAttributes(item));
		
		item = new ItemStack(Material.INK_SACK, 1, DyeColor.MAGENTA.getData());
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Suggestions?");
		lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Want a new option added?");
		lore.add(ChatColor.GRAY + "Just post your suggestion");
		lore.add(ChatColor.GRAY + "on the Kastia forums!");
		im.setLore(lore);
		item.setItemMeta(im);
		inventory.setItem(5, Values.removeAttributes(item));
	
		p.openInventory(inventory);
	}
	
	@EventHandler
	public void onLogin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		for(String s : hidden) {
			if(plugin.getServer().getPlayerExact(s) != null && plugin.getServer().getPlayerExact(s).isValid() && plugin.getServer().getPlayerExact(s).isOnline()) {
				plugin.getServer().getPlayerExact(s).hidePlayer(p);
			}
		}
		if(hidden.contains(p.getName())) {
			for(Player p2 : plugin.getServer().getOnlinePlayers()) {
				p.hidePlayer(p2);
			}
		}
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		try {
			if(event.getInventory().getName().contains("Hub Options")) {
				event.setCancelled(true);
				Player p = (Player)event.getWhoClicked();
				ItemStack clickedItem = event.getCurrentItem();
				p.closeInventory();
//				ItemStack item = null;
//				ItemMeta im = null;
//				ArrayList<String> lore = null;
				if(clickedItem.getItemMeta().getDisplayName().toLowerCase().contains("solo mode")) {
					hidden.add(p.getName());
					for(Player p2 : plugin.getServer().getOnlinePlayers()) {
						p.hidePlayer(p2);
					}
					p.sendMessage(ChatColor.YELLOW + "Solo mode " + ChatColor.GREEN + ChatColor.BOLD + "ENABLED" + ChatColor.YELLOW + ".");
					p.sendMessage(ChatColor.YELLOW + "You can't see anyone!");
				} else if(clickedItem.getItemMeta().getDisplayName().toLowerCase().contains("community mode")) {
					hidden.remove(p.getName());
					for(Player p2 : plugin.getServer().getOnlinePlayers()) {
						p.showPlayer(p2);
					}
					p.sendMessage(ChatColor.YELLOW + "Community mode " + ChatColor.GREEN + ChatColor.BOLD + "ENABLED" + ChatColor.YELLOW + ".");
					p.sendMessage(ChatColor.YELLOW + "You can see everyone!");
				} else if(clickedItem.getItemMeta().getDisplayName().toLowerCase().contains("chat filter (enable)")) {
					p.sendMessage(ChatColor.YELLOW + "Chat Filter " + ChatColor.GREEN + ChatColor.BOLD + "ENABLED" + ChatColor.YELLOW + ".");
					p.sendMessage(ChatColor.YELLOW + "You will only see chat from VIPs.");
					filter.add(p.getName());
				} else if(clickedItem.getItemMeta().getDisplayName().toLowerCase().contains("chat filter (disable)")) {
					p.sendMessage(ChatColor.YELLOW + "Chat Filter " + ChatColor.RED + ChatColor.BOLD + "DISABLED" + ChatColor.YELLOW + ".");
					p.sendMessage(ChatColor.YELLOW + "You will see all chat.");
					filter.remove(p.getName());
				} 
			}
		} catch(Exception e) {
			
		}
	}
	
	public OptionsManager(RonboLobby plugin) {
		OptionsManager.plugin = plugin;
	}
	
	public static RonboLobby plugin;
}
