package me.ronbo.core;

import me.ronbo.core.ranks.RankManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GemManager {

	public static void giveGems(Player p, int i) {
		String name = p.getName();
		int multiplier = 1;
		if(RankManager.check(p, "royal"))
			multiplier = 4;
		else if(RankManager.check(p, "noble"))
			multiplier = 3;
		else if(RankManager.check(p, "knight"))
			multiplier = 2;
		i *= multiplier;
		p.sendMessage(ChatColor.GRAY + " >> +" + i + " Gem" + (i > 1 ? "s" : "") + (multiplier > 1 ? " [x" + multiplier + " multiplier]" : "[No Multiplier]"));
		SQLManager.execute("update playerdata set points = points + " + i + " where name = '" + name + "'");
	}
	
}
