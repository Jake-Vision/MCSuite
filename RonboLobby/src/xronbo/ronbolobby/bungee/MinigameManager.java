package xronbo.ronbolobby.bungee;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import me.ronbo.core.SQLManager;
import me.ronbo.core.ranks.RankManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xronbo.common.Values;
import xronbo.ronbolobby.RonboLobby;


public class MinigameManager implements Listener {

	public static LinkedHashMap<MinigameType, LinkedHashMap<String, ArrayList<Minigame>>> minigames;
	public static HashMap<Integer, Minigame> mgids;
	private static HashMap<String, MinigameType> mgtypes;
	public static HashSet<String> disabled = new HashSet<String>();
	
	public enum MinigameType {
		SKY("Lucky Block Wars", 4, false, 11),
		RONBATTLE("Ronbattle", 1, true, 100),
		SG("Survival Games", 3, true, 24),
		ZOMBO("Zombo Attack", 5, true, 8),
		;
		public String name;
		public int lobbiesPerInstance;
		public boolean spectate;
		public int maxPlayers;
		public static MinigameType getMinigameType(String s) {
			for(MinigameType mgt : MinigameType.values())
				if(mgt.name.equals(s))
					return mgt;
			return null;
		}
		MinigameType(String s, int i, boolean b, int j) {
			name = s;
			lobbiesPerInstance = i;
			spectate = b;
			maxPlayers = j;
		}
		public String toString() {
			return name;
		}
	}
	
