package xronbo.ronbolobby;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import me.ronbo.core.SQLManager;
import me.ronbo.core.ranks.ChatManager;
import me.ronbo.core.ranks.RankManager;
import net.minecraft.server.v1_7_R4.EntityLiving;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import xronbo.common.ArcheryManager;
import xronbo.common.GeneralManager;
import xronbo.common.GrappleManager;
import xronbo.common.Values;
import xronbo.ronbolobby.bungee.ChannelManager;
import xronbo.ronbolobby.bungee.FactionsManager;
import xronbo.ronbolobby.bungee.Minigame;
import xronbo.ronbolobby.bungee.MinigameManager;
import xronbo.ronbolobby.entitytypes.CustomEnderDragon;
import xronbo.ronbolobby.entitytypes.CustomEntityType;
import xronbo.ronbolobby.entitytypes.CustomVillager;
import xronbo.ronbolobby.hpbar.HPBar;

public class RonboLobby extends JavaPlugin implements CommandExecutor, Listener {
	
	public static ChatColor[] colors = {
		ChatColor.AQUA,
		ChatColor.BLUE,
		ChatColor.DARK_GREEN,
		ChatColor.GOLD,
		ChatColor.GREEN,
		ChatColor.LIGHT_PURPLE,
		ChatColor.RED,
		ChatColor.YELLOW,
		ChatColor.DARK_AQUA,
	};
	
	public static ItemStack compass = null;
	public static ItemStack torch = null;
	public static ItemStack book = null;
	public static ItemStack emerald = null;
	public static ItemStack star = null;
	public static ItemStack clock = null;
	public static ItemStack jade = null;
	public static ItemStack ruby = null;
	public static ItemStack onyx = null;
	public static ItemStack factionsmenu = null;

	public static String serverName;
	
