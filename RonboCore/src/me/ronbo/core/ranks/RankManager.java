package me.ronbo.core.ranks;

import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import me.ronbo.core.RonboCore;
import me.ronbo.core.SQLManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RankManager implements Listener {

	public static void load() {
		coloredRanks.clear();
		rankPowers.clear();
		Scanner scan = null;
		try {
			scan = new Scanner(new File(RonboCore.plugin.getDataFolder() + File.separator + "ranks.cfg"));
			while(scan.hasNextLine()) {
				String[] data = scan.nextLine().split(" - ");
				String rankName = ChatColor.translateAlternateColorCodes('&', data[0]);
				int power = Integer.parseInt(data[1]);
				String nameReference = ChatColor.stripColor(rankName).toLowerCase();
				rankPowers.put(nameReference, power);
				coloredRanks.put(nameReference, rankName);
			}
			rankPowers.put("member", 1);
			coloredRanks.put("member", "");
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(scan != null)
				scan.close();
		}
		for(Player p : RonboCore.plugin.getServer().getOnlinePlayers()) {
			RankManager.loadPlayer(p);
		}
	}
	
	public static String setRank(String player, String rank) {
		rank = rank.toLowerCase();
		if(!rankPowers.containsKey(rank))
			return ChatColor.RED + "Could not find a rank named '" + rank + "'.";
		ranks.put(player.toLowerCase(), rank);
		SQLManager.execute("update playerdata set rankString = '" + rank.toLowerCase() + "' where name = '" + player + "'");
		return ChatColor.GREEN + "Set " + player + "'s rank to " + coloredRanks.get(rank) + ChatColor.GREEN + "!"; 
	}
	
	public static HashMap<String, String> coloredRanks = new HashMap<String, String>();
	public static HashMap<String, Integer> rankPowers = new HashMap<String, Integer>();
	public static ConcurrentHashMap<String, String> ranks = new ConcurrentHashMap<String, String>();

	public static boolean check(Player p, String toCheck) {
		if(!rankPowers.containsKey(toCheck.toLowerCase())) {
			System.out.println("WARNING: Checked for non-existent rank! (" + toCheck + ")");
			return false;
		}
		return getRankPower(p.getName().toLowerCase()) >= rankPowers.get(toCheck.toLowerCase());
	}
	
	public static boolean checkEquals(Player p, String toCheck) {
		if(!rankPowers.containsKey(toCheck.toLowerCase())) {
			System.out.println("WARNING: Checked for non-existent rank! (" + toCheck + ")");
			return false;
		}
		return getRankPower(p.getName().toLowerCase()) == rankPowers.get(toCheck.toLowerCase());
	}
	
	public static boolean checkEqualsExact(Player p, String toCheck) {
		if(!rankPowers.containsKey(toCheck.toLowerCase())) {
			System.out.println("WARNING: Checked for exact non-existent rank! (" + toCheck + ")");
			return false;
		}
		return getRank(p).equals(toCheck.toLowerCase());
	}
	
	public static String getPrefix(String player) {
		if(getRankPower(player) <= 1)
			return "";
		return coloredRanks.get(getRank(player));
	}
	
	public static String getRank(Player p) {
		return getRank(p.getName());
	}
	
	public static String getRank(String player) {
		return ranks.get(player.toLowerCase());
	}
	
	public static int getRankPower(String player) {
		return rankPowers.get(getRank(player));
	}
	
	@EventHandler
	public void onLogin(PlayerJoinEvent event) {
		loadPlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onLogout(PlayerQuitEvent event) {
		ranks.remove(event.getPlayer().getName().toLowerCase());
	}
	
	private static final SimpleDateFormat dateformatEST;
	static {
		dateformatEST = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss zzz");
		dateformatEST.setTimeZone(TimeZone.getTimeZone("EST"));
	}
	
	public static HashMap<String, Integer> playTime = new HashMap<String, Integer>();
	public static boolean override = false;
	public static void loadPlayer(Player p) {
		final String name = p.getName().toLowerCase();
		final String uuid = p.getUniqueId().toString();
		ranks.put(name, "member");
		RonboCore.plugin.getServer().getScheduler().runTaskAsynchronously(RonboCore.plugin, new Runnable() {
			public void run() {
				if(!override) {
					String date = dateformatEST.format(new Date());
					try {
						System.out.println("Sending query for " + name);
						ResultSet rs = SQLManager.executeQuery("select rankString, playTime from playerdata where uuid = '" + uuid + "'");
						System.out.println("Received query results");
						SQLManager.execute("insert into playerdata(name,uuid,rankString,points,joindate,lastlogin) values ("
								+ "'" + name + "', '" + uuid + "', 'member', '0', '" + date + "', '" + date + "') on duplicate key update "
										+ "name = '" + name + "', lastlogin = '" + date + "' ");
						String rank = "member";
						playTime.put(name, Integer.MIN_VALUE);
						if(rs.next()) {
							playTime.put(name, rs.getInt("playTime"));
							rank = rs.getString("rankString");
							if(rank != null)
								rank = rank.toLowerCase();
						}
						if(rank == null)
							rank = "member";
						final String failed = rank;
						if(RonboCore.plugin.isEnabled() && !rankPowers.containsKey(rank) || !coloredRanks.containsKey(rank)) {
							RonboCore.plugin.getServer().getScheduler().runTaskAsynchronously(RonboCore.plugin, new Runnable() {
								public void run() {
									Player p = RonboCore.plugin.getServer().getPlayerExact(name);
									if(p != null && p.isOnline()) {
										p.sendMessage(ChatColor.RED + "There was an error loading your rank, " + ChatColor.YELLOW + failed + ChatColor.RED + "!");
										p.sendMessage(ChatColor.RED + "You are temporarily at the normal member rank.");
										p.sendMessage(ChatColor.RED + "Please report this problem to Ronbo. Thanks!");
									}
								}
							});
							rank = "member";
						}
						ranks.put(name, rank);
						System.out.println("Rank for player " + name + " is " + rank + " (power: " + getRankPower(name) + ")");
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public RankManager() {
		
	}
}