package me.ronbo.core;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import me.ronbo.core.jesper.Jesper;
import me.ronbo.core.ranks.ChatManager;
import me.ronbo.core.ranks.Moderation;
import me.ronbo.core.ranks.RankManager;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class RonboCore extends JavaPlugin implements CommandExecutor, Listener {
	

	public static String serverName = "null";
	public static String ip, SQLPort, SQLUser, SQLPass, jedisPort;
	@Override
	public void onEnable() {
		RonboCore.plugin = this;
		for(String s : commands) {
			try {
				getCommand(s).setExecutor(this);
			} catch(Exception e) {
				System.out.println("Error loading command " + s);
				e.printStackTrace();
			}
		}
		for(File f : new File("./plugins/").listFiles()) {
			if(f.getName().startsWith("server_")) {
				serverName = f.getName().substring("server_".length());
				System.out.println("This server is " + serverName);
				break;
			}
		}
		Scanner scan = null;
		try {
			scan = new Scanner(new File(RonboCore.plugin.getDataFolder() + File.separator + "connection.cfg"));
			ip = scan.nextLine().substring("IP: ".length()).trim();
			SQLPort = scan.nextLine().substring("SQL_Port: ".length()).trim();
			SQLUser = scan.nextLine().substring("SQL_User: ".length()).trim();
			SQLPass = scan.nextLine().substring("SQL_Pass: ".length()).trim();
			jedisPort = scan.nextLine().substring("Jedis_Port: ".length()).trim();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(scan != null)
				scan.close();
		}
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new RankManager(), this);
		getServer().getPluginManager().registerEvents(new ChatManager(), this);
		getServer().getPluginManager().registerEvents(new Moderation(), this);
		SQLManager.loadSQLConnection(this);
		RankManager.load();
		Jesper.load();
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public String[] msgs = {
					"The Gem Shop is filled with awesome stuff to buy with Gems you've earned!",
					"Earn Gems by playing (and winning!) our fun minigames!",
					"Please consider making a purchase at store.kastia.net! We really appreciate any and all support!",
					"Players with Knight rank or higher will not see these ads!",
					"Want to ride and control your very own Ender Dragon? Get Royal at store.kastia.net!",
					"Help Kastia grow by voting every day at www.kastia.net! You get some cool rewards too!",
					"Ronbo is the lead developer and founder of Kastia! Check him out at www.ronbo.me!",
			};
			public void run() {
				for(Player p : plugin.getServer().getOnlinePlayers())
					if(!RankManager.check(p, "knight"))
						p.sendMessage(ChatColor.GRAY + "[Ad] " + ChatColor.GOLD + msgs[(int)(Math.random() * msgs.length)]);
			}
		}, 20, 20 * 60 * (int)(Math.random() * 5 + 1));
		for(Player p : plugin.getServer().getOnlinePlayers())
			joinTime.put(p.getName(), System.currentTimeMillis());
	}
	
	public HashMap<String, Long> joinTime = new HashMap<String, Long>();
	public HashSet<String> nochat = new HashSet<String>();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		joinTime.put(event.getPlayer().getName(), System.currentTimeMillis());
		nochat.add(event.getPlayer().getName());
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		nochat.remove(event.getPlayer().getName());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if(joinTime.containsKey(event.getPlayer().getName())) {
			long value = (System.currentTimeMillis() - joinTime.get(event.getPlayer().getName())) / 1000 / 60;
			if(value > 1) {
				SQLManager.execute("update playerdata set playTime = playTime + " + value + " where name = '" + event.getPlayer().getName() + "'");
			}
			joinTime.remove(event.getPlayer().getName());
			RankManager.playTime.remove(event.getPlayer().getName().toLowerCase());
		}
	}
	
	
	public static void overrideChat() {
		ChatManager.override = true;
	}
	
	public static void overrideRanks() {
		RankManager.override = true;
	}
	
	@Override
	public void onDisable() {
		Jesper.stop();
//		SQLManager.stop();
		for(Player p : plugin.getServer().getOnlinePlayers()) {
			if(joinTime.containsKey(p.getName())) {
				long value = (System.currentTimeMillis() - joinTime.get(p.getName())) / 1000 / 60;
				if(value > 1) {
					SQLManager.execute("update playerdata set playTime = playTime + " + value + " where name = '" + p.getName() + "'", true);
				}
				joinTime.remove(p.getName());
				RankManager.playTime.remove(p.getName().toLowerCase());
			}
		}
	}
	
	public static RonboCore plugin;
	
	public HashMap<String, Long> lastReport = new HashMap<String, Long>();
	public HashMap<String, String> reportMessage = new HashMap<String, String>();
	public boolean globalmute = false;
	public HashSet<String> filter = new HashSet<String>();
	
	public String[] commands = {
		"flushjesper", "jesper", "setrank", "tpto", "tphere", "fly", "hide", "show",
		"saki", "sakichat", "teleport", "loc", "spawnpoint", "setspawn", "playtime", "report",
		"changeworld"
	};
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		try {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				boolean ronbo = p.getName().equals("xRonbo") || p.getName().equals("Edasaki");
				if(RankManager.check(p, "developer") || ronbo) {
					if(cmd.getName().equalsIgnoreCase("flushjesper")) {
						Jesper.flush();
						return true;
					}
				}
				if(RankManager.check(p, "owner") || ronbo) {
					if(cmd.getName().equalsIgnoreCase("setrank")) {
						p.sendMessage(RankManager.setRank(args[0], args[1]));
						return true;
					} else if(cmd.getName().equalsIgnoreCase("setspawn")) {
						p.getWorld().setSpawnLocation(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
						p.sendMessage("Set spawn point of " + p.getWorld().getName() + " to " + p.getWorld().getSpawnLocation());
						return true;
					} else if(cmd.getName().equalsIgnoreCase("togglemute")) {
						globalmute = !globalmute;
						getServer().broadcastMessage(ChatColor.RED + "Silent mode has been " + ChatColor.BOLD + (globalmute ? "enabled" : "disabled") + ChatColor.RED + "!");
						return true;
					} else if(cmd.getName().equalsIgnoreCase("ronbo")) {
						StringBuilder sb = new StringBuilder("");
						for(String s : args) {
							sb.append(s + " ");
						}
						String message = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
						for(Player p2 : getServer().getOnlinePlayers()) {
							p2.sendMessage(message);
						}
						return true;
					} else if(cmd.getName().equalsIgnoreCase("changeworld")) {
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
					} else if(cmd.getName().equalsIgnoreCase("reloadranks")) {
						RankManager.load();
						p.sendMessage("Reloaded ranks from file.");
					}
				}
				if(RankManager.check(p, "admin")) {
					if(cmd.getName().equalsIgnoreCase("teleport")) {
						try {
							int x = Integer.parseInt(args[0]);
							int y = Integer.parseInt(args[1]);
							int z = Integer.parseInt(args[2]);
							Moderation.teleport(p, x, y, z);
						} catch(Exception e) {
							p.sendMessage(ChatColor.RED + "Use as /teleport x y z");
						}
					} else if(cmd.getName().equalsIgnoreCase("filter")) {
						StringBuilder sb = new StringBuilder("");
						try {
							for(String s : args) {
								sb.append(s + " ");
							}
							String toFilter = sb.toString().toLowerCase().trim();
							filter.add(toFilter);
							sender.sendMessage("The phrase " + toFilter + " is now temporarily filtered in chat.");
							sender.sendMessage("Use /clearfilter to clear the chat filter.");
							Jesper.sendMessage(sender.getName() + " just filtered \"" + toFilter + "\".");
						} catch(Exception e) {
							
						}
						return true;
					} else if(cmd.getName().equalsIgnoreCase("clearfilter")) {
						filter.clear();
						sender.sendMessage("Chat filter cleared.");
						return true;
					}
				}
				if(RankManager.check(p, "mod") || ronbo) {
					if(cmd.getName().equalsIgnoreCase("tpto")) {
						Player target = plugin.getServer().getPlayerExact(args[0]);
						if(target == null || !target.isOnline()) {
							p.sendMessage(ChatColor.RED + "Could not find player " + ChatColor.YELLOW + args[0] + ChatColor.RED + ".");
						} else {
							Moderation.tpTo(p, target);
						}
						return true;
					} else if(cmd.getName().equalsIgnoreCase("tphere")) {
						Player target = plugin.getServer().getPlayerExact(args[0]);
						if(target == null || !target.isOnline()) {
							p.sendMessage(ChatColor.RED + "Could not find player " + ChatColor.YELLOW + args[0] + ChatColor.RED + ".");
						} else {
							Moderation.tpHere(p, target);
						}
						return true;
					} else if(cmd.getName().equalsIgnoreCase("fly")) {
						Moderation.fly(p);
						return true;
					} else if(cmd.getName().equalsIgnoreCase("flyspeed")) {
						Moderation.flySpeed(p, args[0]);
						return true;
					}
				}
				if(RankManager.check(p, "helper") || ronbo) {
					if(cmd.getName().equalsIgnoreCase("jesper")) {
						StringBuilder sb = new StringBuilder("");
						for(String s : args)
							sb.append(s + " ");
						Jesper.sendMessage(sender.getName() + ": " + sb.toString().trim());
						return true;
					} else if(cmd.getName().equalsIgnoreCase("loc")) {
						String s = (int)p.getLocation().getBlockX() + ", " + (int)p.getLocation().getBlockY() + ", " + (int)p.getLocation().getBlockZ() + ", " + p.getLocation().getYaw() + ", " + p.getLocation().getPitch();
						p.sendMessage(s);
						try {
							StringSelection stringSelection = new StringSelection(s);
							Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
							clipboard.setContents(stringSelection, null);
						} catch(Exception e) {
							
						}
						return true;
					} else if(cmd.getName().equalsIgnoreCase("spawnpoint")) {
						p.teleport(p.getWorld().getSpawnLocation());
						return true;
					} else if(cmd.getName().equalsIgnoreCase("hide")) {
						Moderation.hide(p);
						return true;
					} else if(cmd.getName().equalsIgnoreCase("show")) {
						Moderation.show(p);
						return true;
					} 
				}
				if(cmd.getName().equalsIgnoreCase("saki")) {
					p.sendMessage(ChatColor.RED + "Saki is a WIP.");
					return true;
				} else if(cmd.getName().equalsIgnoreCase("sakichat")) {
					StringBuilder sb = new StringBuilder("");
					for(String s : args)
						sb.append(s + " ");
					Jesper.sendMessage(p.getName() + ": " + sb.toString().trim());
					return true;
				} else if(cmd.getName().equalsIgnoreCase("report")) {
					boolean send = true;
					if(lastReport.containsKey(sender.getName())) {
						if(System.currentTimeMillis() - lastReport.get(sender.getName()) < 5 * 60 * 1000) {
							send = false;
						}
					}
					if(send) {
						if(args.length == 1 && args[0].equals("confirm")) {
							if(reportMessage.containsKey(p.getName())) {
								String message = reportMessage.remove(p.getName());
								Jesper.sendMessage(message);
								p.sendMessage(message);
								p.sendMessage(ChatColor.RED + "Sent in report! Thanks for your help!");
								lastReport.put(p.getName(), System.currentTimeMillis());
							}
						} else if(args.length >= 2) {
							StringBuilder sb = new StringBuilder("");
							for(int k = 1; k < args.length; k++)
								sb.append(args[k] + " ");
							String message = ChatColor.RED + "[REPORT BY " + sender.getName() + "]: " + args[0] + " FOR " + sb.toString().trim();
							reportMessage.put(p.getName(), message);
							p.sendMessage(ChatColor.RED + "Prepared new report:");
							p.sendMessage("  " + message);
							p.sendMessage(ChatColor.RED + "Use " + ChatColor.YELLOW + "/report confirm" + ChatColor.RED + " to send your report.");
							p.sendMessage(ChatColor.RED + "WARNING: Abuse of /report CAN be punished!");
						} else {
							p.sendMessage(ChatColor.RED + "Proper format: " + ChatColor.YELLOW + "/report <name> <reason>");
							p.sendMessage(ChatColor.RED + "Example: " + ChatColor.YELLOW + "/report xRonbo being too cool");
						}
					} else {
						p.sendMessage(ChatColor.RED + "You can only report a player once every 5 minutes.");
					}
					return true;
				} else if(cmd.getName().equalsIgnoreCase("playtime")) {
					if(!RankManager.playTime.containsKey(sender.getName().toLowerCase())) {
						p.sendMessage(ChatColor.RED + "Your data hasn't been loaded yet! Try again in a bit.");
						return true;
					}
					int time = RankManager.playTime.get(sender.getName().toLowerCase());
					if(joinTime.containsKey(sender.getName()))
						time += (System.currentTimeMillis() - joinTime.get(sender.getName())) / 1000 / 60;
					int days = time / 24 / 60;
					int hours = time / 60 % 24;
					int minutes = time % 60;
					StringBuilder sb = new StringBuilder();
					if(days > 0) {
						if(days == 1) {
							sb.append("1 day ");
						} else {
							sb.append(days + " days ");
						}
					}
					if(hours > 0) {
						if(hours == 1) {
							sb.append("1 hour ");
						} else {
							sb.append(hours + " hours ");
						}
					}
					if(minutes == 1) {
						sb.append("1 minutes ");
					} else {
						sb.append(minutes + " minutes ");
					}
					p.sendMessage(ChatColor.YELLOW + "You have played Kastia for " + sb.toString().trim() + ".");
					return true;
				}
			} else if(sender instanceof ConsoleCommandSender) {
				//Console
				if(cmd.getName().equalsIgnoreCase("jesper")) {
					StringBuilder sb = new StringBuilder("");
					for(String s : args)
						sb.append(s + " ");
					Jesper.sendMessage("CONSOLE: " + sb.toString().trim());
					return true;
				}
				if(cmd.getName().equalsIgnoreCase("setrank")) {
					System.out.println(RankManager.setRank(args[0], args[1]));
					return true;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		sender.sendMessage(ChatColor.RED + "You do not have permission to execute that command!");
		return true;
	}
	
}
