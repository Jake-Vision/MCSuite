package xronbo.ronbolobby.bungee;

import me.ronbo.core.ranks.RankManager;

import org.bukkit.entity.Player;

public class Channel implements Comparable<Channel> {

	public static final int MAX_PLAYERS = 200;
	
	public int reqRank;
	public String name;
	public String description;
	public String id;
	public String abbreviation;
	public volatile int playerCount = 0;
	public volatile boolean isUp = false;
	public volatile int latency = 0;
	
	public void connect(Player p) {
		ChannelManager.sendChannelChange(p, id);
	}
	
	public boolean canConnect(Player p) {
		if(!isUp)
			return false;
		int rank = RankManager.getRankPower(p.getName());
		if(rank >= reqRank) {
			return true;
		}
		return false;
	}
	
	public int compareTo(Channel other) {
		if(this.isUp && !other.isUp)
			return -1;
		if(!this.isUp)
			return 1;
		return this.playerCount + this.latency * 2 - other.playerCount - other.latency * 2;
	}
	
	public String toString() {
		return id;
	}
	
	public Channel(String id, String abbreviation, String name, int rank, String description) {
		this.id = id;
		this.abbreviation = abbreviation;
		this.reqRank = rank;
		this.name = name;
		this.description = description;
	}
	
}
