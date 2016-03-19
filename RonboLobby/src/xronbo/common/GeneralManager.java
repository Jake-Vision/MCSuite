package xronbo.common;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;

import xronbo.ronbolobby.RonboLobby;

public class GeneralManager implements Listener {

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (event.getCause() == DamageCause.VOID
				&& event.getEntity() instanceof Player) {
			((Player) event.getEntity()).sendMessage(ChatColor.RED
					+ "Woah! Careful there, pal!");
			event.getEntity().teleport(
					event.getEntity().getWorld().getSpawnLocation());
		}
	}
	
	@EventHandler
	public void onNewChunk(ChunkLoadEvent event) {
		if(event.isNewChunk())
			event.getChunk().unload(false, false);
	}

	@EventHandler
	public void onPlayerInteractCrops(PlayerInteractEvent event) {
		if (event.getAction() == Action.PHYSICAL) {
			Block b = event.getClickedBlock();
			if (b.getType() == Material.SOIL) {
				event.setCancelled(true);
				b.setTypeIdAndData(b.getType().getId(), b.getData(), true);
			}
			if (b.getType() == Material.CROPS) {
				event.setCancelled(true);
				b.setTypeIdAndData(b.getType().getId(), b.getData(), true);
			}
		}
	}

	@EventHandler
	public void onPvP(EntityDamageEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onThunderChange(ThunderChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onDropItem(PlayerDropItemEvent event) {
		try {
			event.setCancelled(true);
		} catch (Exception e) {

		}
	}

	@EventHandler
	public void onWorldLoad(WorldLoadEvent event) {
		World w = event.getWorld();
		if (w.hasStorm())
			w.setStorm(false);
		if (w.isThundering())
			w.setThundering(false);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!(event.getPlayer().getName().equals("xRonbo") || event.getPlayer().getName().equals("Edasaki") || event.getPlayer().isOp()))
			event.setCancelled(true);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!(event.getPlayer().getName().equals("xRonbo") || event.getPlayer().getName().equals("Edasaki") || event.getPlayer().isOp()))
			event.setCancelled(true);
	}

	@EventHandler
	public void onLeafDecay(LeavesDecayEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockFade(BlockFadeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockForm(BlockFormEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void hungerDecay(FoodLevelChangeEvent event) {
		((Player) event.getEntity()).setFoodLevel(20);
		event.setCancelled(true);
	}

	public static RonboLobby plugin;

	public GeneralManager(RonboLobby plugin) {
		GeneralManager.plugin = plugin;
	}
}
