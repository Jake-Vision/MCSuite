package xronbo.ronbolobby;

import java.util.ArrayList;
import java.util.HashMap;

import me.ronbo.core.SQLManager;
import me.ronbo.core.ranks.RankManager;
import net.minecraft.server.v1_7_R4.EntityInsentient;
import net.minecraft.server.v1_7_R4.EntityLiving;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xronbo.common.Values;
import xronbo.ronbolobby.entitytypes.CustomCaveSpider;
import xronbo.ronbolobby.entitytypes.CustomChicken;
import xronbo.ronbolobby.entitytypes.CustomCow;
import xronbo.ronbolobby.entitytypes.CustomCreeper;
import xronbo.ronbolobby.entitytypes.CustomIronGolem;
import xronbo.ronbolobby.entitytypes.CustomMushroomCow;
import xronbo.ronbolobby.entitytypes.CustomOcelot;
import xronbo.ronbolobby.entitytypes.CustomPig;
import xronbo.ronbolobby.entitytypes.CustomPigZombie;
import xronbo.ronbolobby.entitytypes.CustomSheep;
import xronbo.ronbolobby.entitytypes.CustomSkeleton;
import xronbo.ronbolobby.entitytypes.CustomSpider;
import xronbo.ronbolobby.entitytypes.CustomWolf;
import xronbo.ronbolobby.entitytypes.CustomZombie;

public class PetManager implements Listener {
	
	public static final String[][] pets = {
		/*
		 * Noob Tier
		 */
		{"Chicken", "EGG", "A cute chicken!", "500 Gems"},
		{"Pig", "PORK", "Oink oink!", "500 Gems"},
		{"Cow", "LEATHER", "Moooooo", "500 Gems"},
		{"Sheep", "SHEARS", "Just a sheep.", "500 Gems"},
		/*
		 * Not as noob tier
		 */
		{"Baby Chicken", "FEATHER", "Aww, what a cutie.", "700 Gems"},
		{"Baby Pig", "APPLE", "Born to do good!", "700 Gems"},
		{"Baby Cow", "MILK_BUCKET", "Moo. Moo moo?", "700 Gems"},
		{"Baby Sheep", "WOOL", "Baaa..", "700 Gems"},
		{"Spider", "WEB", "Hiss.. Scary!", "800 Gems"},
		{"Mooshroom", "BROWN_MUSHROOM", "So red!", "800 Gems"},
		/*
		 * Average Tier
		 */
		{"Cave Spider", "SPIDER_EYE", "Poisonous!", "1100 Gems"},
		{"Skeleton", "SKULL_ITEM", "A true nightmare.", "1200 Gems"},
		{"Zombie Pigman", "GOLD_SWORD", "Squeak squeak.", "1200 Gems"},
		{"Zombie", "ROTTEN_FLESH", "A true classic.", "1200 Gems"},
		{"Wolf", "RAW_BEEF", "Always loyal!", "1300 Gems"},
		{"Ocelot", "RAW_FISH", "Meowww!", "1300 Gems"},
		/*
		 * Cool Tier
		 */
		{"Baby Wolf", "BONE", "Cute but fierce!", "2000 Gems"},
		{"Baby Ocelot", "COOKED_FISH", "Mew, mew.", "2000 Gems"},
		{"Baby Zombie", "SLIME_BALL", "Not so scary.", "2500 Gems"},
		{"Baby Pigman", "GOLD_INGOT", "Kind of cute.", "2500 Gems"},
		{"Baby Mooshroom", "RED_MUSHROOM", "Ronbo's favorite!", "2800 Gems"},
		/*
		 * Godly Tier
		 */
		{"Creeper", "EMERALD_BLOCK", "Ssss... boom!", "3500 Gems"},
		{"Wither Skeleton", "OBSIDIAN", "A real big guy.", "4000 Gems"},
		{"Iron Golem", "IRON_BLOCK", "Defender of Villages", "5000 Gems"},
		{"Charged Creeper", "TNT", "RUN!", "5500 Gems"},
	};
	public static HashMap<String, Class<? extends EntityLiving>> classes = new HashMap<String, Class<? extends EntityLiving>>();
	static {
		classes.put("Chicken", CustomChicken.class);
		classes.put("Pig", CustomPig.class);
		classes.put("Cow", CustomCow.class);
		classes.put("Sheep", CustomSheep.class);
		classes.put("Baby Chicken", CustomChicken.class);
		classes.put("Baby Pig", CustomPig.class);
		classes.put("Baby Cow", CustomCow.class);
		classes.put("Baby Sheep", CustomSheep.class);
		classes.put("Spider", CustomSpider.class);
		classes.put("Mooshroom", CustomMushroomCow.class);
		classes.put("Cave Spider", CustomCaveSpider.class);
		classes.put("Skeleton", CustomSkeleton.class);
		classes.put("Zombie Pigman", CustomPigZombie.class);
		classes.put("Zombie", CustomZombie.class);
		classes.put("Wolf", CustomWolf.class);
		classes.put("Ocelot", CustomOcelot.class);
		classes.put("Baby Wolf", CustomWolf.class);
		classes.put("Baby Ocelot", CustomOcelot.class);
		classes.put("Baby Zombie", CustomZombie.class);
		classes.put("Baby Pigman", CustomPigZombie.class);
		classes.put("Baby Mooshroom", CustomMushroomCow.class);
		classes.put("Creeper", CustomCreeper.class);
		classes.put("Wither Skeleton", CustomSkeleton.class);
		classes.put("Iron Golem", CustomIronGolem.class);
		classes.put("Charged Creeper", CustomCreeper.class);
	}

