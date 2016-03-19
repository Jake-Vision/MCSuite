package xronbo.ronbolobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import me.ronbo.core.SQLManager;
import me.ronbo.core.ranks.RankManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xronbo.common.Values;
import xronbo.ronbolobby.effects.EffectCreator;
import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.ParticleDetails;
import xronbo.ronbolobby.effects.ParticleType;

public class EffectManager implements Listener {
	
	public static final String[][] effects = {
		/*
		 * Noob Tier
		 */
		{"Crit Stars", "IRON_SWORD", "For true warriors.", "30 Gems"},
		{"Winter", "SNOW_BLOCK", "Brr...", "30 Gems"},
		{"Rainbow Swirls", "GLOWSTONE", "Pretty!", "30 Gems"},
		{"Flame Trail", "BLAZE_ROD", "Become a Fire Lord!", "60 Gems"},
		{"Heart Trail", "APPLE", "Floating hearts!", "60 Gems"},
		{"Anger Trail", "COOKIE", "For when you're feeling mad.", "60 Gems"},
		{"Stone Breaker", "STONE", "For true miners.", "60 Gems"},
		{"Cloud Walker", "FEATHER", "Cloudy!", "100 Gems"},
		{"Purple Sparks", "LAPIS_BLOCK", "Also known as T5 Wand magic!", "150 Gems"},
		{"Ancient Runes", "MOSSY_COBBLESTONE", "What does it mean?", "150 Gems"},
		{"Sparkler", "FIREWORK", "Ahh so sparkly!", "150 Gems"},
		{"Mystery Trail", "STONE", "It's a mystery...", "200 Gems"},
		{"Dark Smoke", "OBSIDIAN", "*cough* *cough*", "200 Gems"},
		{"Sizzling", "LAVA_BUCKET", "Oooh, hot!", "300 Gems"},
		{"Music Lover", "JUKEBOX", "For true musicians.", "500 Gems"},
	};
	public static final HashMap<String, HashSet<ParticleDetails>> effectnames = new HashMap<String, HashSet<ParticleDetails>>();
	static {
		HashSet<ParticleDetails> p = new HashSet<ParticleDetails>();
		p.add(new ParticleDetails(ParticleType.FIRE));
		effectnames.put("Flame Trail", p);
		p = new HashSet<ParticleDetails>();
		p.add(new ParticleDetails(ParticleType.HEART));
		effectnames.put("Heart Trail", p);
		p = new HashSet<ParticleDetails>();
		p.add(new ParticleDetails(ParticleType.COOKIE));
		effectnames.put("Anger Trail", p);
		p = new HashSet<ParticleDetails>();
		p.add(new ParticleDetails(ParticleType.BLOCKBREAK));
		effectnames.put("Stone Breaker", p);
		p = new HashSet<ParticleDetails>();
		p.add(new ParticleDetails(ParticleType.CLOUD));
		effectnames.put("Cloud Walker", p);
		p = new HashSet<ParticleDetails>();
		p.add(new ParticleDetails(ParticleType.CRITICAL));
		effectnames.put("Crit Stars", p);
		p = new HashSet<ParticleDetails>();
		p.add(new ParticleDetails(ParticleType.EMBER));
		effectnames.put("Sizzling", p);
		p = new HashSet<ParticleDetails>();
		p.add(new ParticleDetails(ParticleType.ENDER));
		p.add(new ParticleDetails(ParticleType.PORTAL));
		p.add(new ParticleDetails(ParticleType.MCVOID));
		effectnames.put("Mystery Trail", p);
		p = new HashSet<ParticleDetails>();
		p.add(new ParticleDetails(ParticleType.MAGIC));
		effectnames.put("Purple Sparks", p);
		p = new HashSet<ParticleDetails>();
		p.add(new ParticleDetails(ParticleType.NOTE));
		effectnames.put("Music Lover", p);
		p = new HashSet<ParticleDetails>();
		p.add(new ParticleDetails(ParticleType.RAINBOWSWIRL));
		effectnames.put("Rainbow Swirls", p);
		p = new HashSet<ParticleDetails>();
		p.add(new ParticleDetails(ParticleType.RUNES));
		effectnames.put("Ancient Runes", p);
		p = new HashSet<ParticleDetails>();
		p.add(new ParticleDetails(ParticleType.SMOKE));
		effectnames.put("Dark Smoke", p);
		p = new HashSet<ParticleDetails>();
		p.add(new ParticleDetails(ParticleType.SNOW));
		effectnames.put("Winter", p);
		p = new HashSet<ParticleDetails>();
		p.add(new ParticleDetails(ParticleType.SPARKLE));
		effectnames.put("Sparkler", p);
	}
	
