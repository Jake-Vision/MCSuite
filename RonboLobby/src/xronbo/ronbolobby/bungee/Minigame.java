package xronbo.ronbolobby.bungee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import me.ronbo.core.SQLManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import xronbo.ronbolobby.hpbar.HPBar;

public class Minigame {

	public static int ID = 1;
	
	public final String game;
	public final int id = ID++;
	public final String identifier;
	public final String title;
	public final int maxPlayers;
	public final int index;
	public boolean spectate;
	
	public volatile boolean joinable = true;
	public volatile boolean inProgress = false;
	public volatile int playerCount;
	public volatile int spectatorCount;
	public Set<String> additionalInfo = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	public volatile boolean isUp = false;
	
	public boolean full() {
		return playerCount >= maxPlayers;
	}
	
	public static HashMap<String, Long> lastconnect = new HashMap<String, Long>();
	public static ArrayList<String> leaving = new ArrayList<String>();
	public void connect(Player p, final boolean spectator) {
		if(lastconnect.containsKey(p.getName())) {
			if(System.currentTimeMillis() - lastconnect.get(p.getName()) < 2000) {
				return;
			}
		}
		lastconnect.put(p.getName(), System.currentTimeMillis());
		if(isUp && (spectator || joinable)) {
			p.sendMessage(ChatColor.GREEN + "Connecting to Server " + id + (spectator ? " as a Spectator" : "") + "...");
			p.setScoreboard(MinigameManager.plugin.getServer().getScoreboardManager().getMainScoreboard());
			HPBar.quit(p);
			final String name = p.getName();
			leaving.add(name);
			MinigameManager.plugin.getServer().getScheduler().runTaskAsynchronously(MinigameManager.plugin, new Runnable() {
				public void run() {
					SQLManager.execute("update playerdata set transfer = '" + index + "', spectate = " + (spectator ? "1" : "0") + " where name = '" + name + "'", true);
					MinigameManager.plugin.getServer().getScheduler().scheduleSyncDelayedTask(MinigameManager.plugin, new Runnable() {
						public void run() {
							leaving.remove(name);
							Player p = MinigameManager.plugin.getServer().getPlayerExact(name);
							if(p != null) {
								ChannelManager.sendChannelChange(p, identifier);
							}
						}
					}, 20);
				}
			});
		} else {
			p.sendMessage(ChatColor.RED + "You can't join Server " + id + " right now!");
		}
	}
	
	public String toString() {
		return identifier;
	}
	
	public Minigame(String id, String title, String game, int maxPlayers, int index) {
		this.identifier = id;
		this.title = title;
		this.game = game;
		this.playerCount = 0;
		this.maxPlayers = maxPlayers;
		this.index = index;
	}
	
}
