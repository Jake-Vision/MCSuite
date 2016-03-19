package xronbo.ronbolobby;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import me.ronbo.core.SQLManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xronbo.common.Values;
import xronbo.ronbolobby.bungee.ChannelManager;

public class HubManager implements Listener {

	@EventHandler
	public void oninteract(PlayerInteractEvent event) {
		if(event.getItem() != null && event.getItem().getType() == Material.NETHER_STAR) {
			showMenu(event.getPlayer());
			event.setCancelled(true);
		}
	}
	
	public void showMenu(Player p) {
		Inventory inventory = Bukkit.createInventory(p, 9, ChatColor.BLACK + "             " + ChatColor.UNDERLINE + "Kastia Hubs");
		ItemStack item = null;
		ItemMeta im = null; 
		ArrayList<String> lore = null;
		int count = 0;
		ArrayList<Hub> hublist = new ArrayList<Hub>();
		hublist.addAll(hubs.values());
		Collections.sort(hublist);
		for(Hub h : hublist) {
			if(h.name.equals(RonboLobby.serverName)) {
				item = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.LIME.getData());
				im = item.getItemMeta();
				im.setDisplayName(ChatColor.AQUA + "Hub #" + h.myId);
				lore = new ArrayList<String>();
				lore.add(ChatColor.GRAY + "You are on this hub!");
				lore.add("");
				lore.add(ChatColor.GOLD + "Players: " + ChatColor.YELLOW + h.players + "/100");
				im.setLore(lore);
				item.setItemMeta(im);
				inventory.setItem(count++, Values.removeAttributes(item));
			} else {
				if(h.isUp) {
					if(h.players >= 100) {
						item = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.RED.getData());
						im = item.getItemMeta();
						im.setDisplayName(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "Hub #" + h.myId);
						lore = new ArrayList<String>();
						lore.add(ChatColor.RED + "Currently full!");
						lore.add("");
						lore.add(ChatColor.GOLD + "Players: " + ChatColor.YELLOW + h.players + "/100");
						im.setLore(lore);
						item.setItemMeta(im);
						inventory.setItem(count++, Values.removeAttributes(item));
					} else {
						if(h.players >= 50) {
							item = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.YELLOW.getData());
							im = item.getItemMeta();
							im.setDisplayName(ChatColor.YELLOW + "Hub #" + h.myId);
						} else {
							item = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.WHITE.getData());
							im = item.getItemMeta();
							im.setDisplayName(ChatColor.GREEN + "Hub #" + h.myId);
						}
						lore = new ArrayList<String>();
						lore.add(ChatColor.GRAY + "Click to Join");
						lore.add("");
						lore.add(ChatColor.GOLD + "Players: " + ChatColor.YELLOW + h.players + "/100");
						im.setLore(lore);
						item.setItemMeta(im);
						inventory.setItem(count++, Values.removeAttributes(item));
					}
				} else {
					item = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.BLACK.getData());
					im = item.getItemMeta();
					im.setDisplayName(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "Hub #" + h.myId);
					lore = new ArrayList<String>();
					lore.add(ChatColor.RED + "Currently offline!");
					im.setLore(lore);
					item.setItemMeta(im);
					inventory.setItem(count++, Values.removeAttributes(item));
				}
			}
		}
		
		p.openInventory(inventory);
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		try {
			if(event.getInventory().getName().contains("Kastia Hubs")) {
				event.setCancelled(true);
				Player p = (Player)event.getWhoClicked();
				ItemStack clickedItem = event.getCurrentItem();
				p.closeInventory();
				if(clickedItem.getItemMeta().getDisplayName().contains("Hub #")) {
					int id = Integer.parseInt(ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).replaceAll("[^0-9]", ""));
					Hub h = hubs_byid.get(id);
					if(h != null) {
						if(h.name.equals(RonboLobby.serverName)) {
							p.sendMessage(ChatColor.RED + "Error: You are already on this hub!");
						} else {
							if(h.isUp) {
								if(h.players < 100) {
									ChannelManager.sendChannelChange(p, h.name);
								} else {
									p.sendMessage(ChatColor.RED + "Error: That hub is full!");
								}
							} else {
								p.sendMessage(ChatColor.RED + "Error: That hub is down.");
							}
						}
					} else {
						p.sendMessage(ChatColor.RED + "Error: Could not find corresponding hub server.");
					}
				}
			}
		} catch(Exception e) {
			
		}
	}
	public static ConcurrentHashMap<String, Hub> hubs = new ConcurrentHashMap<String, Hub>();
	public static ConcurrentHashMap<Integer, Hub> hubs_byid = new ConcurrentHashMap<Integer, Hub>();
	public static ConcurrentHashMap<String, Long> lastupdates = new ConcurrentHashMap<String, Long>();
	public static void loadHubUpdaterTask() {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			public void run() {
				ResultSet rs = SQLManager.executeQuery("select name, players, lastupdate from bungee where name like 'hub%' and length(name) > 3");
				try {
					while(rs.next()) {
						String name = rs.getString("name");
						int players = rs.getInt("players");
						long lastupdate = rs.getLong("lastupdate");
						boolean isup = true;
						if(lastupdates.containsKey(name)) {
							if(lastupdates.get(name) == lastupdate) {
								isup = false;
							}
						}
						lastupdates.put(name, lastupdate);
						Hub h;
						if(hubs.containsKey(name)) {
							h = hubs.get(name);
						} else {
							h = new Hub(name);
							hubs.put(name, h);
							hubs_byid.put(h.myId, h);
						}
						h.players = players;
						h.isUp = isup;
					}
					if(plugin.isEnabled())
						plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, this, 20*15);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static class Hub implements Comparable<Hub> {
		public final String name;
		public volatile int players;
		public volatile boolean isUp;
		public static int ID = 1;
		public int myId = ID++;
		@Override
		public int compareTo(Hub other) {
			return this.myId - other.myId;
		}
		@Override
		public boolean equals(Object other) {
			if(other instanceof Hub && ((Hub)other).name.equals(this.name))
				return true;
			return false;
		}
		public Hub(String name) {
			this.name = name;
		}
		@Override
		public String toString() {
			return name + "(" + players + ")";
		}
	}
	
	public HubManager(RonboLobby plugin) {
		HubManager.plugin = plugin;
		loadHubUpdaterTask();
	}
	
	public static RonboLobby plugin;
}
