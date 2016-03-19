package me.ronbo.core.ranks;

import java.util.HashSet;

import me.ronbo.core.RonboCore;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Moderation implements Listener {

	private static HashSet<String> hidden = new HashSet<String>();
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onLogin(PlayerJoinEvent event) {
		final String name = event.getPlayer().getName();
		RonboCore.plugin.getServer().getScheduler().scheduleSyncDelayedTask(RonboCore.plugin, new Runnable() {
			public void run() {
				Player p = RonboCore.plugin.getServer().getPlayerExact(name);
				if(p != null && p.isOnline() && !RankManager.check(p, "helper")) {
					for(String s : hidden) {
						Player p2 = RonboCore.plugin.getServer().getPlayerExact(s);
						if(p2 != null && p2.isOnline() && p2 != p)
							p.hidePlayer(p2);
					}
				}
			}
		}, 20);
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onLogoff(PlayerQuitEvent event) {
		if(hidden.contains(event.getPlayer().getName())) {
			hidden.remove(event.getPlayer().getName());
			for(Player p2 : RonboCore.plugin.getServer().getOnlinePlayers())
				p2.showPlayer(event.getPlayer());
		}
	}
	
	public static void hide(Player p) {
		hidden.add(p.getName());
		for(Player p2 : RonboCore.plugin.getServer().getOnlinePlayers())
			if(p2 != p && !RankManager.check(p2, "helper"))
				p2.hidePlayer(p);
		p.sendMessage(ChatColor.GREEN + "You are now hidden from all non-staff.");
	}
	
	public static void show(Player p) {
		hidden.remove(p.getName());
		for(Player p2 : RonboCore.plugin.getServer().getOnlinePlayers())
			p2.showPlayer(p);
		p.sendMessage(ChatColor.GREEN + "You are now visible to all players.");
	}
	
	public static void tpTo(Player p, Player target) {
		p.teleport(target);
		p.sendMessage(ChatColor.GREEN + "You teleported to " + target.getName() + ".");
	}
	
	public static void tpHere(Player p, Player target) {
		target.teleport(p);
		p.sendMessage(ChatColor.GREEN + "You teleported " + target.getName() + " to you.");
		target.sendMessage(ChatColor.GREEN + "You have been teleported to " + p.getName() + ".");
	}
	
	public static void teleport(Player p, double x, double y, double z) {
		Location loc = new Location(p.getWorld(), x, y, z);
		p.teleport(loc);
		p.sendMessage(ChatColor.GREEN + "Teleported to (" + x + ", " + y + ", " + z + ").");
	}
	
	public static void fly(Player p) {
		p.setAllowFlight(!p.getAllowFlight());
		p.sendMessage(ChatColor.YELLOW + "Flying is now " + (p.getAllowFlight() ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED") + ChatColor.YELLOW + ".");
	}
	
	public static void flySpeed(Player p, String speed) {
		try {
			float f = Float.parseFloat(speed);
			p.setFlySpeed(f);
			p.sendMessage(ChatColor.GREEN + "Set fly speed to " + ChatColor.WHITE + f + ChatColor.GREEN + "!");
		} catch(Exception e) {
			p.sendMessage(ChatColor.RED + "Range should be 0.0-1.0. Like this: /flyspeed 0.8");
		}
	}
	
}
