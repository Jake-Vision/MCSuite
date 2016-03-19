package xronbo.common;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import xronbo.ronbolobby.RonboLobby;

public class GrappleManager implements Listener {

	public static ItemStack grapple;
	public static boolean loaded = false;
	
	public static void load() {
		loaded = true;
		ItemMeta im;
		
		grapple = new ItemStack(Material.FISHING_ROD);
		im = grapple.getItemMeta();
		im.addEnchant(Enchantment.DURABILITY, 5, true);
		im.setDisplayName(ChatColor.GOLD + "Grappling Hook");
		im.setLore(Arrays.asList(new String[] {ChatColor.GREEN + "Right-click to throw a hook.", "", ChatColor.GREEN + "Right-click again to pull yourself to it!"}));
		grapple.setItemMeta(im);
	}
	
	@EventHandler
	public void onPlayerFishEvent(PlayerFishEvent event) {
		Fish hook = event.getHook();
		if(event.getState() != PlayerFishEvent.State.FISHING) {
			boolean isBlock = false;
	    	for(int dx = -1; dx <= 1; dx++) {
	    		for(int dy = -1; dy <= 1; dy++) {
	    			for(int dz = -1; dz <= 1; dz++) {
	    				if(hook.getWorld().getBlockAt(hook.getLocation().getBlockX() + dx, hook.getLocation().getBlockY() + dy, hook.getLocation().getBlockZ() + dz).getType() != Material.AIR) {
	    					isBlock = true;
	    					break;
	    				}
	    			}
	    		}
	    	}
	    	if(isBlock) {
	    		pullEntityToLocation(event.getPlayer(), hook.getLocation());
	    	} else {
	    		event.getPlayer().sendMessage(ChatColor.RED + "Your grapple failed to hit anything...");
	    	}
		}
	}

	public void pullEntityToLocation(Entity e, Location loc) {
		Location entityLoc = e.getLocation();
	    entityLoc.setY(entityLoc.getY() + 0.5D);
	    e.teleport(entityLoc);
	    double g = -0.08D;
	    double d = loc.distance(entityLoc);
	    double t = d;
	    double v_x = (1.0D + 0.07000000000000001D * t) * (loc.getX() - entityLoc.getX()) / t;
	    double v_y = (1.0D + 0.03D * t) * (loc.getY() - entityLoc.getY()) / t - 0.5D * g * t;
	    double v_z = (1.0D + 0.07000000000000001D * t) * (loc.getZ() - entityLoc.getZ()) / t;
	    Vector v = e.getVelocity();
	    v.setX(v_x);
	    v.setY(v_y);
	    v.setZ(v_z);
	    e.setVelocity(v);
    }
	
	public static void supply(Player p) {
		if(!loaded)
			load();
		p.getInventory().remove(Material.FISHING_ROD);
		p.getInventory().addItem(grapple);
		p.sendMessage("");
		p.sendMessage(ChatColor.GOLD + "Siege Veteran" + ChatColor.WHITE + ": " + ChatColor.LIGHT_PURPLE + "Arrgg.. I remember the Siege of Stonehelm. 'twas brutal!");
		p.sendMessage(ChatColor.GOLD + "Siege Veteran" + ChatColor.WHITE + ": " + ChatColor.LIGHT_PURPLE + "Here, take this Grappling Hook. You should practice with it in case you're ever in a siege!");
		p.sendMessage(ChatColor.GOLD + "Siege Veteran" + ChatColor.WHITE + ": " + ChatColor.LIGHT_PURPLE + "Just " + ChatColor.WHITE + "right click" + ChatColor.LIGHT_PURPLE + " to use it!");
	}
	
	public static RonboLobby plugin;
	public GrappleManager(RonboLobby plugin) {
		GrappleManager.plugin = plugin;
	}
	
}
