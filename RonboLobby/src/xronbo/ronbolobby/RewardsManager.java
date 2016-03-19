package xronbo.ronbolobby;

import java.util.HashMap;

import me.ronbo.core.SQLManager;
import me.ronbo.core.ranks.RankManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class RewardsManager {
	
	public static void giveReward(String name, int id) {
		int knightdays = 0;
		int nobledays = 0;
		System.out.println("Rewarding RID " + id + " to player " + name + ".");
		switch(id) {
			case 1:
				knightdays = 30;
				plugin.getServer().broadcastMessage("");
				plugin.getServer().broadcastMessage(ChatColor.GRAY + "----------------------------------------------------");
				plugin.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + name + " just bought " + knightdays + " days of Knight!");
				plugin.getServer().broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Thanks for your support, " + name + "! " + ChatColor.RED + "\u2764");
				plugin.getServer().broadcastMessage(ChatColor.GRAY + "----------------------------------------------------");
				SQLManager.execute("update playerdata set newKnightDays = newKnightDays + " + knightdays + " where name = '" + name + "'");
				if(plugin.getServer().getPlayerExact(name) != null) {
					Player temp = plugin.getServer().getPlayerExact(name);
					temp.sendMessage(ChatColor.GREEN + "Your purchase was just processed!");
					temp.sendMessage(ChatColor.GREEN + "You will need to " + ChatColor.BOLD + "relog" + ChatColor.GREEN + " to get your rank!");
					RonboLobby.stats.put(temp.getName(), new Stats(temp.getName(), temp.getUniqueId().toString()));
					RankManager.loadPlayer(temp);
				}
				break;
			case 2:
				nobledays = 90;
				plugin.getServer().broadcastMessage("");
				plugin.getServer().broadcastMessage(ChatColor.GRAY + "----------------------------------------------------");
				plugin.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + name + " just bought " + nobledays + " days of Noble!");
				plugin.getServer().broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Thanks for your support, " + name + "! " + ChatColor.RED + "\u2764");
				plugin.getServer().broadcastMessage(ChatColor.GRAY + "----------------------------------------------------");
				SQLManager.execute("update playerdata set newNobleDays = newNobleDays + " + nobledays + " where name = '" + name + "'");
				if(plugin.getServer().getPlayerExact(name) != null) {
					Player temp = plugin.getServer().getPlayerExact(name);
					temp.sendMessage(ChatColor.GREEN + "Your purchase was just processed!");
					temp.sendMessage(ChatColor.GREEN + "You will need to " + ChatColor.BOLD + "relog" + ChatColor.GREEN + " to get your rank!");
					RonboLobby.stats.put(temp.getName(), new Stats(temp.getName(), temp.getUniqueId().toString()));
					RankManager.loadPlayer(temp);
				}
				break;
			case 3:
				plugin.getServer().broadcastMessage("");
				plugin.getServer().broadcastMessage(ChatColor.GRAY + "----------------------------------------------------");
				plugin.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + name + " just bought Royal!");
				plugin.getServer().broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Thanks for your support, " + name + "! " + ChatColor.RED + "\u2764");
				plugin.getServer().broadcastMessage(ChatColor.GRAY + "----------------------------------------------------");
				SQLManager.execute("update playerdata set rankString = 'royal' where name = '" + name + "'");
				if(plugin.getServer().getPlayerExact(name) != null) {
					Player temp = plugin.getServer().getPlayerExact(name);
					temp.sendMessage(ChatColor.GREEN + "Your purchase was just processed!");
					temp.sendMessage(ChatColor.GREEN + "Wait a few minutes for it to take effect!");
					RonboLobby.stats.put(temp.getName(), new Stats(temp.getName(), temp.getUniqueId().toString()));
					RankManager.loadPlayer(temp);
				}
				break;
			case 4:
				addGems(name, 500);
				break;
			case 5:
				addGems(name, 1000);
				break;
			case 6:
				addGems(name, 2500);
				break;
			case 7:
				addGems(name, 5000);
				break;
			case 8:
				addGems(name, 10000);
				break;
			case 9:
				addGems(name, 10000);
				plugin.getServer().broadcastMessage("");
				plugin.getServer().broadcastMessage(ChatColor.GRAY + "----------------------------------------------------");
				plugin.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + name + " just bought LORD!");
				plugin.getServer().broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Thanks for your support, " + name + "! " + ChatColor.RED + "\u2764");
				plugin.getServer().broadcastMessage(ChatColor.GRAY + "----------------------------------------------------");
				SQLManager.execute("update playerdata set rankString = 'lord' where name = '" + name + "'");
				if(plugin.getServer().getPlayerExact(name) != null) {
					Player temp = plugin.getServer().getPlayerExact(name);
					temp.sendMessage(ChatColor.GREEN + "Your purchase was just processed!");
					temp.sendMessage(ChatColor.GREEN + "Wait a few minutes for it to take effect!");
					RonboLobby.stats.put(temp.getName(), new Stats(temp.getName(), temp.getUniqueId().toString()));
					RankManager.loadPlayer(temp);
				}
				break;
//			case 9:
				//pegasus