	public static int updateTaskID = -1;
	public static void load() {
		minigames = new LinkedHashMap<MinigameType, LinkedHashMap<String, ArrayList<Minigame>>>();
		mgids = new HashMap<Integer, Minigame>();
		mgtypes = new HashMap<String, MinigameType>();
		Scanner scan = null;
		ArrayList<Object[]> mglist = new ArrayList<Object[]>();
		try {
			scan = new Scanner(new File(plugin.getDataFolder() + File.separator + "servers.dat"));
			while(scan.hasNextLine()) {
				String[] data = scan.nextLine().split("-");
				String serverID = data[0];
				String message = data[1];
				String mgt = data[2];
				try {
					Object[] o = {serverID, message, MinigameType.valueOf(mgt), MinigameType.valueOf(mgt).maxPlayers};
					mglist.add(o);
				} catch(Exception e) {
					System.out.println("ERROR LOADING MINIGAME SET " + serverID);
					e.printStackTrace();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(scan != null)
				scan.close();
		}
		for(Object[] o : mglist) {
			MinigameType mgt = (MinigameType)o[2];
			int count = mgt.lobbiesPerInstance;
			for(int k = 0; k < count; k++) {
				Minigame m = new Minigame((String)o[0], (String)o[1], mgt.toString(), (int)o[3], k);
				m.spectate = mgt.spectate;
				if(minigames.containsKey(mgt)) {
					if(minigames.get(mgt).containsKey(m.identifier)) {
						minigames.get(mgt).get(m.identifier).add(m);
					} else {
						ArrayList<Minigame> eda = new ArrayList<Minigame>();
						eda.add(m);
						minigames.get(mgt).put(m.identifier, eda);
					}
				} else {
					LinkedHashMap<String, ArrayList<Minigame>> games = new LinkedHashMap<String, ArrayList<Minigame>>();
					ArrayList<Minigame> eda = new ArrayList<Minigame>();
					eda.add(m);
					games.put(m.identifier, eda);
					minigames.put(mgt, games);
				}
				mgids.put(m.id, m);
			}
			mgtypes.put((String)o[0], mgt);
		}
		if(updateTaskID != -1)
			plugin.getServer().getScheduler().cancelTask(updateTaskID);
		updateTaskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				updateTask();
			}
		}, 30, 30);
	}
	
	public static ArrayList<Minigame> getMinigame(String serverName) {
		if(mgtypes.containsKey(serverName)) {
			return minigames.get(mgtypes.get(serverName)).get(serverName);
		}
		return null;
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		try {
			if(event.getInventory().getName().contains("Kastia Games Menu")) {
				event.setCancelled(true);
				Player p = (Player)event.getWhoClicked();
				if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Lucky Block Wars")) {
					showMenu(MinigameType.SKY, p);
				} else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Ronbattle")) {
					showMenu(MinigameType.RONBATTLE, p);
				} else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Survival Games")) {
					showMenu(MinigameType.SG, p);
				} else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Zombo Attack")) {
					if(RankManager.check(p, "royal"))
						showMenu(MinigameType.ZOMBO, p);
					else
						p.sendMessage(ChatColor.RED + "Zombo Attack is currently in alpha testing phase and is only open to Royals!");
				} else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Factions")) {
					p.teleport(new Location(plugin.getServer().getWorld("fhub"), -1743.5, 107.6, 93.5, 90f, -1.95f).add((int)(Math.random() * 5 - 2), 0, (int)(Math.random() * 5 - 2)));
					plugin.setFactionsInventory(p);
				} else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("RPG")) {
					if(RankManager.check(p, "royal"))
						ChannelManager.sendChannelChange(p, "rpg1");
					else
						p.sendMessage(ChatColor.RED + "RPG is currently in alpha testing phase and is only open to Royals!");
				}
			} else {
				for(MinigameType mgt : MinigameType.values()) {
					if(event.getInventory().getName().equals(ChatColor.BLACK + mgt.name + " Servers")) {
						event.setCancelled(true);
						ItemStack item = event.getCurrentItem();
						ItemMeta im = item.getItemMeta();
						int id = Integer.parseInt(im.getDisplayName().replaceAll("[^0-9]", ""));
						Minigame m = mgids.get(id);
						Player p = (Player)event.getWhoClicked();
						boolean vip = RankManager.check(p, "knight");
						if(m.isUp && !disabled.contains(m.identifier)) {
							if(m.inProgress) {
								if(m.joinable && !m.full()) {
									m.connect(p, false);
								} else if(m.spectatorCount < 20 || vip) {
									if(m.spectate || RankManager.check(p, "helper")) {
										m.connect(p, true);
									} else {
										p.sendMessage(ChatColor.RED + "This gamemode cannot be spectated.");
									}
								} else {
									p.sendMessage(ChatColor.RED + "Spectator slots for this game are filled!");
								}
							} else {
								if(!m.full() || vip) {
									m.connect(p, false);
								} else {
									p.sendMessage(ChatColor.RED + "This server is full!");
								}
							}
						} else {
							p.sendMessage(ChatColor.RED + "This server is down!");
						}
						p.closeInventory();
						break;
					}
				}
			}
		} catch(Exception e) {
			
		}
	}
	
	public static HashMap<String, MinigameType> personalMenus = new HashMap<String, MinigameType>();
	
	@EventHandler
	public void onInvClose(InventoryCloseEvent event) {
		if(event.getInventory().getName().contains(" Servers"))
			personalMenus.remove(event.getPlayer().getName());
	}
	
	public static void showMenu(MinigameType mgt, Player p) {
		Inventory inventory;
		showMenu(mgt, p, inventory = Bukkit.createInventory(p, 6*9, ChatColor.BLACK + mgt.name + " Servers"), true);
		p.openInventory(inventory);
	}
	
	public static void showMenu(final MinigameType mgt, Player p, final Inventory inventory, boolean task) {
		LinkedHashMap<String, ArrayList<Minigame>> games = minigames.get(mgt);
		final String name = p.getName();
		personalMenus.put(name, mgt);
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		boolean vip = RankManager.check(p, "knight");
		if(games == null) {
			p.openInventory(inventory);
			return;
		}
		for(ArrayList<Minigame> l : games.values()) {
			for(Minigame m : l) {
				ItemStack item = null;
				if(m.isUp && !disabled.contains(m.identifier)) {
					if(m.inProgress) {
						if(m.joinable && !m.full()) {
							item = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.YELLOW.getWoolData());
						} else {
							item = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.RED.getWoolData());
						}
					} else {
						if(!m.full() || vip) {
							item = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.WHITE.getWoolData());
						} else {
							item = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.RED.getWoolData());
						}
					}
				} else {
					item = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.BLACK.getWoolData());
				}
				ItemMeta im = item.getItemMeta();
				ArrayList<String> lore = new ArrayList<String>();
				if(m.isUp && !disabled.contains(m.identifier)) {
					im.setDisplayName(ChatColor.AQUA + "" + ChatColor.UNDERLINE + "Server #" + m.id);
					lore.add(ChatColor.RED + "" + ChatColor.ITALIC + (m.title.length() > 0 ? m.title : ""));
					lore.add(ChatColor.YELLOW + "Game: " + ChatColor.GOLD + m.game);
					lore.add(ChatColor.YELLOW + "Players: " + ChatColor.GOLD + m.playerCount + ChatColor.YELLOW + "/" + ChatColor.GOLD + m.maxPlayers);
					lore.add("");
					if(m.additionalInfo != null && m.additionalInfo.size() > 0) {
						for(String temp : m.additionalInfo)
							lore.add(ChatColor.YELLOW + temp);
						lore.add("");
					}
					if(m.inProgress) {
						if(m.joinable && !m.full()) {
							lore.add(ChatColor.YELLOW + "Click to Join! " + ChatColor.GRAY + "(In Progress)");
						} else if(m.spectatorCount < 20 || vip) {
							if(m.spectate || RankManager.check(p, "helper")) {
								lore.add(ChatColor.YELLOW + "Click to Spectate!");
							} else {
								lore.add(ChatColor.YELLOW + "You cannot spectate this game.");
							}
						} else {
							lore.add(ChatColor.RED + "Spectator mode is full for normal players!");
							lore.add(ChatColor.RED + "VIPs can spectate all games.");
						}
					} else {
						if(!m.full() || vip) {
							if(!m.full()) {
								lore.add(ChatColor.GREEN + "Click to Join!");
							} else if(vip) {
								lore.add(ChatColor.GREEN + "Click to Join as VIP!");
								lore.add("");
								lore.add(ChatColor.RED + "" + ChatColor.BOLD + "This lobby is full!");
								lore.add("");
								lore.add(ChatColor.GOLD + "But as a VIP, you can kick");
								lore.add(ChatColor.GOLD + "non-VIPs out of the lobby.");
								lore.add("");
								lore.add(ChatColor.AQUA + "Note: If everyone in the lobby");
								lore.add(ChatColor.AQUA + "is a VIP, then you can't join,");
								lore.add(ChatColor.AQUA + "and will be sent back to Hub.");
							}
						} else {
							p.sendMessage(ChatColor.RED + "This lobby is full!");
							if(m.spectate) {
								lore.add("");
								p.sendMessage(ChatColor.RED + "Wait for the game to start,");
								p.sendMessage(ChatColor.RED + "and you can spectate it!");
							}
						}
					}
				} else {
					im.setDisplayName(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "Server #" + m.id);
					lore.add(ChatColor.RED + "This server is currently down!");
					lore.add(ChatColor.RED + "Please try a different one.");
					lore.add("");
					lore.add(ChatColor.RED + "Sorry for the inconvenience!");
				}
				im.setLore(lore);
				item.setItemMeta(im);
				items.add(item);
			}
		}
		int slot1 = 0;
		int slot2 = inventory.getSize()/2;
		inventory.clear();
		for(ItemStack item : items) {
			byte data = item.getData().getData();
			if(data == DyeColor.WHITE.getWoolData() || data == DyeColor.YELLOW.getWoolData()) {
				if(slot1 < inventory.getSize()/2)
					inventory.setItem(slot1++, item);
			} else if(data == DyeColor.RED.getWoolData()) {
				if(slot2 < inventory.getSize())
					inventory.setItem(slot2++, item);
			} else if(data == DyeColor.BLACK.getWoolData()) {
				//don't display servers that are down
			}
 		}
		if(task) {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					if(plugin.getServer().getPlayerExact(name) == null || !plugin.getServer().getPlayerExact(name).isOnline()) {
						personalMenus.remove(name);
						return;
					}
					if(personalMenus.containsKey(name) && personalMenus.get(name) == mgt) {
						showMenu(mgt, plugin.getServer().getPlayerExact(name), inventory, false);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 20);
					}
				}
			}, 20);
		}
	}
	
	public static int getInvSize(int numGames) {
		if(numGames == 0)
			numGames++;
		return 9 * (int)Math.ceil(numGames / 9.0);
	}
	
	public static Inventory minigamesMenu = null;
	
	public static void openMinigamesMenu(Player p) {
		if(minigamesMenu == null) {
			minigamesMenu = Bukkit.createInventory(p, 6*9, ChatColor.BLACK + "        " + ChatColor.UNDERLINE + "Kastia Games Menu");
			ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte)SkullType.ZOMBIE.ordinal());
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Zombo Attack");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GRAY + "Click to Play!");
			lore.add("");
			lore.add(ChatColor.RED + "Currently in restricted");
			lore.add(ChatColor.RED + "royal-only alpha testing!");
			lore.addAll(Values.longStringToLoreList(ChatColor.YELLOW, "Join a team of survivors and fight back the scary " + ChatColor.RED + "Zombos" + ChatColor.YELLOW + "!", "How many rounds of " + ChatColor.RED + "Zombos" + ChatColor.YELLOW + " can you kill before you die?"));
			im.setLore(lore);
			item.setItemMeta(im);
			minigamesMenu.setItem(2+9*1, Values.removeAttributes(item));
			
			item = new ItemStack(Material.FISHING_ROD);
			im = item.getItemMeta();
			im.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Siege");
			lore = new ArrayList<String>();
			lore.add(ChatColor.GRAY + "Click to Play!");
			lore.add("");
			lore.add(ChatColor.RED + "Currently DISABLED.");
			lore.add(ChatColor.RED + "Reason: Improvements!");
