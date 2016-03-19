package xronbo.ronbolobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class CasinoManager implements Listener {
	
	public HashSet<Coord> inuse = new HashSet<Coord>();
	public HashSet<String> playing = new HashSet<String>();
	public HashSet<UUID> noPickup = new HashSet<UUID>();
	
	public static ArrayList<Slot> cslots = new ArrayList<Slot>();
	
	static {
		for(Slot s : new Slot[] {
		new Slot(ChatColor.AQUA + "D", "Diamond", 0.08, 300),
		new Slot(ChatColor.RED + "T", "TNT", 0.10, 250),
		new Slot(ChatColor.GOLD + "J", "Jackpot", 0.003, 1000),
		new Slot(ChatColor.GREEN + "$", "Big Bux", 0.003, 1200),
		new Slot(ChatColor.LIGHT_PURPLE + "R", "Ronbo", 0.05, 400),
		new Slot(ChatColor.DARK_BLUE + "M", "Mystery", 0.07, 300),
		}) {
			cslots.add(s);
		}
	}
	
	public Material[] jungleMats = {
		Material.LEAVES,
		Material.APPLE,
		Material.LEATHER,
		Material.COCOA,
		Material.ARROW,
		Material.VINE,
		Material.BREAD,
		Material.YELLOW_FLOWER,
		Material.RED_MUSHROOM,
		Material.RED_ROSE,
		Material.BROWN_MUSHROOM,
		Material.LOG,
		Material.SEEDS
	};
	
	public static HashMap<Material, Integer> jungleValues = new HashMap<Material, Integer>();
	
	static {
		jungleValues.put(Material.LEAVES, 5);
		jungleValues.put(Material.APPLE, 10);
		jungleValues.put(Material.LEATHER, 15);
		jungleValues.put(Material.COCOA, 12);
		jungleValues.put(Material.ARROW, 11);
		jungleValues.put(Material.VINE, 1);
		jungleValues.put(Material.BREAD, 30);
		jungleValues.put(Material.YELLOW_FLOWER, 16);
		jungleValues.put(Material.RED_MUSHROOM, 3);
		jungleValues.put(Material.RED_ROSE, 16);
		jungleValues.put(Material.BROWN_MUSHROOM, 3);
		jungleValues.put(Material.LOG, 6);
		jungleValues.put(Material.SEEDS, 2);
	}
	
	public static class Slot {
		public String id,name;
		public double rarity;
		public int worth;
		public Slot(String id, String name, double rarity, int worth) {
			this.id = id;
			this.name = name;
			this.rarity = rarity;
			this.worth = worth;
		}
	}
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent event) {
		try {
			final Sign sign = (Sign)event.getClickedBlock().getState();
			final Player p = event.getPlayer();
			final Coord coord = new Coord(event.getClickedBlock());
			final Block block = event.getClickedBlock();
			Stats stats = RonboLobby.getStats(p);
			if(playing.contains(p.getName())) {
				p.sendMessage(ChatColor.RED + "You are already playing a game!");
			} else {
				if(stats != null) {
		 			if(sign.getLine(0).contains("CreeperSlots")) {
						System.out.println(inuse);
						if(inuse.contains(coord)) {
							p.sendMessage(ChatColor.RED + "This machine is currently being used!");
						} else {
							if(stats.ronbux >= 100) {
								takeRonbux(p, 100);
								p.sendMessage(ChatColor.GREEN + "The CreeperSlots machine begins spinning...");
								inuse.add(coord);
								playing.add(p.getName());
								plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
									public int count = 30;
									public boolean win = false;
									public int winAmount = 0;
									public String winLetter = "";
									public String winDisplay = "";
									public String a,b,c;
									public void run() {
										if(count == 30) {
											Collections.shuffle(cslots);
											for(Slot slot : cslots) {
												if(Math.random() < slot.rarity) {
													win = true;
													winLetter = slot.id;
													winAmount = slot.worth;
													winDisplay = ChatColor.getLastColors(slot.id) + slot.name;
													break;
												}
											}
										}
										String roll = "";
										if(count > 20) {
											roll = "?-?-?";
										} else if(count == 20) {
											if(win) {
												a = winLetter + ChatColor.RESET;
											} else {
												a = cslots.get((int)(Math.random() * cslots.size())).id + ChatColor.RESET;
											}
											roll = a + "-?-?";
										} else if(count > 10) {
											roll = a + "-?-?";
										} else if(count == 10) {
											if(win) {
												b = winLetter + ChatColor.RESET;
											} else {
												b = cslots.get((int)(Math.random() * cslots.size())).id + ChatColor.RESET;
											}
											roll = a + "-" + b +"-?";
										} else if(count > 0) {
											roll = a + "-" + b +"-?";
										} else if(count == 0) {
											if(win) {
												c = winLetter + ChatColor.RESET;
											} else {
												do {
													c = cslots.get((int)(Math.random() * cslots.size())).id + ChatColor.RESET;
												} while(c.equals(a) && c.equals(b));
											}
											roll = a + "-" + b +"-" + c;
											if(win) {
												p.sendMessage(ChatColor.GOLD + "CONGRATULATIONS!");
												p.sendMessage(ChatColor.GREEN + "You matched 3 " + winDisplay + ChatColor.GREEN + " for a prize of " + winAmount + " Ronbux!");
												giveRonbux(p, winAmount);
											} else {
												p.sendMessage(ChatColor.RED + "Aww... You didn't win anything.");
											}
										} else if(count < 0) {
											roll = a + "-" + b +"-" + c;
										}
										while(roll.contains("?"))
											roll = Pattern.compile("\\?").matcher(roll).replaceFirst(Matcher.quoteReplacement(cslots.get((int)(Math.random() * cslots.size())).id) + ChatColor.RESET);
										sign.setLine(3, roll);
										sign.update();
										if(count > -10 && p.isValid()) {
											plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 4);
										} else {
											sign.setLine(3, "?-?-?");
											sign.update();
											inuse.remove(coord);
											playing.remove(p.getName());
										}
										count--;
									}
								});
							} else {
								p.sendMessage(ChatColor.RED + "You need 100 Ronbux to play CreeperSlots!");
							}
						}
					} else if(sign.getLine(0).contains("Wheel of Fun")) {
						
					} else if(sign.getLine(0).contains("Jungle Roll")) {
						if(stats.ronbux >= 10) {
							if(inuse.contains(coord)) {
								p.sendMessage(ChatColor.RED + "This machine is currently being used!");
							} else {
								Block leaves = null;
								for(int dx = -1; dx <= 1; dx++) {
									for(int dz = -1; dz <= 1; dz++) {
										if(block.getLocation().add(dx, 0, dz).getBlock().getType() == Material.LEAVES) {
											leaves = block.getLocation().add(dx, 0, dz).getBlock();
											break;
										}
									}
								}
								if(leaves != null) {
									p.sendMessage(ChatColor.GREEN + "The Jungle Roll has begun!");
									takeRonbux(p, 10);
									final Location loc = leaves.getLocation().add(0.5, 1.2, 0.5);
									inuse.add(coord);
									final String name = p.getName();
									playing.add(name);
									final Item item = loc.getWorld().dropItem(loc, new ItemStack(jungleMats[(int)(Math.random() * jungleMats.length)]));
									noPickup.add(item.getUniqueId());
									plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
										public int count = 30;
										public void run() {
											if(count >= 0)
												item.setItemStack(new ItemStack(jungleMats[(int)(Math.random() * jungleMats.length)]));
											item.teleport(loc);
											if(count > 20) {
												plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 5);
											} else if(count > 15) {
												plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 8);
											} else if(count > 10) {
												plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 12);
											} else if(count > 5) {
												plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 17);
											} else if(count > 0) {
												plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 23);
											} else if (count == 0) {
												plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 20);
												int value = 5;
												if(jungleValues.containsKey(item.getItemStack().getType())) {
													value = jungleValues.get(item.getItemStack().getType());
												}
												p.sendMessage(ChatColor.GREEN + "You rolled an item worth " + ChatColor.GOLD + value + ChatColor.GREEN + " Ronbux!");
												giveRonbux(p, value);
											} else if(count >= -3) {
												plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 20);
											} else if(count < -3) {
												item.remove();
												inuse.remove(coord);
												playing.remove(name);
											}
											count--;
										}
									});
								} else {
									p.sendMessage(ChatColor.RED + "Error: Could not find leaf block! Please report this to Ronbo.");
								}
							}
						} else {
							p.sendMessage(ChatColor.RED + "You need 10 Ronbux to play Jungle Roll!");
						}
					}
				}
			}
		} catch(Exception e) {
			
		}
	}
	
	@EventHandler
	public void onpickup(PlayerPickupItemEvent event) {
		if(noPickup.contains(event.getItem().getUniqueId()))
			event.setCancelled(true);
	}
	
	public static class Coord {
		@Override
		public String toString() {
			return "[" + x + "," + y + "," + z + "]";
		}
		@Override
		public boolean equals(Object other) {
			if(other instanceof Coord) {
				Coord c = (Coord)other;
				return x == c.x && y == c.y && z == c.z;
			}
			return false;
		}
		@Override
		public int hashCode() {
			return x+y+z;
		}
		public int x,y,z;
		public Coord(Block b) {
			this(b.getX(), b.getY(), b.getZ());
		}
		public Coord(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
	
	public static void giveRonbux(Player p, int amount) {
		Stats stats = RonboLobby.getStats(p);
		if(stats != null) {
			stats.ronbuxEarned += amount;
			stats.ronbux += amount;
			p.sendMessage(ChatColor.GRAY + " >> +" + amount + " Ronbux");
		}
	}
	
	public static void takeRonbux(Player p, int amount) {
		Stats stats = RonboLobby.getStats(p);
		if(stats != null) {
			stats.ronbuxEarned -= amount;
			stats.ronbux -= amount;
			p.sendMessage(ChatColor.DARK_GRAY + " >> -" + amount + " Ronbux");
		}
	}
	
	public static RonboLobby plugin;
	
	public CasinoManager(RonboLobby plugin) {
		CasinoManager.plugin = plugin;
	}
	
}
