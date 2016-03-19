package xronbo.ronbolobby.bungee;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import me.ronbo.core.SQLManager;
import me.ronbo.core.ranks.RankManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xronbo.ronbolobby.RonboLobby;
import xronbo.ronbolobby.Stats;


public class FactionsManager implements Listener {
	
	public static volatile String a = "?/100";
	public static volatile String b = "?/100";
	public static volatile String c = "?/100";

	public static HashMap<String, Long> tpcooldown = new HashMap<String, Long>();
	
	@EventHandler
	public void onEndPortal(PlayerPortalEvent event) {
		if(event.getCause() == TeleportCause.END_PORTAL) {
			event.setCancelled(true);
			final Player p = event.getPlayer();
			double x = event.getPlayer().getLocation().getX();
			double z = event.getPlayer().getLocation().getZ();
			if((x+1771) * (x+1771) + (z-78) * (z-78) < 9) {
				boolean teleport = true;
				if(tpcooldown.containsKey(p.getName())) {
					if(System.currentTimeMillis() - tpcooldown.get(p.getName()) < 3000) {
						teleport = false;
					}
				}
				if(teleport) {
					tpcooldown.put(p.getName(), System.currentTimeMillis());
					if(FactionsManager.upA && (FactionsManager.playercountA < 100 || ((RankManager.check(p, "knight") || Stats.joinFullFactions.contains(p.getName())) && FactionsManager.playercountA < 250))) {
						p.sendMessage(ChatColor.GREEN + "Joining Factions Jade..." + ChatColor.GRAY + " (" + FactionsManager.playercountA + "/100 players)");
						FactionsManager.sendChannelChange(p, "factions1");
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							public void run() {
								if(p.isOnline()) {
									p.teleport(new Location(plugin.getServer().getWorld("fhub"), -1743.5, 107.6, 93.5, 90f, -1.95f).add((int)(Math.random() * 5 - 2), 0, (int)(Math.random() * 5 - 2)));
									p.sendMessage(ChatColor.RED + "Sorry, looks like something is wrong with Factions Jade.");
									p.sendMessage(ChatColor.RED + "Please try again later!");
								}
							}
						}, 20*3);
					} else if(FactionsManager.upA) {
						p.teleport(new Location(plugin.getServer().getWorld("fhub"), -1743.5, 107.6, 93.5, 90f, -1.95f).add((int)(Math.random() * 5 - 2), 0, (int)(Math.random() * 5 - 2)));
						p.sendMessage(ChatColor.RED + "Factions Jade is full right now, sorry! " + ChatColor.GRAY + "(" + FactionsManager.playercountA + "/100 players)");
					} else {
						p.teleport(new Location(plugin.getServer().getWorld("fhub"), -1743.5, 107.6, 93.5, 90f, -1.95f).add((int)(Math.random() * 5 - 2), 0, (int)(Math.random() * 5 - 2)));
						p.sendMessage(ChatColor.RED + "Factions Jade is currently down for maintenance. Sorry!");
					}
				}
			} else if((x+1771) * (x+1771) + (z-108) * (z-108) < 9) {
				boolean teleport = true;
				if(tpcooldown.containsKey(p.getName())) {
					if(System.currentTimeMillis() - tpcooldown.get(p.getName()) < 3000) {
						teleport = false;
					}
				}
				if(teleport) {
					tpcooldown.put(p.getName(), System.currentTimeMillis());
					if(FactionsManager.upB && (FactionsManager.playercountB < 100 || ((RankManager.check(p, "knight") || Stats.joinFullFactions.contains(p.getName())) && FactionsManager.playercountB < 250))) {
						p.sendMessage(ChatColor.GREEN + "Joining Factions Onyx..." + ChatColor.GRAY + " (" + FactionsManager.playercountB + "/100 players)");
						FactionsManager.sendChannelChange(p, "factions2");
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							public void run() {
								if(p.isOnline()) {
									p.teleport(new Location(plugin.getServer().getWorld("fhub"), -1743.5, 107.6, 93.5, 90f, -1.95f).add((int)(Math.random() * 5 - 2), 0, (int)(Math.random() * 5 - 2)));
									p.sendMessage(ChatColor.RED + "Sorry, looks like something is wrong with Factions Onyx.");
									p.sendMessage(ChatColor.RED + "Please try again later!");
								}
							}
						}, 20*3);
					} else if(FactionsManager.upB) {
						p.teleport(new Location(plugin.getServer().getWorld("fhub"), -1743.5, 107.6, 93.5, 90f, -1.95f).add((int)(Math.random() * 5 - 2), 0, (int)(Math.random() * 5 - 2)));
						p.sendMessage(ChatColor.RED + "Factions Onyx is full right now, sorry! " + ChatColor.GRAY + "(" + FactionsManager.playercountB + "/100 players)");
					} else {
						p.teleport(new Location(plugin.getServer().getWorld("fhub"), -1743.5, 107.6, 93.5, 90f, -1.95f).add((int)(Math.random() * 5 - 2), 0, (int)(Math.random() * 5 - 2)));
						p.sendMessage(ChatColor.RED + "Factions Onyx is currently down for maintenance. Sorry!");
					}
				}
			} else if((x+1775) * (x+1775) + (z-85) * (z-85) < 9) {
				boolean teleport = true;
				if(tpcooldown.containsKey(p.getName())) {
					if(System.currentTimeMillis() - tpcooldown.get(p.getName()) < 3000) {
						teleport = false;
					}
				}
				if(teleport) {
					tpcooldown.put(p.getName(), System.currentTimeMillis());
					if(FactionsManager.upC && (FactionsManager.playercountC < 100 || ((RankManager.check(p, "knight") || Stats.joinFullFactions.contains(p.getName())) && FactionsManager.playercountC < 250))) {
						p.sendMessage(ChatColor.GREEN + "Joining Factions Ruby..." + ChatColor.GRAY + " (" + FactionsManager.playercountC + "/100 players)");
						FactionsManager.sendChannelChange(p, "factions3");
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							public void run() {
								if(p.isOnline()) {
									p.teleport(new Location(plugin.getServer().getWorld("fhub"), -1743.5, 107.6, 93.5, 90f, -1.95f).add((int)(Math.random() * 5 - 2), 0, (int)(Math.random() * 5 - 2)));
									p.sendMessage(ChatColor.RED + "Sorry, looks like something is wrong with Factions Ruby.");
									p.sendMessage(ChatColor.RED + "Please try again later!");
								}
							}
						}, 20*3);
					} else if(FactionsManager.upC) {
						p.teleport(new Location(plugin.getServer().getWorld("fhub"), -1743.5, 107.6, 93.5, 90f, -1.95f).add((int)(Math.random() * 5 - 2), 0, (int)(Math.random() * 5 - 2)));
						p.sendMessage(ChatColor.RED + "Factions Ruby is full right now, sorry! " + ChatColor.GRAY + "(" + FactionsManager.playercountC + "/100 players)");
					} else {
						p.teleport(new Location(plugin.getServer().getWorld("fhub"), -1743.5, 107.6, 93.5, 90f, -1.95f).add((int)(Math.random() * 5 - 2), 0, (int)(Math.random() * 5 - 2)));
						p.sendMessage(ChatColor.RED + "Factions Ruby is currently down for maintenance. Sorry!");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		try {
			if(event.getMaterial() == Material.CAKE) {
				showMenu(event.getPlayer());
				event.setCancelled(true);
			}
		} catch(Exception e) {
			
		}
	}
	
	public static void showMenu(Player p) {
		Inventory inventory = Bukkit.createInventory(p, 5*9, ChatColor.BLACK + "Kastia Factions");
		ItemStack item;
		ItemMeta im;
		
		item = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.LIME.getWoolData());
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.GREEN + "Factions " + ChatColor.BOLD + "Jade");
		if(upA && !a.contains("?")) {
			if(playercountA < 100 || ((RankManager.check(p, "knight") || Stats.joinFullFactions.contains(p.getName())) && playercountA < 250)) {
				im.setLore(Arrays.asList(ChatColor.YELLOW + "Original Factions", "", ChatColor.GOLD + "Players: " + ChatColor.YELLOW + a, "", ChatColor.GRAY + "Click to enter!"));
			} else if(playercountA >= 100 && !(RankManager.check(p, "knight") || Stats.joinFullFactions.contains(p.getName()))) {
				im.setLore(Arrays.asList(ChatColor.YELLOW + "Original Factions", "", ChatColor.GOLD + "Players: " + ChatColor.YELLOW + a, "", ChatColor.RED + "This server is currently full!", "", ChatColor.GOLD + "VIP" + ChatColor.RED + " players can join full servers", ChatColor.RED + "up to a maximum of 250 players!"));
			} else {
				im.setLore(Arrays.asList(ChatColor.YELLOW + "Original Factions", "", ChatColor.GOLD + "Players: " + ChatColor.YELLOW + a, "", ChatColor.RED + "This server is too full for anyone to join!"));
			}
		} else {
			im.setLore(Arrays.asList(ChatColor.YELLOW + "Original Factions", "", ChatColor.RED + "Currently down for maintenance!"));
		}
		if(onlineStaffA.size() > 0) {
			ArrayList<String> lore = (ArrayList<String>)im.getLore();
			lore.add("");
			lore.add(ChatColor.GREEN + "Online Staff:");
			for(String s : onlineStaffA)
				lore.add(ChatColor.GRAY + "- " + ChatColor.WHITE + s);
			im.setLore(lore);
		}
		item.setItemMeta(im);
		inventory.setItem(3+9*2, item);

		item = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.GRAY.getWoolData());
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.GRAY + "Factions " + ChatColor.BOLD + "Onyx");
		if(upB && !b.contains("?")) {
			if(playercountB < 100 || ((RankManager.check(p, "knight") || Stats.joinFullFactions.contains(p.getName())) && playercountB < 250)) {
				im.setLore(Arrays.asList(ChatColor.YELLOW + "OP Factions", "", ChatColor.GOLD + "Players: " + ChatColor.YELLOW + b, "", ChatColor.GRAY + "Click to enter!"));
			} else if(playercountB >= 100 && !(RankManager.check(p, "knight") || Stats.joinFullFactions.contains(p.getName()))) {
				im.setLore(Arrays.asList(ChatColor.YELLOW + "OP Factions", "", ChatColor.GOLD + "Players: " + ChatColor.YELLOW + b, "", ChatColor.RED + "This server is currently full!", "", ChatColor.GOLD + "VIP" + ChatColor.RED + " players can join full servers", ChatColor.RED + "up to a maximum of 250 players!"));
			} else {
				im.setLore(Arrays.asList(ChatColor.YELLOW + "OP Factions", "", ChatColor.GOLD + "Players: " + ChatColor.YELLOW + b, "", ChatColor.RED + "This server is too full for anyone to join!"));
			}
		} else {
			im.setLore(Arrays.asList(ChatColor.YELLOW + "OP Factions", "", ChatColor.RED + "Currently down for maintenance!"));
		}
		if(onlineStaffB.size() > 0) {
			ArrayList<String> lore = (ArrayList<String>)im.getLore();
			lore.add("");
			lore.add(ChatColor.GREEN + "Online Staff:");
			for(String s : onlineStaffB)
				lore.add(ChatColor.GRAY + "- " + ChatColor.WHITE + s);
			im.setLore(lore);
		}
		item.setItemMeta(im);
		inventory.setItem(4+9*2, item);

		item = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.RED.getWoolData());
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.RED + "Factions " + ChatColor.BOLD + "Ruby");
		if(upC && !c.contains("?")) {
			if(playercountC < 100 || ((RankManager.check(p, "knight") || Stats.joinFullFactions.contains(p.getName())) && playercountC < 250)) {
				im.setLore(Arrays.asList(ChatColor.YELLOW + "McMMO Factions", "", ChatColor.GOLD + "Players: " + ChatColor.YELLOW + c, "", ChatColor.GRAY + "Click to enter!"));
			} else if(playercountC >= 100 && !(RankManager.check(p, "knight") || Stats.joinFullFactions.contains(p.getName()))) {
				im.setLore(Arrays.asList(ChatColor.YELLOW + "McMMO Factions", "", ChatColor.GOLD + "Players: " + ChatColor.YELLOW + c, "", ChatColor.RED + "This server is currently full!", "", ChatColor.GOLD + "VIP" + ChatColor.RED + " players can join full servers", ChatColor.RED + "up to a maximum of 250 players!"));
			} else {
				im.setLore(Arrays.asList(ChatColor.YELLOW + "McMMO Factions", "", ChatColor.GOLD + "Players: " + ChatColor.YELLOW + c, "", ChatColor.RED + "This server is too full for anyone to join!"));
			}
		} else {
			im.setLore(Arrays.asList(ChatColor.YELLOW + "McMMO Factions", "", ChatColor.RED + "Currently down for maintenance!"));
		}
		if(onlineStaffC.size() > 0) {
			ArrayList<String> lore = (ArrayList<String>)im.getLore();
			lore.add("");
			lore.add(ChatColor.GREEN + "Online Staff:");
			for(String s : onlineStaffC)
				lore.add(ChatColor.GRAY + "- " + ChatColor.WHITE + s);
			im.setLore(lore);
		}
		item.setItemMeta(im);
		inventory.setItem(5+9*2, item);
		
		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLUE.getData());
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.AQUA + "");
		im.setLore(new ArrayList<String>());
		item.setItemMeta(im);
		
		for(int k = 0; k < 9; k++)
			inventory.setItem(k, item);
		for(int k = 9*4; k < 9*5; k++)
			inventory.setItem(k, item);
		for(int k = 9; k < 9*5; k+=9)
			inventory.setItem(k, item);
		for(int k = 18-1; k < 5*9-1; k+=9)
			inventory.setItem(k, item);
		
		p.openInventory(inventory);
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent event) {
		try {
			if(event.getInventory().getName().contains("Factions")) {
				event.setCancelled(true);
				if(event.getCurrentItem().getItemMeta().getLore().toString().contains("Click to enter!")) {
					if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Jade")) {
						((Player)event.getWhoClicked()).sendMessage(ChatColor.GREEN + "Connecting to Factions Jade...");
						sendChannelChange((Player)event.getWhoClicked(), "factions1");
					} else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Onyx")) {
						((Player)event.getWhoClicked()).sendMessage(ChatColor.GREEN + "Connecting to Factions Onyx...");
						sendChannelChange((Player)event.getWhoClicked(), "factions2");
					} else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Ruby")) {
						((Player)event.getWhoClicked()).sendMessage(ChatColor.GREEN + "Connecting to Factions Ruby...");
						sendChannelChange((Player)event.getWhoClicked(), "factions3");
					}
				} else {
					((Player)event.getWhoClicked()).sendMessage(ChatColor.RED + "You can't join this server right now!");
				}
				event.getWhoClicked().closeInventory();
			}
		} catch(Exception e) {
			
		}
	}

	public static void sendChannelChange(Player p, String channelName) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF(channelName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
	}
	
	public static RonboLobby plugin;
	
	public static volatile int lastA = 0, lastB = 0, lastC = 0;
	public static volatile int counterA = 1, counterB = 1, counterC = 1;
	public static volatile boolean upA = false, upB = false, upC = false;
	public static volatile int playercountA, playercountB, playercountC;
	public static volatile Vector<String> onlineStaffA = new Vector<String>();
	public static volatile Vector<String> onlineStaffB = new Vector<String>();
	public static volatile Vector<String> onlineStaffC = new Vector<String>();
	public FactionsManager(RonboLobby pl) {
		FactionsManager.plugin = pl;
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					public void run() {
						ResultSet rs = SQLManager.executeQuery("select players, lastUpdate, name, extra from bungee where name like 'factions%'");
						try {
							while(rs.next()) {
								if(rs.getString("name").equals("factions1")) {
									a = (playercountA = rs.getInt("players")) + "/100";
									if(counterA++ % 2 == 0) {
										upA = true;
										if(lastA == rs.getInt("lastUpdate")) {
											upA = false;
										}
										lastA = rs.getInt("lastUpdate");
									}
									onlineStaffA.clear();
									if(rs.getString("extra") != null)
										for(String s : rs.getString("extra").split(" "))
											if(s.length() > 0)
												onlineStaffA.add(s);
								} else if(rs.getString("name").equals("factions2")) {
									b = (playercountB = rs.getInt("players")) + "/100";
									if(counterB++ % 2 == 0) {
										upB = true;
										if(lastB == rs.getInt("lastUpdate")) {
											upB = false;
										}
										lastB = rs.getInt("lastUpdate");
									}
									onlineStaffB.clear();
									if(rs.getString("extra") != null)
										for(String s : rs.getString("extra").split(" "))
											if(s.length() > 0)
												onlineStaffB.add(s);
								} else if(rs.getString("name").equals("factions3")) {
									c = (playercountC = rs.getInt("players")) + "/100";
									if(counterC++ % 2 == 0) {
										upC = true;
										if(lastC == rs.getInt("lastUpdate")) {
											upC = false;
										}
										lastC = rs.getInt("lastUpdate");
									}
									onlineStaffC.clear();
									if(rs.getString("extra") != null)
										for(String s : rs.getString("extra").split(" "))
											if(s.length() > 0)
												onlineStaffC.add(s);
								}
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}, 20 * 3, 20 * 10);
	}
	
}