	public void onEnable() {
		plugin = this;
		CustomEntityType.registerEntities();
		RewardsManager.plugin = this;
		Stats.plugin = this;
		boolean found = false;
		for(File f : new File("./plugins/").listFiles()) {
			if(f.getName().startsWith("server_")) {
				serverName = f.getName().substring("server_".length());
				System.out.println("This server is " + serverName);
				found = true;
				break;
			}
		}
		if(!found) {
			getServer().broadcastMessage(ChatColor.RED + "FAILED TO FIND SERVER ID!");
			getServer().shutdown();
		}
		ChatManager.chatCooldown = 10000;
		plugin.getServer().createWorld(new WorldCreator("fhub"));
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new HPBar(this), this);
		getServer().getPluginManager().registerEvents(new GeneralManager(this), this);
		getServer().getPluginManager().registerEvents(new ArcheryManager(this), this);
		getServer().getPluginManager().registerEvents(new GrappleManager(this), this);
		getServer().getPluginManager().registerEvents(new GemShopManager(this), this);
		getServer().getPluginManager().registerEvents(new PetManager(this), this);
		getServer().getPluginManager().registerEvents(new EffectManager(this), this);
		getServer().getPluginManager().registerEvents(new MinigameManager(this), this);
		getServer().getPluginManager().registerEvents(new ChannelManager(this), this);
		getServer().getPluginManager().registerEvents(new FactionsManager(this), this);
		getServer().getPluginManager().registerEvents(new OptionsManager(this), this);
		getServer().getPluginManager().registerEvents(new HubManager(this), this);
		for(String s : commands)
			try {	
				getCommand(s).setExecutor(this);
			} catch(Exception e) {
				System.out.println("Error loading command " + s);
				e.printStackTrace();
			}
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		init();
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public int lastupdate = 0;
			public void run() {
				SQLManager.execute("replace into bungee values ('"+serverName+"', " + plugin.getServer().getOnlinePlayers().length + ", " + lastupdate++ + ", 1, 0, '')");
			}
		}, 20, 20);
		getLogger().info("Loaded Ronbo Lobby");
	}
	
	@EventHandler
	public void oninvclick(InventoryClickEvent event) {
		if(event.getWhoClicked().getGameMode() != GameMode.CREATIVE)
			event.setCancelled(true);
	}
	
	public static String[] welcomes = {
		"hey <name> welcome!",
		"welcome to Kastia <name>!!",
		"Welcome <name>!",
		"welcome!",
		"hi <name> welcome to Kastia!!",
		"<name>, welcome :D",
		"Hey <name>, to the Kastia!",
		"Welcome to Kastia <name>! :)",
		"Hey <name>, hope to see you around :)",
		"hey <name> welcome :D",
		"yo <name> welcome to Kastia :)"
	};
	
	public void welcome(String name) {
		Player p = plugin.getServer().getPlayer(name);
		if(p != null && p.isValid() && p.isOnline()) {
			if(plugin.getServer().getOnlinePlayers().length > 10) {
				Player p2;
				do {
					p2 = plugin.getServer().getOnlinePlayers()[(int)(Math.random() * plugin.getServer().getOnlinePlayers().length)];
				} while(p2 == null || p2.equals(p));
				int index = (int)(Math.random() * welcomes.length);
				String phrase = welcomes[index].replaceAll("<name>", Math.random() < 0.5 ? name.toLowerCase() : name);
				p.sendMessage(ChatManager.makeMessage(p2, phrase));
				int prev = index;
				Player prevP = p2;
				do {
					index = (int)(Math.random() * welcomes.length);
				} while(index == prev);
				Player p3;
				do {
					p3 = plugin.getServer().getOnlinePlayers()[(int)(Math.random() * plugin.getServer().getOnlinePlayers().length)];
				} while(p3 == null || p3.equals(prevP) || p3.equals(p));
				phrase = welcomes[index].replaceAll("<name>", Math.random() < 0.5 ? name.toLowerCase() : name);
				p.sendMessage(ChatManager.makeMessage(p3, phrase));
			}
		}
	}
	
	
	public Location getRandomSpawnLoc() {
		Location loc = plugin.getServer().getWorld("world").getSpawnLocation().add((int)(Math.random() * 5 - 2), 0, (int)(Math.random() * 5 - 2));
		loc.setYaw(-180);
		return loc;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if(p.getItemInHand() != null && p.getItemInHand().getType() == Material.COMPASS) {
			MinigameManager.openMinigamesMenu(p);
			event.setCancelled(true);
		} else if(p.getItemInHand() != null && p.getItemInHand().getType() == Material.WATCH) {
			setMainInventory(p);
			p.teleport(getRandomSpawnLoc());
		} else if(p.getItemInHand() != null && p.getItemInHand().getType() == Material.STAINED_CLAY) {
			if(p.getItemInHand().getData().getData() == DyeColor.LIME.getWoolData()) {
				if(FactionsManager.upA && (FactionsManager.playercountA < 100 || ((RankManager.check(p, "knight") || Stats.joinFullFactions.contains(p.getName())) && FactionsManager.playercountA < 250))) {
					p.sendMessage(ChatColor.GREEN + "Joining Factions Jade..." + ChatColor.GRAY + " (" + FactionsManager.playercountA + "/100 players)");
					FactionsManager.sendChannelChange(p, "factions1");
				} else if(FactionsManager.upA) {
					p.sendMessage(ChatColor.RED + "Factions Jade is full right now, sorry! " + ChatColor.GRAY + "(" + FactionsManager.playercountA + "/100 players)");
				} else {
					p.sendMessage(ChatColor.RED + "Factions Jade is currently down for maintenance. Sorry!");
				}
			} else if(p.getItemInHand().getData().getData() == DyeColor.GRAY.getWoolData()) {
				if(FactionsManager.upB && (FactionsManager.playercountB < 100 || ((RankManager.check(p, "knight") || Stats.joinFullFactions.contains(p.getName())) && FactionsManager.playercountB < 250))) {
					p.sendMessage(ChatColor.GREEN + "Joining Factions Onyx..." + ChatColor.GRAY + " (" + FactionsManager.playercountB + "/100 players)");
					FactionsManager.sendChannelChange(p, "factions2");
				} else if(FactionsManager.upB) {
					p.sendMessage(ChatColor.RED + "Factions Onyx is full right now, sorry! " + ChatColor.GRAY + "(" + FactionsManager.playercountB + "/100 players)");
				} else {
					p.sendMessage(ChatColor.RED + "Factions Onyx is currently down for maintenance. Sorry!");
				}
			} else if(p.getItemInHand().getData().getData() == DyeColor.RED.getWoolData()) {
				if(FactionsManager.upC && (FactionsManager.playercountC < 100 || ((RankManager.check(p, "knight") || Stats.joinFullFactions.contains(p.getName())) && FactionsManager.playercountC < 250))) {
					p.sendMessage(ChatColor.GREEN + "Joining Factions Ruby..." + ChatColor.GRAY + " (" + FactionsManager.playercountC + "/100 players)");
					FactionsManager.sendChannelChange(p, "factions3");
				} else if(FactionsManager.upC) {
					p.sendMessage(ChatColor.RED + "Factions Ruby is full right now, sorry! " + ChatColor.GRAY + "(" + FactionsManager.playercountC + "/100 players)");
				} else {
					p.sendMessage(ChatColor.RED + "Factions Ruby is currently down for maintenance. Sorry!");
				}
			}
		}
	}
	
	public void setMainInventory(Player p) {
		p.getInventory().clear();
		p.getInventory().setItem(0, compass);
		p.getInventory().setItem(1, torch);
		p.getInventory().setItem(4, book);
		p.getInventory().setItem(7, emerald);
		p.getInventory().setItem(8, star);
	}
	
	public void setFactionsInventory(Player p) {
		p.getInventory().clear();
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage(ChatColor.GRAY + "+-----------------------------+");
		p.sendMessage(ChatColor.GOLD + "    Kastia has 3 Factions servers!");
		p.sendMessage("");
		p.sendMessage(ChatColor.GREEN + "     Factions " + ChatColor.BOLD + "Jade" + ChatColor.GREEN + " (Original)");
		p.sendMessage(ChatColor.DARK_GRAY + "     Factions " + ChatColor.BOLD + "Onyx" + ChatColor.DARK_GRAY + " (OP)");
		p.sendMessage(ChatColor.RED + "     Factions " + ChatColor.BOLD + "Ruby" + ChatColor.RED + " (McMMO)");
		p.sendMessage(ChatColor.GRAY + "+-----------------------------+");
		p.getInventory().setItem(0, factionsmenu);
		p.getInventory().setItem(3, jade);
		p.getInventory().setItem(4, onyx);
		p.getInventory().setItem(5, ruby);
		p.getInventory().setItem(8, clock);
	}
	
	public void init() {
		ChannelManager.loadChannels();
		for(World w : plugin.getServer().getWorlds()) {
			w.setGameRuleValue("doFireTick", "false");
			w.setGameRuleValue("doMobSpawning", "false");
			w.setGameRuleValue("randomTickSpeed", "0");
		}
		compass = new ItemStack(Material.COMPASS);
		ItemMeta im = compass.getItemMeta();
		im.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Game Menu");
		im.setLore(Arrays.asList(Values.stringToLore("Choose a game to head to!", ChatColor.YELLOW)));
		compass.setItemMeta(im);
		
		torch = new ItemStack(Material.REDSTONE_TORCH_ON);
		im = torch.getItemMeta();
		im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Hub Options");
		im.setLore(Arrays.asList(Values.stringToLore("Toggle some hub options!", ChatColor.YELLOW)));
		torch.setItemMeta(im);
		
		book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bm = (BookMeta)book.getItemMeta();
		bm.setAuthor("King Ronbo");
		bm.setTitle("The Rules of Kastia");
		ArrayList<String> pages = new ArrayList<String>();
		pages.add("Rule 1. \nDon't be a meanie.\n \nRule 2. \nBe a coolio.");
		pages.add("Written by Ronbo.\nSo wonderful!");
		bm.setPages(pages);
		book.setItemMeta(bm);
		im = book.getItemMeta();
		im.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Rules");
		im.setLore(Arrays.asList(Values.stringToLore("The Rules of Kastia!", ChatColor.YELLOW)));
		book.setItemMeta(im);
		
		emerald = new ItemStack(Material.EMERALD);
		im = emerald.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Shop");
		im.setLore(Arrays.asList(Values.stringToLore("Time to spend big bucks!", ChatColor.YELLOW)));
		emerald.setItemMeta(im);
		
		star = new ItemStack(Material.NETHER_STAR);
		im = star.getItemMeta();
		im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Hub Select");
		im.setLore(Arrays.asList(Values.stringToLore("Switch to a different hub number!", ChatColor.YELLOW)));
		star.setItemMeta(im);
		
		clock = new ItemStack(Material.WATCH);
		im = clock.getItemMeta();
		im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Return to Main Hub");
		im.setLore(Arrays.asList(Values.stringToLore("Go back to the main hub!", ChatColor.YELLOW)));
		clock.setItemMeta(im);
		
		jade = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.LIME.getWoolData());
		im = jade.getItemMeta();
		im.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Factions Jade " + ChatColor.GRAY + "(Quick Join)");
		im.setLore(Arrays.asList(Values.stringToLore("Play on Factions Jade!", ChatColor.YELLOW)));
		jade.setItemMeta(im);
		
		onyx = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.GRAY.getWoolData());
		im = onyx.getItemMeta();
		im.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Factions Onyx " + ChatColor.GRAY + "(Quick Join)");
		im.setLore(Arrays.asList(Values.stringToLore("Play on Factions Onyx!", ChatColor.YELLOW)));
		onyx.setItemMeta(im);
		
		ruby = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.RED.getWoolData());
		im = ruby.getItemMeta();
		im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Factions Ruby " + ChatColor.GRAY + "(Quick Join)");
		im.setLore(Arrays.asList(Values.stringToLore("Play on Factions Ruby!", ChatColor.YELLOW)));
		ruby.setItemMeta(im);
		
		factionsmenu = new ItemStack(Material.CAKE);
		im = factionsmenu.getItemMeta();
		im.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Factions Menu");
		im.setLore(Arrays.asList(Values.stringToLore("See all the Factions servers!", ChatColor.YELLOW)));
		factionsmenu.setItemMeta(im);
		
		loadGlobals();
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				for(Player p : plugin.getServer().getOnlinePlayers())
					loadPlayer(p);
				for(Entity e : plugin.getServer().getWorld("world").getEntities()) {
					if(!(e instanceof Player)) {
						e.remove();
					}
				}
				for(Entity e : plugin.getServer().getWorld("fhub").getEntities()) {
					if(!(e instanceof Player)) {
						e.remove();
					}
				}
				LivingEntity le = createLivingEntity(CustomVillager.class, new Location(plugin.getServer().getWorld("world"), -609.5, 7, 566.5));
				le.setCustomName(ChatColor.GOLD + "Master Archer");
				le.setCustomNameVisible(true);
				le.setRemoveWhenFarAway(false);
				le = createLivingEntity(CustomVillager.class, new Location(plugin.getServer().getWorld("world"), -591.5, 7, 566.5));
				le.setCustomName(ChatColor.GOLD + "Siege Veteran");
				le.setCustomNameVisible(true);
				le.setRemoveWhenFarAway(false);
				le = createLivingEntity(CustomVillager.class, new Location(plugin.getServer().getWorld("world"), -637.5, 6, 626.5));
				le.setCustomName(ChatColor.GOLD + "King Ronbo");
				le.setCustomNameVisible(true);
				le.setRemoveWhenFarAway(false);
				le = createLivingEntity(CustomVillager.class, new Location(plugin.getServer().getWorld("world"), -467, 131, 559));
				le.setCustomName(ChatColor.WHITE + "GamingGetsEnder");
				le.setCustomNameVisible(true);
				le.setRemoveWhenFarAway(false);
				le = createLivingEntity(CustomVillager.class, new Location(plugin.getServer().getWorld("fhub"), -1767.5, 105, 93.5));
				le.setCustomName(ChatColor.RED + "Factions Fred");
				le.setCustomNameVisible(true);
				le.setRemoveWhenFarAway(false);
				le = createLivingEntity(CustomVillager.class, new Location(plugin.getServer().getWorld("world"), -606.5, 7, 563.5));
				((Villager)le).setProfession(Profession.BLACKSMITH);
				le.setCustomName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Ronbattle");
				le.setCustomNameVisible(true);
				le.setRemoveWhenFarAway(false);
				le = createLivingEntity(CustomVillager.class, new Location(plugin.getServer().getWorld("world"), -602.5, 7, 562.5));
				((Villager)le).setProfession(Profession.LIBRARIAN);
				le.setCustomName(ChatColor.GREEN + "" + ChatColor.BOLD + "Lucky Block Wars");
				le.setCustomNameVisible(true);
				le.setRemoveWhenFarAway(false);
				le = createLivingEntity(CustomVillager.class, new Location(plugin.getServer().getWorld("world"), -598.5, 7, 562.5));
				((Villager)le).setProfession(Profession.PRIEST);
				le.setCustomName(ChatColor.AQUA + "" + ChatColor.BOLD + "Factions");
				le.setCustomNameVisible(true);
				le.setRemoveWhenFarAway(false);
				le = createLivingEntity(CustomVillager.class, new Location(plugin.getServer().getWorld("world"), -594.5, 7, 563.5));
				((Villager)le).setProfession(Profession.BUTCHER);
				le.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + "Survival Games");
				le.setCustomNameVisible(true);
				le.setRemoveWhenFarAway(false);
			}
		}, 20);
	}
	
	public void loadGlobals() {
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				plugin.getServer().getWorld("world").setTime(3000);
				for(Entity e : plugin.getServer().getWorld("world").getEntities())
					if(e instanceof EnderDragon)
						if(e.getPassenger() == null || e.getPassenger().isDead() || !e.getPassenger().isValid())
							e.remove();
			}
		}, 20, 20);
	}
	
	public String randomColor() {
		return colors[(int)(Math.random() * colors.length)] + "";
	}
	
	public static ConcurrentHashMap<String, Stats> stats = new ConcurrentHashMap<String, Stats>();

	public static Stats getStats(Player p) {
		return stats.get(p.getName());
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent event) {
		if(stats.containsKey(event.getPlayer().getName())) {
			stats.get(event.getPlayer().getName()).save();
			stats.get(event.getPlayer().getName()).disabled = true;
			stats.remove(event.getPlayer());
		}
	}
	
	public HashMap<String, Integer> taskids = new HashMap<String, Integer>();
	public void loadPlayer(final Player p) {
		final String uuid = p.getUniqueId().toString();
		final String name = p.getName();
		p.setMetadata("login", new FixedMetadataValue(plugin, System.currentTimeMillis()));
		p.teleport(getRandomSpawnLoc());
		if(taskids.containsKey(name))
			plugin.getServer().getScheduler().cancelTask(taskids.get(name));
		taskids.put(name, plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public Scoreboard board;
			public Team noobs;
			public Objective objective;
			public int index = 0, index2 = 0;//, index3 = 0;
			public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
			public void run() {
				Player p = plugin.getServer().getPlayerExact(name);
				if(p == null || Minigame.leaving.contains(name))
					return;
				Stats stats = getStats(p);
				int max = -1;
				String top = "No one";
				for(Entry<String, Integer> e : ArcheryManager.glassHit.entrySet()) {
					if(e.getValue() > max) {
						top = e.getKey();
						max = e.getValue();
					}
				}
				if(stats != null) {
					if(board == null)
						board = plugin.getServer().getScoreboardManager().getNewScoreboard();
					p.setScoreboard(board);
					if(objective != null)
						objective.unregister();
					String s = "";
					objective = board.registerNewObjective("display" + (int)(Math.random() * 1000000000), "dummy");
					String scroll = "Welcome to Kastia, " + p.getName() + "!   ";
					String scroll_prefix = ChatColor.YELLOW + "" + ChatColor.BOLD;
					if(index >= scroll.length())
						index = 0;
					s = scroll_prefix + scroll.substring(index, scroll.length()) + scroll.substring(0, index++);
					objective.setDisplayName(s.length() >= 16 ? s.substring(0, 16) : s);
					objective.setDisplaySlot(DisplaySlot.SIDEBAR);
					scroll = "Buy a rank at store.kastia.net for awesome perks!   ";
					scroll_prefix = ChatColor.GOLD + "";
					if(index2 >= scroll.length())
						index2 = 0;
					String rank_display = scroll_prefix + scroll.substring(index2, scroll.length()) + scroll.substring(0, index2++);
					String[] display = {
							"--------------",
							ChatColor.GREEN + "" + ChatColor.BOLD + "Gems",
							ChatColor.GREEN + "" + stats.points,
							ChatColor.AQUA + "",
							ChatColor.GOLD + "" + ChatColor.BOLD + "Rank",
							RankManager.check(p, "knight")? RankManager.checkEqualsExact(p, "lord") ? ChatColor.YELLOW + "" + ChatColor.BOLD + "LORD" : RankManager.getPrefix(name) : rank_display,
							ChatColor.BLACK + "",
							ChatColor.AQUA + "" + ChatColor.BOLD + "Top Archer",
							ChatColor.AQUA + top,
							ChatColor.AQUA + "" + max + " Hits",
							ChatColor.BOLD + "www.kastia.net",
							ChatColor.RESET + "--------------",
					};
					int count = display.length;
					for(String s2 : display)
						objective.getScore(s2.length() >= 16 ? s2.substring(0, 16) : s2).setScore(count--);
					if((noobs = board.getTeam("znoobs")) == null) {
						noobs = board.registerNewTeam("znoobs");
					}
					noobs.setPrefix(ChatColor.GRAY + "");
					for(Player p2 : plugin.getServer().getOnlinePlayers()){
						Team t = board.getPlayerTeam(p);
						if(t != null)
							t.removePlayer(p2);
						if(RankManager.check(p2, "knight")) {
							Team temp = null;
							if((temp = board.getTeam(ALPHABET.charAt(ALPHABET.length() - RankManager.getRankPower(p2.getName())) + RankManager.getRank(p2.getName()))) == null) {
								temp = board.registerNewTeam(ALPHABET.charAt(ALPHABET.length() - RankManager.getRankPower(p2.getName())) + RankManager.getRank(p2.getName()));
							}
							String rank_pre = RankManager.getPrefix(p2.getName());
							String rank_col = ChatColor.getLastColors(rank_pre);
							if(RankManager.checkEqualsExact(p2, "lord")) {
								rank_pre = ChatColor.YELLOW + "" + ChatColor.BOLD + "LORD";
								rank_col = ChatColor.GOLD + "";
							}
							String prefix = rank_pre + rank_col + " ";
							if(prefix.length() > 15)
								prefix = prefix.substring(0, 16);
							temp.setPrefix(prefix);
							temp.addPlayer(p2);
						} else {
							noobs.addPlayer(p2);
						}
					}
				}
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 10);
			}
		}, 40));
		setMainInventory(p);
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage(ChatColor.GOLD + "Welcome to Kastia!");
		p.sendMessage(ChatColor.GREEN + "Have fun, and be sure to check out " + ChatColor.YELLOW + "www.kastia.net" + ChatColor.GREEN + "!");
		ArcheryManager.glassHit.put(p.getName(), 0);
		stats.put(p.getName(), new Stats(p.getName(), uuid));
		for(PotionEffect pe : p.getActivePotionEffects())
			p.removePotionEffect(pe.getType());
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		loadPlayer(event.getPlayer());
		event.setJoinMessage("");
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		ArcheryManager.glassHit.remove(p.getName());
		event.setQuitMessage("");
		p.setScoreboard(plugin.getServer().getScoreboardManager().getMainScoreboard());
	}
	
	public static LivingEntity createLivingEntity(Class<?> type, Location loc) {
		net.minecraft.server.v1_7_R4.World world = ((CraftWorld)(loc.getWorld())).getHandle();
		net.minecraft.server.v1_7_R4.EntityLiving e = null;
		try {
			e = (EntityLiving) (type.getDeclaredConstructor(net.minecraft.server.v1_7_R4.World.class).newInstance(world));
		} catch(Exception e2) {
			e2.printStackTrace();
		}
		e.setPosition(loc.getX(), loc.getY(), loc.getZ());
		world.addEntity(e, SpawnReason.CUSTOM);
		LivingEntity le = (LivingEntity)(((net.minecraft.server.v1_7_R4.Entity)e).getBukkitEntity());
		return le;
	}
	
	public static LivingEntity createPet(Class<?> type, Location loc) {
		net.minecraft.server.v1_7_R4.World world = ((CraftWorld)(loc.getWorld())).getHandle();
		net.minecraft.server.v1_7_R4.EntityLiving e = null;
		try {
			e = (EntityLiving) (type.getDeclaredConstructor(net.minecraft.server.v1_7_R4.World.class, boolean.class).newInstance(world, true));
		} catch(Exception e2) {
			e2.printStackTrace();
		}
		e.setPosition(loc.getX(), loc.getY(), loc.getZ());
		world.addEntity(e, SpawnReason.CUSTOM);
		LivingEntity le = (LivingEntity)(((net.minecraft.server.v1_7_R4.Entity)e).getBukkitEntity());
		return le;
	}
	@EventHandler
	public void ondragon(EntityExplodeEvent event) {
		if(event.getEntity() instanceof EnderDragon)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void ondismount(VehicleExitEvent event) {
		if(event.getVehicle() instanceof EnderDragon)
			event.getVehicle().remove();
	}
	
	public static String[] commands = {
		"db", "updateinfo", "bow", "grapple", "connect", "cc", "hub", "changeworld", "givereward", 
		"voted", "dragon", "pet", "ride", "name", "effects", "targets", "disable", "enable", "reloadminigames",
		"jade", "onyx", "ruby"
	};
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			if(sender instanceof Player) {
				final Player p = (Player)sender;
				if(cmd.getName().equalsIgnoreCase("db")) {
					
				}
				if(cmd.getName().equalsIgnoreCase("jade")) {
					if(FactionsManager.upA && (FactionsManager.playercountA < 100 || (RankManager.check(p, "knight") && FactionsManager.playercountA < 250))) {
						p.sendMessage(ChatColor.GREEN + "Joining Factions Jade..." + ChatColor.GRAY + " (" + FactionsManager.playercountA + "/100 players)");
						FactionsManager.sendChannelChange(p, "factions1");
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							public void run() {
								if(p.isOnline()) {
									p.sendMessage(ChatColor.RED + "Sorry, looks like something is wrong with Factions Jade.");
									p.sendMessage(ChatColor.RED + "Please try again later!");
								}
							}
						}, 20*3);
					} else if(FactionsManager.upA) {
						p.sendMessage(ChatColor.RED + "Factions Jade is full right now, sorry! " + ChatColor.GRAY + "(" + FactionsManager.playercountA + "/100 players)");
					} else {
						p.sendMessage(ChatColor.RED + "Factions Jade is currently down for maintenance. Sorry!");
					}
				}
				if(cmd.getName().equalsIgnoreCase("onyx")) {
					if(FactionsManager.upB && (FactionsManager.playercountB < 100 || (RankManager.check(p, "knight") && FactionsManager.playercountB < 250))) {
						p.sendMessage(ChatColor.GREEN + "Joining Factions Onyx..." + ChatColor.GRAY + " (" + FactionsManager.playercountB + "/100 players)");
						FactionsManager.sendChannelChange(p, "factions2");
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							public void run() {
								if(p.isOnline()) {
									p.sendMessage(ChatColor.RED + "Sorry, looks like something is wrong with Factions Onyx.");
									p.sendMessage(ChatColor.RED + "Please try again later!");
								}
							}
						}, 20*3);
					} else if(FactionsManager.upB) {
						p.sendMessage(ChatColor.RED + "Factions Onyx is full right now, sorry! " + ChatColor.GRAY + "(" + FactionsManager.playercountB + "/100 players)");
					} else {
						p.sendMessage(ChatColor.RED + "Factions Onyx is currently down for maintenance. Sorry!");
					}
				}
				if(cmd.getName().equalsIgnoreCase("ruby")) {
					if(FactionsManager.upC && (FactionsManager.playercountC < 100 || (RankManager.check(p, "knight") && FactionsManager.playercountC < 250))) {
						p.sendMessage(ChatColor.GREEN + "Joining Factions Ruby..." + ChatColor.GRAY + " (" + FactionsManager.playercountC + "/100 players)");
						FactionsManager.sendChannelChange(p, "factions3");
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							public void run() {
								if(p.isOnline()) {
									p.sendMessage(ChatColor.RED + "Sorry, looks like something is wrong with Factions Ruby.");
									p.sendMessage(ChatColor.RED + "Please try again later!");
								}
							}
						}, 20*3);
					} else if(FactionsManager.upC) {
						p.sendMessage(ChatColor.RED + "Factions Ruby is full right now, sorry! " + ChatColor.GRAY + "(" + FactionsManager.playercountC + "/100 players)");
					} else {
						p.sendMessage(ChatColor.RED + "Factions Ruby is currently down for maintenance. Sorry!");
					}
				}
				if(cmd.getName().equalsIgnoreCase("effects")) {
					EffectManager.showMenu(p);
				}
				if(cmd.getName().equalsIgnoreCase("pet")) {
					PetManager.showMenu(p);
				}
				if(cmd.getName().equalsIgnoreCase("reloadminigames")) {
					if(RankManager.check(p, "owner"))
						MinigameManager.load();
				}
				if(cmd.getName().equalsIgnoreCase("ride")) {
					PetManager.spawnPet(p);
					if(PetManager.spawnedPets.containsKey(p.getName())) {
						PetManager.spawnedPets.get(p.getName()).setPassenger(p);
						p.sendMessage(ChatColor.GOLD + "You are riding your pet!");
						p.sendMessage(ChatColor.GOLD + "Use " + ChatColor.YELLOW + "WASD" + ChatColor.GOLD + " and " + ChatColor.YELLOW + "spacebar" + ChatColor.GOLD + " to control it!");
					} else {
						p.sendMessage(ChatColor.RED + "You don't have an active pet!");
					}
				}
				if(cmd.getName().equalsIgnoreCase("name")) {
					Stats stats = getStats(p);
					if(stats.points < 15) {
						p.sendMessage(ChatColor.RED + "You need 15 Gems to change your pet's name!");
					} else {
						if(args.length == 0) {
							p.sendMessage(ChatColor.RED + "Your pet's name can't be blank.");
							p.sendMessage(ChatColor.RED + "Use /name <NAME> to give it a name!");
						} else {
							StringBuilder sb = new StringBuilder("");
							for(String s : args)
								sb.append(s + " ");
							String name = sb.toString().trim();
							if(ChatColor.stripColor(name).length() > 50) {
								p.sendMessage(ChatColor.RED + "That name is too long! Names can be up to 50 characters.");
								p.sendMessage(ChatColor.RED + "\"" + ChatColor.stripColor(name) + "\" is " + ChatColor.stripColor(name).length() + " characters.");
							} else {
								p.sendMessage(ChatColor.GOLD + "Knights, Nobles, and Royals can have colors in their pet names!");
								p.sendMessage(ChatColor.AQUA + "Just use &6, for example, to switch to the Gold color!");
								p.sendMessage(ChatColor.YELLOW + "Useful reference: http://ess.khhq.net/mc/");
								if(RankManager.check(p, "knight")) {
									stats.petname = ChatColor.translateAlternateColorCodes('&', name);
								} else {
									stats.petname = ChatColor.stripColor(name);
								}
								stats.points -= 15;
								PetManager.spawnPet(p);
								p.sendMessage(ChatColor.GREEN + "Your pet's name is now " + stats.petname + "!");
								SQLManager.executePrepared("update playerdata set hubpetname = ?, points = points - 15 where uuid = '" + stats.uuid + "'", false, stats.petname);
							}
						}
					}
				}
				if(cmd.getName().equalsIgnoreCase("dragon")) {
					if(RankManager.check(p, "royal")) {
						p.sendMessage(ChatColor.AQUA + "Hey there! Thank you so much for buying Royal!!!");
						p.sendMessage(ChatColor.GOLD + "You are now riding your dragon!");
						p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "It will move towards wherever you point!");
						p.sendMessage(ChatColor.GOLD + "Have fun!");
						final LivingEntity le = createPet(CustomEnderDragon.class, p.getLocation());
						((CustomEnderDragon)((CraftEntity)le).getHandle()).player = p.getName();
						le.setPassenger(p);
						final String name = p.getName();
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							public void run() {
								if(plugin.getServer().getPlayerExact(name) == null || le.getPassenger() == null || !(le.getPassenger() instanceof Player))
									le.remove();
								else
									plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 5);
							}
						});
					} else {
						p.sendMessage(ChatColor.RED + "/dragon is for Royals only!");
						p.sendMessage(ChatColor.RED + "Get the Royal rank at store.kastia.net!");
					}
				}
				if(cmd.getName().equalsIgnoreCase("targets")) {
					if(RankManager.check(p, "admin")) {
						ArcheryManager.glassHit.put(args[0], Integer.parseInt(args[1]));
					}
				}
				if(cmd.getName().equalsIgnoreCase("bow")) {
					ArcheryManager.supply(p);
				}
				if(cmd.getName().equalsIgnoreCase("grapple")) {
					GrappleManager.supply(p);
				}
				if(cmd.getName().equalsIgnoreCase("givereward")) {
					if(RankManager.check(p, "owner")) {
						RewardsManager.giveReward(args[0], Integer.parseInt(args[1]));
					}
				}
				if(cmd.getName().equalsIgnoreCase("enable")) {
					if(RankManager.check(p, "admin")) {
						MinigameManager.disabled.remove(args[0]);
						p.sendMessage("Enabled server " + args[0] + ".");
					}
				}
				if(cmd.getName().equalsIgnoreCase("disable")) {
					if(RankManager.check(p, "admin")) {
						MinigameManager.disabled.add(args[0]);
						p.sendMessage("Disabled server " + args[0] + ".");
					}
				}
				if(cmd.getName().equalsIgnoreCase("updateinfo")) {
					if(RankManager.check(p, "admin")) {
						ChannelManager.loadChannels();
						p.sendMessage("Updated info.");
					}
				}
				if(cmd.getName().equalsIgnoreCase("changeworld")) {
					if(RankManager.check(p, "admin")) {
						if(args.length > 0) {
							World w = null;
							StringBuilder worldName = new StringBuilder("");
							for(String s : args)
								worldName.append(s + " ");
							worldName = new StringBuilder(worldName.substring(0, worldName.length() - 1));
							for(World w2 : plugin.getServer().getWorlds()) {
								if(w2.getName().equalsIgnoreCase(worldName.toString()))
									w = w2;
							}
							if(w == null) {
								p.sendMessage(ChatColor.RED + "Invalid world name! (" + worldName.toString() + ")");
								StringBuilder sb = new StringBuilder("Valid world names: ");
								for(World w2 : plugin.getServer().getWorlds())
									if(!w2.getName().contains("+di+"))
										sb.append(w2.getName() + ", ");
								p.sendMessage(sb.substring(0, sb.length() - 2));
								return true;
							}
							p.teleport(w.getSpawnLocation());
						} else {
							StringBuilder sb = new StringBuilder("Valid world names: ");
							for(World w2 : plugin.getServer().getWorlds())
									sb.append(w2.getName() + ", ");
							p.sendMessage(sb.substring(0, sb.length() - 2));
						}
					}
				}
				if(cmd.getName().equalsIgnoreCase("hub")) {
					p.teleport(getRandomSpawnLoc());
					loadPlayer(p);
				}
				if(cmd.getName().equalsIgnoreCase("cc")) {
					ChannelManager.showChannelMenu(p);
				}
			} else {
				if(cmd.getName().equalsIgnoreCase("updateinfo")) {
					ChannelManager.loadChannels();
					System.out.println("Updated info.");
				}
				if(cmd.getName().equalsIgnoreCase("givereward")) {
					RewardsManager.giveReward(args[0], Integer.parseInt(args[1]));
				}
				if(cmd.getName().equalsIgnoreCase("voted")) {
					RewardsManager.voted(args[0]);
				}
				if(cmd.getName().equalsIgnoreCase("db")) {
					System.out.println(Bukkit.getOfflinePlayer(args[0]).getUniqueId());
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public void onDisable() {
		CustomEntityType.unregisterEntities();
		plugin.getServer().getScheduler().cancelAllTasks();
		for(Entry<int[], Material> e : ArcheryManager.brokenGlass.entrySet()) {
			new Location(plugin.getServer().getWorld("world"), e.getKey()[0], e.getKey()[1], e.getKey()[2]).getBlock().setType(e.getValue());
		}
		getLogger().info("Disabling Ronbo Lobby...");
	}
	
	public static RonboLobby plugin;
	
}