//			lore.addAll(Values.longStringToLoreList(ChatColor.YELLOW, "Fight as a " + ChatColor.RED + "Wild One" + ChatColor.YELLOW + " or a " + ChatColor.AQUA + "Diamond Legionary" + ChatColor.YELLOW + " in this fun PvP game!", ChatColor.RED + "Wild Ones" + ChatColor.YELLOW + ", hunt down " + ChatColor.GOLD + "the " + ChatColor.GOLD + "Grandmaster" + ChatColor.YELLOW + " and kill him!", ChatColor.AQUA + "Diamond Legionaries" + ChatColor.YELLOW + ", protect " + ChatColor.GOLD + "the Grandmaster" + ChatColor.YELLOW + " at all costs!"));
			im.setLore(lore);
			item.setItemMeta(im);
//			minigamesMenu.setItem(1, Values.removeAttributes(item));
			
			item = new ItemStack(Material.CHAINMAIL_HELMET);
			im = item.getItemMeta();
			im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Ronbattle");
			lore = new ArrayList<String>();
			lore.add(ChatColor.GRAY + "Click to Play!");
			lore.add("");
			lore.addAll(Values.longStringToLoreList(ChatColor.YELLOW, "Pick a class and jump into battle!", "Earn Kill Streak rewards, and spend your " + ChatColor.GOLD + "Battle " + ChatColor.GOLD + "Points " + ChatColor.YELLOW + "on new perks!"));
			im.setLore(lore);
			item.setItemMeta(im);
			minigamesMenu.setItem(3+9*2, Values.removeAttributes(item));
			
			item = new ItemStack(Material.SPONGE);
			im = item.getItemMeta();
			im.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Lucky Block Wars");
			lore = new ArrayList<String>();
			lore.add(ChatColor.GRAY + "Click to Play!");
			lore.add("");
			lore.addAll(Values.longStringToLoreList(ChatColor.YELLOW, "Fight for victory in this super cool SkyWars remix!", "Mine " + ChatColor.GOLD + "Lucky " + ChatColor.GOLD + "Blocks" + ChatColor.YELLOW + " for a 50/50 chance of sweet loot or grave danger!", "", ChatColor.GRAY + "a.k.a. Super Sky Strikers"));
			im.setLore(lore);
			item.setItemMeta(im);
			minigamesMenu.setItem(4+9*2, Values.removeAttributes(item));
			
			item = new ItemStack(Material.BOW);
			im = item.getItemMeta();
			im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Survival Games");
			lore = new ArrayList<String>();
			lore.add(ChatColor.GRAY + "Click to Play!");
			lore.add("");
			lore.addAll(Values.longStringToLoreList(ChatColor.YELLOW, "This one's a classic!"));
			im.setLore(lore);
			item.setItemMeta(im);
			minigamesMenu.setItem(5+9*2, Values.removeAttributes(item));
			
			item = new ItemStack(Material.DIAMOND_SWORD);
			im = item.getItemMeta();
			im.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Factions");
			im.setLore(Arrays.asList(ChatColor.GRAY + "Click to play!", "", ChatColor.YELLOW + "You will be warped to the", ChatColor.YELLOW + "Kastia Factions Hub!"));
			item.setItemMeta(im);
			minigamesMenu.setItem(4+9*3, Values.removeAttributes(item, true));
			
			item = new ItemStack(Material.SKULL_ITEM);
			im = item.getItemMeta();
			im.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "RPG");
			im.setLore(Arrays.asList(ChatColor.RED + "Now in Royal-exclusive early testing!", 
					"", 
					ChatColor.RED + "Royals, plase remember this is a very",
					ChatColor.RED + "early version of the RPG. There will be",
					ChatColor.RED + "much more content in the future, so",
					ChatColor.RED + "please do be patient with us <3"
					));
			item.setItemMeta(im);
			minigamesMenu.setItem(1+9*1, Values.removeAttributes(item, true));
			
			item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.PURPLE.getData());
			im = item.getItemMeta();
			im.setDisplayName(ChatColor.AQUA + "");
			im.setLore(new ArrayList<String>());
			item.setItemMeta(im);
			
			for(int k = 0; k < 9; k++)
				minigamesMenu.setItem(k, item);
			for(int k = 9*5; k < 9*6; k++)
				minigamesMenu.setItem(k, item);
			for(int k = 9; k < 9*5; k+=9)
				minigamesMenu.setItem(k, item);
			for(int k = 18-1; k < 6*9-1; k+=9)
				minigamesMenu.setItem(k, item);
		}
		p.openInventory(minigamesMenu);
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
	
	public static String secondsToTimeString(int sec) {
		StringBuilder sb = new StringBuilder("");
		sb.append((sec / 60) + "m ");
		sb.append(sec - (sec / 60) * 60 + "s");
		return sb.toString();
	}
	
	public static HashMap<Minigame, Integer> lastUpdate = new HashMap<Minigame, Integer>();
	public static HashMap<Minigame, Integer> updateCounter = new HashMap<Minigame, Integer>();
	public static HashMap<String, Minigame> tempmap = new HashMap<String, Minigame>();
	public static void updateTask() {
		StringBuilder sb = new StringBuilder();
		for(Entry<MinigameType, LinkedHashMap<String, ArrayList<Minigame>>> e : minigames.entrySet()) {
			for(Entry<String, ArrayList<Minigame>> e2 : e.getValue().entrySet()) {
				String server = e2.getKey();
				int count = 0;
				for(Minigame m : e2.getValue()) {
					int id = count++;
					String myID = server + id;
					sb.append("'" + myID + "' ");
					tempmap.put(myID, m);
				}
			}
		}
		final String set = "(" + sb.toString().trim().replace(' ', ',') + ")";
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			public void run() {
				ResultSet rs = SQLManager.executeQuery("select * from bungee where name in " + set);
				try {
					ArrayList<Minigame> allgames = new ArrayList<Minigame>();
					allgames.addAll(mgids.values());
					while(rs.next()) {
						String name = rs.getString("name");
						Minigame m = tempmap.get(name);
						if(m == null) {
							System.out.println("WARNING: Could not find server " + name + " in bungee table.");
							continue;
						}
						allgames.remove(m);
						int players = rs.getInt("players");
						int lastupdate = rs.getInt("lastupdate");
						boolean joinable = rs.getBoolean("joinable");
						boolean inProgress = rs.getBoolean("inProgress");
						String extra = rs.getString("extra");
						if(extra == null)
							extra = "";
						if(lastUpdate.containsKey(m)) {
							if(lastupdate == lastUpdate.get(m)) {
								if(updateCounter.containsKey(m)) {
									updateCounter.put(m, updateCounter.get(m) + 1);
								} else {
									updateCounter.put(m, 1);
								}
								if(updateCounter.get(m) >= 3) {
									m.isUp = false;
								} else {
									m.isUp = true;
								}
							} else {
								updateCounter.remove(m);
								m.isUp = true;
							}
						} else {
							m.isUp = true;
						}
						lastUpdate.put(m, lastupdate);
						if(m.isUp) {
							m.playerCount = players;
							m.joinable = joinable;
							m.inProgress = inProgress;
							if(extra.trim().length() > 0) {
								m.additionalInfo.clear();
								for(String s : extra.trim().split("!@!")) {
									if(s.length() > 0) {
										m.additionalInfo.add(ChatColor.YELLOW + s);
										if(s.contains("RESTARTING"))
											m.isUp = false;
									}
									if(s.contains("Spectators:")) {
										m.spectatorCount = Integer.parseInt(ChatColor.stripColor(s).replaceAll("[^0-9]", ""));
									}
								}
							}
						} else {
							m.playerCount = 0;
						}
					}
					for(Minigame m : allgames) {
						m.playerCount = 0;
						m.isUp = false;
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static RonboLobby plugin;
	
	public MinigameManager(RonboLobby plugin) {
		MinigameManager.plugin = plugin;
		load();
	}
	
}