	public static void showMenu(Player p) {
		Inventory inventory = Bukkit.createInventory(p, 9 * 6, ChatColor.BLACK + "               " + ChatColor.UNDERLINE + "Kastia Pets");
		ItemStack item = null;
		ItemMeta im = null;
		ArrayList<String> lore = null;
		Stats stats = RonboLobby.getStats(p);
		
		item = new ItemStack(Material.BOOK);
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Pet Guide");
		lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "--------------");
		lore.add(ChatColor.GOLD + "Your active pet:");
		lore.add(ChatColor.YELLOW + (stats.currentpet == null || stats.currentpet.length() == 0 ? "None" : stats.currentpet));
		lore.add(ChatColor.GRAY + "--------------");
		lore.add(ChatColor.GOLD + "Commands");
		lore.add(ChatColor.YELLOW + "/pet " + ChatColor.LIGHT_PURPLE + " - " + ChatColor.AQUA + "View this menu");
		lore.add(ChatColor.YELLOW + "/ride " + ChatColor.LIGHT_PURPLE + " - " + ChatColor.AQUA + "Ride your pet!");
		lore.add(ChatColor.GRAY + "--------------");
		lore.add(ChatColor.GOLD + "Getting a Pet");
		lore.add(ChatColor.YELLOW + "Buy pets with Gems!");
		lore.add("");
		lore.add(ChatColor.YELLOW + "Then, select your new pet");
		lore.add(ChatColor.YELLOW + "by clicking in this menu!");
		im.setLore(lore);
		item.setItemMeta(im);
		inventory.setItem(0, Values.removeAttributes(item));
		
		item = new ItemStack(Material.NAME_TAG);
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Name Your Pet");
		lore = new ArrayList<String>();
		lore.add(ChatColor.GOLD + "Current Name:");
		lore.add(ChatColor.YELLOW + stats.petname);
		lore.add(ChatColor.GRAY + "--------------");
		lore.add(ChatColor.YELLOW + "Rename your pet with");
		lore.add(ChatColor.YELLOW + "/name <NAME_HERE>!");
		lore.add(ChatColor.GRAY + "--------------");
		lore.add(ChatColor.YELLOW + "Changing your pet's");
		lore.add(ChatColor.YELLOW + "name costs " + ChatColor.GOLD + "15 Gems" + ChatColor.YELLOW + ".");
		lore.add(ChatColor.GRAY + "--------------");
		lore.add(ChatColor.YELLOW + "Inappropriate names will");
		lore.add(ChatColor.YELLOW + "be changed without warning!");
		im.setLore(lore);
		item.setItemMeta(im);
		inventory.setItem(9, Values.removeAttributes(item));

