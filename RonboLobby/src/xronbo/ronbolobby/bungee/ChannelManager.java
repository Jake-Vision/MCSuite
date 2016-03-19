package xronbo.ronbolobby.bungee;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Scanner;

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
import org.bukkit.material.Wool;

import xronbo.common.Values;
import xronbo.ronbolobby.RonboLobby;


public class ChannelManager implements Listener {

	public static LinkedHashMap<String, Channel> channels = new LinkedHashMap<String, Channel>();
	
	public static void loadChannels() {
		if(!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdirs();
		File f = new File(plugin.getDataFolder() + File.separator + "channels.dat");
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			Scanner scan = new Scanner(f);
			if(scan.hasNextLine()) {
				channels.clear();
				while(scan.hasNextLine()) {
					String[] data = scan.nextLine().split(";");
					String id = data[0];
					String abbreviation = data[1];
					String name = data[2];
					int rank = 1;
					String desc = data.length > 4 ? data[4] : "";
					channels.put(id, new Channel(id,abbreviation,name,rank,desc));
				}
			}
			scan.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		updateTask();
	}

	public static HashMap<Channel, Integer> lastUpdate = new HashMap<Channel, Integer>();
	public static HashMap<Channel, Integer> updateCounter = new HashMap<Channel, Integer>();
	public static void updateTask() {
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				for(Entry<String, Channel> e : channels.entrySet()) {
					final String ccname = e.getKey();
					final Channel c = e.getValue();
					plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
						public void run() {
							ResultSet rs = SQLManager.executeQuery("select * from bungee where name = 'channel_" + ccname + "'");
							try {
								if(rs.next()) {
									int players = rs.getInt("players");
									int lastupdate = rs.getInt("lastupdate");
									String extra = rs.getString("extra");
									if(extra == null)
										extra = "";
									if(lastUpdate.containsKey(c)) {
										if(lastupdate == lastUpdate.get(c)) {
											if(updateCounter.containsKey(c)) {
												updateCounter.put(c, updateCounter.get(c) + 1);
											} else {
												updateCounter.put(c, 1);
											}
											if(updateCounter.get(c) >= 5) {
												c.isUp = false;
											} else {
												c.isUp = true;
											}
										} else {
											updateCounter.remove(c);
											c.isUp = true;
										}
									} else {
										c.isUp = true;
									}
									lastUpdate.put(c, lastupdate);
									if(c.isUp) {
										c.playerCount = players;
										c.latency = (extra.length() > 0 ? Integer.parseInt(extra.replaceAll("[^0-9]", "")) : 999);
									} else {
										c.playerCount = 0;
										c.latency = 999;
									}
								} else {
									c.isUp = false;
								}
							} catch(Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		}, 20, 20);
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		try {
			if(event.getInventory().getName().contains("Kastia Channels")) {
				event.setCancelled(true);
				String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				final Player p = (Player)event.getWhoClicked();
				for(final Channel c : channels.values()) {
					if(c.name.equals(name)) {
						if(c.canConnect(p)) {
							p.sendMessage(ChatColor.GREEN + "Connecting you to " + c.name + ChatColor.GREEN + "...");
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								public void run() {
									if(p != null && p.isOnline() && p.isValid()) {
										c.connect(p);
										plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
											public void run() {
												if(p != null && p.isOnline() && p.isValid()) {
													p.sendMessage(ChatColor.RED + "Oops! Looks like there was a connection error.");
													p.sendMessage(ChatColor.RED + "Please try connecting to a channel again.");
												}
											}
										}, 20 * 5);
									}
								}
							}, 10);
						} else {
							p.sendMessage(ChatColor.RED + "You can't connect to this channel!");
						}
						break;
					}
				}
				p.closeInventory();
			}
		} catch(Exception e) {
			
		}
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		checkMenus(e.getPlayer());
	}
	
	public void checkMenus(final Player p) {
		if(p.getItemInHand() != null) {
//			if(p.getItemInHand().getType() == Material.COMPASS) {
//				p.sendMessage(ChatColor.GREEN + "Automatically connecting you to the fastest channel!");
//				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//					public void run() {
//						if(p != null && p.isOnline() && p.isValid()) {
//							ArrayList<Channel> sorted = new ArrayList<Channel>(channels.values());
//							Collections.sort(sorted);
//							for(int k = 0; k < sorted.size(); k++) {
//								if(sorted.get(k).canConnect(p)) {
//									sorted.get(k).connect(p);
//									plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//										public void run() {
//											if(p != null && p.isOnline() && p.isValid()) {
//												p.sendMessage(ChatColor.RED + "Oops! Looks like there was a connection error.");
//												p.sendMessage(ChatColor.RED + "Please try connecting to a channel again.");
//											}
//										}
//									}, 20 * 5);
//									return;
//								}
//							}
//							p.sendMessage(ChatColor.RED + "Sorry! It looks like all channels are unavailable right now.");
//							p.sendMessage(ChatColor.RED + "They will be brought back up ASAP!");
//							p.sendMessage(ChatColor.RED + "Until then, check out Kastia's minigames! There's plenty to do!");
//						}
//					}
//				}, 10);
//			} else 
			if(p.getItemInHand().getType() == Material.DIAMOND) {
				showChannelMenu(p);
			}
		}
	}
	
	public static void showChannelMenu(Player p) {
		Inventory inventory = Bukkit.createInventory(p, getInvSize(channels.size()), ChatColor.BLACK + "Kastia Channels");
		int count = 0;
		for(Channel c : channels.values()) {
			ItemStack item = null;
			ItemMeta im = null;
			if(!c.isUp) { 
				item = new ItemStack(Material.WOOL, 1, DyeColor.BLACK.getData());
				im = item.getItemMeta();
				im.setDisplayName(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + c.name);
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "Click to Connect!");
				lore.add("");
				if(c.description != null && c.description.length() > 0) {
					lore.add(ChatColor.AQUA + "" + ChatColor.STRIKETHROUGH + c.description);
					lore.add("");
				}
				lore.add(ChatColor.RED + "This channel is down.");
				lore.add(ChatColor.RED + "Sorry for the trouble!");
				im.setLore(lore);
			} else {
				if(c.latency >= Values.LATENCY_THRESHOLD_HIGH || c.playerCount > Channel.MAX_PLAYERS * Values.PLAYER_THRESHOLD_HIGH) {
					item = new ItemStack(Material.WOOL, 1, DyeColor.RED.getData());
					im = item.getItemMeta();
					item.setData(new Wool(DyeColor.RED));
					im.setDisplayName(ChatColor.RED + c.name);
				} else if(c.latency >= Values.LATENCY_THRESHOLD_MEDIUM || c.playerCount > Channel.MAX_PLAYERS * Values.PLAYER_THRESHOLD_MEDIUM) {
					item = new ItemStack(Material.WOOL, 1, DyeColor.YELLOW.getData());
					im = item.getItemMeta();
					item.setData(new Wool(DyeColor.YELLOW));
					im.setDisplayName(ChatColor.YELLOW + c.name);
				} else {
					item = new ItemStack(Material.WOOL, 1, DyeColor.LIME.getData());
					im = item.getItemMeta();
					item.setData(new Wool(DyeColor.LIME));
					im.setDisplayName(ChatColor.GREEN + c.name);
				}
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(ChatColor.GRAY + "Click to Connect!");
				lore.add("");
				if(c.description != null && c.description.length() > 0) {
					lore.add(ChatColor.AQUA + c.description);
					lore.add("");
				}
				lore.add(getPlayerString(c.playerCount));
				lore.add(getLatencyString(c.latency));
				im.setLore(lore);
			}
			item.setAmount(count + 1);
			item.setItemMeta(im);
			inventory.setItem(count++, item);
		}
		p.openInventory(inventory);
	}
	
	public static String getPlayerString(int k) {
		if(k > Channel.MAX_PLAYERS * Values.PLAYER_THRESHOLD_HIGH) {
			return ChatColor.DARK_RED + "Players: " + ChatColor.RED + k + "/" + Channel.MAX_PLAYERS;
		} else if(k > Channel.MAX_PLAYERS * Values.PLAYER_THRESHOLD_MEDIUM) {
			return ChatColor.GOLD + "Players: " + ChatColor.YELLOW + k + "/" + Channel.MAX_PLAYERS;
		} else {
			return ChatColor.DARK_GREEN + "Players: " + ChatColor.GREEN + k + "/" + Channel.MAX_PLAYERS;
		}
	}
	
	public static String getLatencyString(int k) {
		if(k >= Values.LATENCY_THRESHOLD_HIGH) {
			return ChatColor.DARK_RED + "Latency: " + ChatColor.RED + k + "ms";
		} else if(k >= Values.LATENCY_THRESHOLD_MEDIUM) {
			return ChatColor.GOLD + "Latency: " + ChatColor.YELLOW + k + "ms";
		} else {
			return ChatColor.DARK_GREEN + "Latency: " + ChatColor.GREEN + k + "ms";
		}
	}
	
	public static int getInvSize(int numChannels) {
		if(numChannels == 0)
			numChannels++;
		return 9 * (int)Math.ceil(numChannels / 9.0);
	}
	
	public static void sendChannelChange(Player p, String channelName) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF(channelName);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
	}
	
	public static RonboLobby plugin;
	
	public ChannelManager(RonboLobby plugin) {
		ChannelManager.plugin = plugin;
	}
	
}