//				break;
//			case 10:
//				givePet(name, "babychicken", "Baby Chicken");
//				break;
//			case 11:
//				givePet(name, "babypig", "Baby Pig");
//				break;
//			case 12:
//				givePet(name, "babycow", "Baby Cow");
//				break;
//			case 13:
//				givePet(name, "sheep", "Sheep");
//				break;
//			case 14:
//				givePet(name, "babysheep", "Baby Sheep");
//				break;
//			case 15:
//				givePet(name, "spider", "Spider");
//				break;
//			case 16:
//				givePet(name, "cavespider", "Cave Spider");
//				break;
//			case 17:
//				givePet(name, "irongolem", "Iron Golem");
//				break;
//			case 18:
//				givePet(name, "mushroomcow", "Mooshroom");
//				break;
//			case 19:
//				givePet(name, "babymushroomcow", "Baby Mooshroom");
//				break;
//			case 20:
//				givePet(name, "pigzombie", "Zombie Pigman");
//				break;
//			case 21:
//				givePet(name, "babypigzombie", "Baby Pigman");
//				break;
//			case 22:
//				givePet(name, "skeleton", "Skeleton");
//				break;
//			case 23:
//				givePet(name, "wolf", "Wolf");
//				break;
//			case 24:
//				givePet(name, "angrywolf", "Angry Wolf");
//				break;
//			case 25:
//				givePet(name, "zombie", "Zombie");
//				break;
//			case 26:
//				givePet(name, "babyzombie", "Baby Zombie");
//				break;
//			case 27:
//				givePet(name, "creeper", "Creeper");
//				break;
//			case 28:
//				givePet(name, "ocelot", "Ocelot");
//				break;
//			case 29:
//				givePet(name, "babyocelot", "Baby Ocelot");
//				break;
//			case 30:
//				givePet(name, "babywolf", "Baby Wolf");
//				break;
//			case 31:
//				givePet(name, "witherskeleton", "Wither Skeleton");
//				break;
		}
	}
	
//	public static void givePet(String name, String type, String display) {
//		String uuid = Bukkit.getOfflinePlayer(name).getUniqueId().toString();
//		plugin.getServer().broadcastMessage(ChatColor.GRAY + "----------------------------------------------------");
//		plugin.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + name + " just bought the RPG Pet, " + display +"!");
//		plugin.getServer().broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Thanks for your support, " + name + "! " + ChatColor.RED + "\u2764");
//		plugin.getServer().broadcastMessage(ChatColor.GRAY + "----------------------------------------------------");
//		String[] strings = {
//				uuid,
//				name + "'s Pet",
//				"1",
//				"0",
//				"",
//				type,
//				type
//		};
//		Player p = plugin.getServer().getPlayerExact(name);
//		SQLManager.executePrepared("insert into pets (uuid, name, level, exp, activePowers, type, ownedTypes) "
//				+ "VALUES (?, ?, ?, ?, ?, ?, ?) on duplicate key update ownedTypes = concat(ownedTypes, ' " + type + "');", true, strings);
//		if(p != null) {
//			p.sendMessage("");
//			p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "WARNING: To be safe, wait about a minute before logging in to the RPG to try your new pet!");
//			p.sendMessage(ChatColor.RED + "Thank you for your purchase!");
//		}
//	}
	
	public static HashMap<String, Long> lastmsg = new HashMap<String, Long>();
	public static void voted(String name) {
		boolean msg = true;
		if(lastmsg.containsKey(name.toLowerCase())) {
			if(System.currentTimeMillis() - lastmsg.get(name.toLowerCase()) < 60000) {
				msg = false;
			}
		}
		lastmsg.put(name.toLowerCase(), System.currentTimeMillis());
		if(msg) {
			plugin.getServer().broadcastMessage("");
			plugin.getServer().broadcastMessage(ChatColor.AQUA + name + " just voted and received some sweet rewards!");
			plugin.getServer().broadcastMessage(ChatColor.AQUA + "Please vote daily at www.kastia.net! (\"Vote for Rewards\")");
		}
		SQLManager.execute("update playerdata set points = points + 10 where name = '" + name + "'");
		if(plugin.getServer().getPlayerExact(name) != null) {
			Player temp = plugin.getServer().getPlayerExact(name);
			temp.sendMessage(ChatColor.GREEN + "You received your rewards for 1 Vote! Thanks!");
			RonboLobby.stats.put(temp.getName(), new Stats(temp.getName(), temp.getUniqueId().toString()));
		}
	}
	
	public static void addGems(String name, int gems) {
		plugin.getServer().broadcastMessage("");
		plugin.getServer().broadcastMessage(ChatColor.GRAY + "----------------------------------------------------");
		plugin.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + name + " just bought " + gems + ChatColor.GREEN + " Gems" + ChatColor.GOLD + "!");
		plugin.getServer().broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Thanks for your support, " + name + "! " + ChatColor.RED + "\u2764");
		plugin.getServer().broadcastMessage(ChatColor.GRAY + "----------------------------------------------------");
		SQLManager.execute("update playerdata set points = points + " + gems + " where name = '" + name + "'");
		if(plugin.getServer().getPlayerExact(name) != null) {
			Player temp = plugin.getServer().getPlayerExact(name);
			temp.sendMessage(ChatColor.GREEN + "Your purchase was just processed!");
			temp.sendMessage(ChatColor.GREEN + "Wait a few minutes for it to take effect!");
			RonboLobby.stats.put(temp.getName(), new Stats(temp.getName(), temp.getUniqueId().toString()));
		}
	}
	
	public static RonboLobby plugin;
}
