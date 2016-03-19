package xronbo.common;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

import me.ronbo.core.SQLManager;
import net.minecraft.server.v1_7_R4.EntityArrow;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftArrow;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xronbo.ronbolobby.RonboLobby;

public class ArcheryManager implements Listener {

	public static ItemStack bow,arrow;
	public static boolean loaded = false;

	public static void load() {
		loaded = true;
		ItemMeta im;

		bow = new ItemStack(Material.BOW);
		im = bow.getItemMeta();
		im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		im.setDisplayName(ChatColor.GOLD + "Training Bow");
		im.setLore(Arrays.asList(new String[] {ChatColor.GREEN + "For use in the Archery Range."}));
		bow.setItemMeta(im);

		arrow = new ItemStack(Material.ARROW);
		im = arrow.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "Training Arrow");
		im.setLore(Arrays.asList(new String[] {ChatColor.GREEN + "What a nice arrow!"}));
		arrow.setItemMeta(im);
	}

	public static void supply(Player p) {
		if(!loaded)
			load();
		p.getInventory().remove(Material.BOW);
		p.getInventory().addItem(bow);
		for(int k = 9; k < p.getInventory().getSize(); k++) {
			if(p.getInventory().getItem(k) == null || p.getInventory().getItem(k).getType() == Material.AIR || p.getInventory().getItem(k).getType() == Material.ARROW) {
				p.getInventory().setItem(k, arrow);
				break;
			}
		}
		p.sendMessage("");
		p.sendMessage(ChatColor.GOLD + "Master Archer" + ChatColor.WHITE + ": " + ChatColor.LIGHT_PURPLE + "They say I was destined to be an archer...");
		p.sendMessage(ChatColor.GOLD + "Master Archer" + ChatColor.WHITE + ": " + ChatColor.LIGHT_PURPLE + "But, you look like you have potential too!");
		p.sendMessage(ChatColor.GOLD + "Master Archer" + ChatColor.WHITE + ": " + ChatColor.LIGHT_PURPLE + "Try to shoot some " + ChatColor.WHITE + "Glass Targets" + ChatColor.LIGHT_PURPLE + "!");
	}

	public static HashMap<String, Integer> glassHit = new HashMap<String, Integer>();

	@EventHandler
	public void onProjectileHit(final ProjectileHitEvent event) {
		Player p = (Player) event.getEntity().getShooter();
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				event.getEntity().remove();
			}
		}, 10);
		if(p != null) {
			if(event.getEntityType() == EntityType.ARROW) {
				final String playerName = p.getName();
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						try {
							EntityArrow entityArrow = ((CraftArrow)event.getEntity()).getHandle();
							Field fieldX = EntityArrow.class.getDeclaredField("d");
							Field fieldY = EntityArrow.class.getDeclaredField("e");
							Field fieldZ = EntityArrow.class.getDeclaredField("f");
							fieldX.setAccessible(true);
							fieldY.setAccessible(true);
							fieldZ.setAccessible(true);
							int x = fieldX.getInt(entityArrow);
							int y = fieldY.getInt(entityArrow);
							int z = fieldZ.getInt(entityArrow);
							if(x != 1 && y != 1 && z != 1) {
								if(!(x > -700 && x < -628 && z > 718 && z < 760)) {
									Block block = event.getEntity().getWorld().getBlockAt(x, y, z);
									if(block.getType() == Material.STAINED_GLASS && block.getData() != DyeColor.GRAY.getWoolData()) {
										final int[] loc = new int[3];
										loc[0] = block.getLocation().getBlockX();
										loc[1] = block.getLocation().getBlockY();
										loc[2] = block.getLocation().getBlockZ();
										final Material material = block.getType();
										brokenGlass.put(loc, material);
										plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
											public void run() {
												new Location(plugin.getServer().getWorld("world"), loc[0], loc[1], loc[2]).getBlock().setType(material);
											}
										}, (int)((Math.random() * 120) + 40) * 10);
										entityArrow.getBukkitEntity().remove();
										Player p = plugin.getServer().getPlayerExact(playerName);
										if(p != null) {
											p.playSound(p.getLocation(), Sound.GLASS, 6, 1);
											if(glassHit.containsKey(p.getName())) {
												glassHit.put(p.getName(), glassHit.get(p.getName()) + 1);
											} else {
												glassHit.put(p.getName(), 1);
											}
											int amount = glassHit.get(p.getName());
											p.sendMessage(ChatColor.GOLD + "[Archery] Score! You've managed to hit " + amount + " targets!");
											if(amount % 10 == 0) {
												broadcastArcheryMessage(amount / 10, p.getName());
											}
										}
										block.getDrops().clear();
										block.breakNaturally();
									}
								}
							}
						}
						catch (Exception e){
							e.printStackTrace();
						}
					}
				}, 1);
			}
		}
	}

	public static HashMap<int[], Material> brokenGlass = new HashMap<int[], Material>();

	public static String[] verbs = {"sniped", "shot", "hit", "destroyed", "broken"};

	public static void broadcastArcheryMessage(int num, String name) {
		if(num > 0 && num % 2 == 0) {
			if(plugin.getServer().getPlayerExact(name) != null) {
				plugin.getServer().getPlayerExact(name).sendMessage(ChatColor.GREEN + "You earned 1 Gem for hitting 20 targets!");
				if(RonboLobby.stats.containsKey(name))
					RonboLobby.stats.get(name).points++;
			}
			SQLManager.execute("update playerdata set points = points + 1 where name = '" + name + "'");
		}
		String message = ChatColor.GOLD + name + " has " + verbs[(int)(Math.random() * verbs.length)] + " " + (num * 10) + " targets! ";
		switch(num) {
		case 1:
			message += "Not bad!";
			break;
		case 2:
			message += "Wow! That's a lot!";
			break;
		case 3:
			message += "Great archery training!";
			break;
		case 4:
			message += "40 targets is really impressive!";
			break;
		case 5:
			message += "I guess they're pretty bored...";
			break;
		case 6:
			message += "Might as well make a server just for archery...";
			break;
		default:
			message += "What a pro!";
			break;
		}
		plugin.getServer().broadcastMessage(message);
	}

	public static RonboLobby plugin;
	public ArcheryManager(RonboLobby plugin) {
		ArcheryManager.plugin = plugin;
	}

}