	public static void showMenu(Player p) {
		Inventory inventory = Bukkit.createInventory(p, 9 * 6, ChatColor.BLACK + "           " + ChatColor.UNDERLINE + "Kastia Effects");
		ItemStack item = null;
		ItemMeta im = null;
		ArrayList<String> lore = null;
		Stats stats = RonboLobby.getStats(p);
		
		item = new ItemStack(Material.BOOK);
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Effects Guide");
		lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "--------------");
		lore.add(ChatColor.GOLD + "Your active effect:");
		lore.add(ChatColor.YELLOW + (stats.activeeffect == null || stats.activeeffect.length() == 0 ? "None" : stats.activeeffect));
		lore.add(ChatColor.GRAY + "--------------");
		lore.add(ChatColor.GOLD + "Using Effects");
		lore.add(ChatColor.YELLOW + "Buy effects with Gems!");
		lore.add("");
		lore.add(ChatColor.YELLOW + "Then, turn on your effect");
		lore.add(ChatColor.YELLOW + "by clicking in this menu!");
		im.setLore(lore);
		item.setItemMeta(im);
		inventory.setItem(0, Values.removeAttributes(item));

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLUE.getData());
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Noble Perks!");
		lore = new ArrayList<String>();
		lore.add(ChatColor.GOLD + "Nobles " + ChatColor.YELLOW + "and " + ChatColor.GOLD + "Royals " + ChatColor.YELLOW + "get all");
		lore.add(ChatColor.YELLOW + "effects unlocked for free!");
		lore.add("");
		lore.add(ChatColor.YELLOW + "Get " + ChatColor.GOLD + "Noble " + ChatColor.YELLOW + "or " + ChatColor.GOLD + "Royal " + ChatColor.YELLOW + "at");
		lore.add(ChatColor.YELLOW + "store.kastia.net!");
		im.setLore(lore);
		item.setItemMeta(im);
		inventory.setItem(9, Values.removeAttributes(item));

		int index = 1;
		
		for(String[] s : effects) {
			item = new ItemStack(Material.valueOf(s[1]));
			im = item.getItemMeta();
			im.setDisplayName(ChatColor.AQUA + s[0]);
			lore = new ArrayList<String>();
			lore.add(ChatColor.GRAY + "--------------");
			lore.add(ChatColor.YELLOW + s[2]);
			im.setLore(lore);
			item.setItemMeta(im);
			inventory.setItem(index, Values.removeAttributes(item));
			
			int status = 0;
			if(RankManager.check(p, "noble") || stats.hubunlocks.contains(s[0].replaceAll(" ", "_"))) {
				if(stats.activeeffect.equals(s[0].replaceAll(" ", "_"))) {
					status = 2;
				} else {
					status = 1;
				}
			}
			if(status == 0) {
				item = new ItemStack(Material.INK_SACK, 1, DyeColor.GRAY.getDyeData());
				im = item.getItemMeta();
				im.setDisplayName(ChatColor.GRAY + s[0] + " - Not Owned");
				lore = new ArrayList<String>();
				lore.add(ChatColor.AQUA + "Click to Buy!");
				lore.add(ChatColor.GRAY + "--------------");
				lore.add(ChatColor.GOLD + "Price: " + ChatColor.YELLOW + s[3]);
				im.setLore(lore);
				item.setItemMeta(im);
				inventory.setItem(index + 9, Values.removeAttributes(item));
			} else if(status == 1) {
				item = new ItemStack(Material.INK_SACK, 1, DyeColor.MAGENTA.getDyeData());
				im = item.getItemMeta();
				im.setDisplayName(ChatColor.GRAY + s[0] + " - Owned");
				lore = new ArrayList<String>();
				lore.add(ChatColor.RED + "Inactive");
				lore.add(ChatColor.GRAY + "--------------");
				lore.add(ChatColor.AQUA + "Click to use your " + s[0] + "!");
				im.setLore(lore);
				item.setItemMeta(im);
				inventory.setItem(index + 9, Values.removeAttributes(item));
			} else if(status == 2) {
				item = new ItemStack(Material.INK_SACK, 1, DyeColor.LIME.getDyeData());
				im = item.getItemMeta();
				im.setDisplayName(ChatColor.GRAY + s[0] + " - Active!");
				lore = new ArrayList<String>();
				lore.add(ChatColor.AQUA + s[0] + " is your active effect!");
				lore.add(ChatColor.GRAY + "Click to disable effects.");
				im.setLore(lore);
				item.setItemMeta(im);
				inventory.setItem(index + 9, Values.removeAttributes(item));
			}
			index++;
			if(index % 9 == 0)
				index += 9;
		}
		
		p.openInventory(inventory);
	}
	
	@EventHandler
	public void onclick(InventoryClickEvent event) {
		try {
			if(event.getInventory().getName().contains("Kastia Effects")) {
				event.setCancelled(true);
				ItemStack item = event.getCurrentItem();
				Player p = (Player)event.getWhoClicked();
				Stats stats = RonboLobby.getStats(p);
				if(item.getItemMeta().getDisplayName().contains("Not Owned")) {
					int price = -1;
					for(String s : item.getItemMeta().getLore()) {
						if(s.contains("Price:")) {
							price = Integer.parseInt(ChatColor.stripColor(s).replaceAll("[^0-9]", ""));
							break;
						}
					}
					String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
					name = name.substring(0, name.indexOf(" -"));
					if(price > -1) {
						if(stats.points >= price) {
							stats.points -= price;
							stats.hubunlocks.add(name.replaceAll(" ", "_"));
							SQLManager.execute("update playerdata set points = points - " + price + ", hubunlocks = concat(hubunlocks, ' " + name.replaceAll(" ", "_") + "') where uuid = '" + stats.uuid + "'");
							p.sendMessage(ChatColor.GREEN + "You bought " + name + "!");
							p.sendMessage(ChatColor.GREEN + "Use /effects to activate it!");
						} else {
							p.sendMessage(ChatColor.RED + "You need " + price + " Gems to buy that!");
						}
					}
					p.closeInventory();
					showMenu(p);
				} else if(item.getItemMeta().getDisplayName().contains("Owned")) {
					String name = ChatColor.stripColor(item.getItemMeta().getLore().toString());
					name = name.substring(name.indexOf("your ") + "your ".length(), name.indexOf("!"));
					stats.activeeffect = name.replaceAll(" ", "_");
					SQLManager.execute("update playerdata set hubactiveeffect = '" + name.replaceAll(" ", "_") + "' where uuid = '" + stats.uuid + "'");
					p.closeInventory();
					showMenu(p);
					p.sendMessage(ChatColor.GREEN + "You switched your active effect to " + name + "!");
					activate(p);
				} else if(item.getItemMeta().getDisplayName().contains("Active")) {
					stats.activeeffect = "";
					SQLManager.execute("update playerdata set hubactiveeffect = '' where uuid = '" + stats.uuid + "'");
					p.closeInventory();
					showMenu(p);
					p.sendMessage(ChatColor.GREEN + "Your effect is now disabled.");
					details.remove(p.getName());
				}
			}
		} catch(Exception e) {
			
		}
	}
	
	public static HashMap<String, HashSet<ParticleDetails>> details = new HashMap<String, HashSet<ParticleDetails>>();
	public static HashMap<String, Integer> tasks = new HashMap<String, Integer>();
	
	public static void activate(Player p) {
		Stats stats = RonboLobby.getStats(p);
		if(stats != null) {
			if(stats.activeeffect != null && stats.activeeffect.length() > 0) {
				HashSet<ParticleDetails> pd = effectnames.get(stats.activeeffect.replaceAll("_", " "));
				if(pd != null) {
					details.put(p.getName(), pd);
				}
				if(!tasks.containsKey(p.getName())) {
					final String name = p.getName();
					tasks.put(p.getName(), plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							if(plugin.getServer().getPlayerExact(name) == null) {
								tasks.remove(name);
								details.remove(name);
							} else {
								HashSet<ParticleDetails> particles = details.get(name);
								if(particles == null) {
									tasks.remove(name);
									details.remove(name);
								} else {
									Player p = plugin.getServer().getPlayerExact(name);
									EffectHolder holder = EffectCreator.createLocHolder(particles, p.getLocation().add(p.getLocation().getDirection().normalize().multiply(-0.75)));
									holder.setRunning(true);
									holder.update();
									holder.setRunning(false);
									plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 15);
								}
							}
						}
					}));
				}
			}
		}
	}
	
	public EffectManager(RonboLobby plugin) {
		EffectManager.plugin = plugin;
	}
	
	public static RonboLobby plugin;
}