		item = new ItemStack(Material.GOLDEN_APPLE, 1, (short)1);
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "Royals get all pets!");
		lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "--------------");
		lore.add(ChatColor.YELLOW + "Players who are " + ChatColor.GOLD + "Royals");
		lore.add(ChatColor.YELLOW + "get all pets unlocked - free!");
		lore.add(ChatColor.GRAY + "--------------");
		lore.add(ChatColor.YELLOW + "Buy Royal at store.kastia.net!");
		lore.add(ChatColor.YELLOW + "We greatly appreciate all support!");
		im.setLore(lore);
		item.setItemMeta(im);
		inventory.setItem(44, Values.removeAttributes(item));

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLUE.getData());
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "Ender Dragon");
		lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "--------------");
		lore.add(ChatColor.YELLOW + "Players who are " + ChatColor.GOLD + "Royals");
		lore.add(ChatColor.YELLOW + "get to use /dragon!");
		lore.add(ChatColor.GRAY + "--------------");
		lore.add(ChatColor.YELLOW + "Ride and control your own");
		lore.add(ChatColor.YELLOW + "Ender Dragon all around the hub!");
		im.setLore(lore);
		item.setItemMeta(im);
		inventory.setItem(53, Values.removeAttributes(item));
		
		int index = 1;
		
		for(String[] s : pets) {
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
			if(RankManager.check(p, "royal") || stats.hubunlocks.contains(s[0].replaceAll(" ", "_"))) {
				if(stats.currentpet.equals(s[0].replaceAll(" ", "_"))) {
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
				lore.add(ChatColor.AQUA + s[0] + " is your active pet!");
				lore.add(ChatColor.GRAY + "Click to hide your pet.");
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
			if(event.getInventory().getName().contains("Kastia Pets")) {
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
							p.sendMessage(ChatColor.GREEN + "You bought the pet " + name + "!");
							p.sendMessage(ChatColor.GREEN + "Use /pet to activate it!");
						} else {
							p.sendMessage(ChatColor.RED + "You need " + price + " Gems to buy that!");
						}
					}
					p.closeInventory();
					showMenu(p);
				} else if(item.getItemMeta().getDisplayName().contains("Owned")) {
					String name = ChatColor.stripColor(item.getItemMeta().getLore().toString());
					name = name.substring(name.indexOf("your ") + "your ".length(), name.indexOf("!"));
					stats.currentpet = name.replaceAll(" ", "_");
					SQLManager.execute("update playerdata set hubactivepet = '" + name.replaceAll(" ", "_") + "' where uuid = '" + stats.uuid + "'");
					p.closeInventory();
					showMenu(p);
					p.sendMessage(ChatColor.GREEN + "You switched your active pet to " + name + "!");
					spawnPet(p);
				} else if(item.getItemMeta().getDisplayName().contains("Active")) {
					stats.currentpet = "";
					SQLManager.execute("update playerdata set hubactivepet = '' where uuid = '" + stats.uuid + "'");
					p.closeInventory();
					showMenu(p);
					p.sendMessage(ChatColor.GREEN + "Your pet is now hidden.");
					spawnPet(p);
				}
			}
		} catch(Exception e) {
			
		}
	}
	
	public static HashMap<String, Entity> spawnedPets = new HashMap<String, Entity>();
	public static HashMap<String, Integer> tasks = new HashMap<String, Integer>();
	public static void spawnPet(Player p) {
		if(p == null || !p.isValid() || !p.isOnline())
			return;
		if(spawnedPets.containsKey(p.getName()))
			spawnedPets.get(p.getName()).remove();
		Stats stats = RonboLobby.getStats(p);
		if(stats != null && stats.currentpet != null) {
			if(stats.currentpet.length() > 0) {
				Class<?> petclass = classes.get(stats.currentpet.replaceAll("_", " "));
				LivingEntity le = RonboLobby.createPet(petclass, p.getLocation());
				le.setCustomName(stats.petname);
				le.setCustomNameVisible(true);
				if(le instanceof Ageable) {
					if(stats.currentpet.toLowerCase().contains("baby")) {
						((Ageable)le).setBaby();
						((Ageable)le).setAgeLock(true);
					} else {
						((Ageable)le).setAdult();
					}
				}
				if(le instanceof Zombie) {
					if(stats.currentpet.toLowerCase().contains("baby")) {
						((Zombie)le).setBaby(true);
					}
				}
				if(stats.currentpet.toLowerCase().contains("wither")) {
					((Skeleton)le).setSkeletonType(SkeletonType.WITHER);
				}
				if(stats.currentpet.toLowerCase().contains("charged")) {
					((Creeper)le).setPowered(true);
				}
				spawnedPets.put(p.getName(), le);
				if(!tasks.containsKey(p.getName())) {
					final String name = p.getName();
					tasks.put(p.getName(), plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							if(plugin.getServer().getPlayerExact(name) == null) {
								if(spawnedPets.containsKey(name))
									spawnedPets.get(name).remove();
								spawnedPets.remove(name);
								tasks.remove(name);
							} else {
								if(spawnedPets.containsKey(name)) {
									Location loc = plugin.getServer().getPlayerExact(name).getLocation();
									LivingEntity le = (LivingEntity)spawnedPets.get(name);
									((EntityInsentient) ((CraftLivingEntity) le).getHandle()).getNavigation().a(loc.getX(), loc.getY(), loc.getZ(), 1.4d);
									try {
										if(le.getLocation().distanceSquared(loc) > 200)
											le.teleport(loc);
									} catch(Exception e) {
										
									}
								}
								plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 5);
							}
						}
					}));
				}
			}
		}
	}
	
	public PetManager(RonboLobby plugin) {
		PetManager.plugin = plugin;
	}
	
	public static RonboLobby plugin;
}
