package xronbo.ronbolobby;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;

import me.ronbo.core.SQLManager;
import me.ronbo.core.ranks.RankManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;


public class Stats {
	public static HashSet<String> joinFullFactions = new HashSet<String>();
	public String joinDate = null;
	public final String uuid;
	public final String name;
	public int points;
	public boolean disabled = false;
	public volatile boolean loaded = false;
	public ArrayList<String> hubunlocks = new ArrayList<String>();
	public String currentpet;
	public String petname;
	public String activeeffect;
	public void save() {
		save(false);
	}
	public void save(boolean forced) {
		if(!loaded)
			return;
		String date = dateformatEST.format(new Date());
		if(joinDate == null) {
			joinDate = date;
		}
		SQLManager.execute("insert into playerdata(name, uuid, points, joindate, lastlogin, hubunlocks) values ('" + name + "', '" + uuid + 
				"', " + points + ", '" + joinDate + "', '" + date + "', ' ')"
				+ " on duplicate key update name = '" + name + "', lastlogin = '" + date + "'", forced);
	}
	private void load() {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if(plugin.getServer().getPlayerExact(name) != null && plugin.getServer().getPlayerExact(name).isOnline())
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 20 * 10);
			}
		}, 20 * 10);
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			public void run() {
				try {
					ResultSet rs = SQLManager.executeQuery("select points, hubunlocks, hubactivepet, "
							+ "hubpetname, hubactiveeffect, huboptions, rankString, knightExpiryDate, nobleExpiryDate, "
							+ "newKnightDays, newNobleDays, factionsrank from playerdata where uuid = '" + uuid + "'");
					if(rs.next()) {
						try {
							points = rs.getInt("points");
						} catch(Exception e) {
							e.printStackTrace();
						}
						int knightdays = rs.getInt("newKnightDays");
						int nobledays = rs.getInt("newNobleDays");
						String rankString = rs.getString("rankString");
						if(rankString == null)
							rankString = "member";
						String factionsRank = rs.getString("factionsrank");
						if(factionsRank != null && !factionsRank.equalsIgnoreCase("member"))
							joinFullFactions.add(name);
						try {
							String knightExpiryDate = rs.getString("knightExpiryDate");
							Calendar knightExpiry = Calendar.getInstance(TimeZone.getTimeZone("EST"));
							if(knightExpiryDate != null) {
								knightExpiry.setTime(dateformatEST.parse(knightExpiryDate));
							}
							if(knightdays > 0) {
								knightExpiry.add(Calendar.DATE, knightdays);
								if(rankString == null || rankString.length() == 0 || rankString.equalsIgnoreCase("member")) {
									rankString = "knight";
									SQLManager.execute("update playerdata set rankString = 'knight', knightExpiryDate = '" + dateformatEST.format(knightExpiry.getTime()) + "',"
											+ " newKnightDays = 0 where uuid = '" + uuid + "'");
									plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
										public void run() {
											if(plugin.getServer().getPlayerExact(name) != null && plugin.getServer().getPlayerExact(name).isOnline())
												RankManager.loadPlayer(plugin.getServer().getPlayerExact(name));
										}
									}, 20);
								} else {
									SQLManager.execute("update playerdata set knightExpiryDate = '" + dateformatEST.format(knightExpiry.getTime()) + "',"
											+ " newKnightDays = 0 where uuid = '" + uuid + "'");
								}
							}
							if(rankString.equalsIgnoreCase("knight") && knightExpiry.compareTo(Calendar.getInstance(TimeZone.getTimeZone("EST"))) < 0) {
								sendMessage(ChatColor.RED + "Your Knight just expired! You are now a normal member.");
								SQLManager.execute("update playerdata set rankString = 'member', knightExpiryDate = '' where uuid = '" + uuid + "'");
								rankString = "member";
								plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
									public void run() {
										if(plugin.getServer().getPlayerExact(name) != null && plugin.getServer().getPlayerExact(name).isOnline())
											RankManager.loadPlayer(plugin.getServer().getPlayerExact(name));
									}
								}, 20);
							} else if(knightExpiry.compareTo(Calendar.getInstance(TimeZone.getTimeZone("EST"))) > 0) {
								if(rankString.equalsIgnoreCase("member")) {
									SQLManager.execute("update playerdata set rankString = 'knight' where uuid = '" + uuid + "'");
									rankString = "knight";
								}
								if(rankString.equalsIgnoreCase("knight")) {
									sendMessage(ChatColor.YELLOW + "You are a Knight! Thanks for your support!");
									sendMessage(ChatColor.YELLOW + "Your Knight will expire on " + ChatColor.WHITE + dateformatEST.format(knightExpiry.getTime()) + ".");
								}
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
						
						try {
							String nobleExpiryDate = rs.getString("nobleExpiryDate");
							Calendar nobleExpiry = Calendar.getInstance(TimeZone.getTimeZone("EST"));
							if(nobleExpiryDate != null) {
								nobleExpiry.setTime(dateformatEST.parse(nobleExpiryDate));
							}
							if(nobledays > 0) {
								nobleExpiry.add(Calendar.DATE, nobledays);
								if(rankString == null || rankString.length() == 0 || rankString.equalsIgnoreCase("member") || rankString.equalsIgnoreCase("knight")) {
									rankString = "noble";
									SQLManager.execute("update playerdata set rankString = 'noble', nobleExpiryDate = '" + dateformatEST.format(nobleExpiry.getTime()) + "',"
											+ " newNobleDays = 0 where uuid = '" + uuid + "'");
									plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
										public void run() {
											if(plugin.getServer().getPlayerExact(name) != null && plugin.getServer().getPlayerExact(name).isOnline())
												RankManager.loadPlayer(plugin.getServer().getPlayerExact(name));
										}
									}, 20);
								} else {
									SQLManager.execute("update playerdata set nobleExpiryDate = '" + dateformatEST.format(nobleExpiry.getTime()) + "',"
											+ " newNobleDays = 0 where uuid = '" + uuid + "'");
								}
							}
							if(rankString.equalsIgnoreCase("noble") && nobleExpiry.compareTo(Calendar.getInstance(TimeZone.getTimeZone("EST"))) < 0) {
								sendMessage(ChatColor.RED + "Your Noble just expired! You are now a normal member.");
								SQLManager.execute("update playerdata set rankString = 'member', nobleExpiryDate = '' where uuid = '" + uuid + "'");
								rankString = "member";
								plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
									public void run() {
										if(plugin.getServer().getPlayerExact(name) != null && plugin.getServer().getPlayerExact(name).isOnline())
											RankManager.loadPlayer(plugin.getServer().getPlayerExact(name));
									}
								}, 20);
							} else if(nobleExpiry.compareTo(Calendar.getInstance(TimeZone.getTimeZone("EST"))) > 0) {
								if(rankString.equalsIgnoreCase("member")) {
									SQLManager.execute("update playerdata set rankString = 'noble' where uuid = '" + uuid + "'");
									rankString = "noble";
								}
								if(rankString.equalsIgnoreCase("noble")) {
									sendMessage(ChatColor.YELLOW + "You are a Noble! Thanks for your support!");
									sendMessage(ChatColor.YELLOW + "Your Noble will expire on " + ChatColor.WHITE + dateformatEST.format(nobleExpiry.getTime()) + ".");
								}
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
						
						currentpet = rs.getString("hubactivepet");
						if(currentpet == null)
							currentpet = "";
						petname = rs.getString("hubpetname");
						if(petname == null || petname.length() == 0)
							petname = name + "'s Pet";
						activeeffect = rs.getString("hubactiveeffect");
						if(activeeffect == null)
							activeeffect = "";
						String unlocks = rs.getString("hubunlocks");
						System.out.println("unlocks for " + name + ": " + unlocks);
						if(unlocks != null) {
							for(String s : Arrays.asList(unlocks.trim().split(" ")))
								hubunlocks.add(s);
						}
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							public void run() {
								if(plugin.getServer().getPlayerExact(name) != null) {
									PetManager.spawnPet(plugin.getServer().getPlayerExact(name));
									EffectManager.activate(plugin.getServer().getPlayerExact(name));
								}
							}
						});
						loaded = true;
					} else {
						RonboLobby.plugin.getServer().getScheduler().scheduleSyncDelayedTask(RonboLobby.plugin, new Runnable() {
							public void run() {
								plugin.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "Please welcome " + name + " to Kastia!");
							}
						});
						loaded = true;
						save();
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void sendMessage(String s) {
		if(RonboLobby.plugin.getServer().getPlayerExact(name) != null)
			RonboLobby.plugin.getServer().getPlayerExact(name).sendMessage(s);
	}
	
	public Stats(String player, String uuid) {
		this.name = player;
		this.uuid = uuid;
		this.petname = name + "'s Pet";
		load();
		Player p = plugin.getServer().getPlayerExact(player);
		if(p != null && p.isOnline()) {
			PermissionAttachment pa = p.addAttachment(plugin);
			pa.unsetPermission("bukkit.command.me");
			pa.unsetPermission("minecraft.command.me");
			pa.setPermission("bukkit.command.me", false);
			pa.setPermission("minecraft.command.me", false);
		}
		RonboLobby.plugin.getServer().getScheduler().scheduleSyncDelayedTask(RonboLobby.plugin, new Runnable() {
			public void run() {
				if(disabled)
					return;
				save();
				RonboLobby.plugin.getServer().getScheduler().scheduleSyncDelayedTask(RonboLobby.plugin, this, 20 * 120);
			}
		}, 20 * 120);
	}
	
	public static final SimpleDateFormat dateformatEST;
	static {
		dateformatEST = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss zzz");
		dateformatEST.setTimeZone(TimeZone.getTimeZone("EST"));
	}
	public static RonboLobby plugin;
}