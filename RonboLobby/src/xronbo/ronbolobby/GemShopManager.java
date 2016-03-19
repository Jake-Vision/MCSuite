package xronbo.ronbolobby;

import java.util.ArrayList;

import me.ronbo.core.SQLManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xronbo.common.Values;

public class GemShopManager implements Listener {

	@EventHandler
	public void oninteract(PlayerInteractEvent event) {
		if(event.getItem() != null && event.getItem().getType() == Material.EMERALD) {
			showMenu(event.getPlayer());
			event.setCancelled(true);
		}
	}
	
	public void showMenu(Player p) {
		Inventory inventory = Bukkit.createInventory(p, 9, ChatColor.BLACK + "           " + ChatColor.UNDERLINE + "The Gem Shop");
		ItemStack item = null;
		ItemMeta im = null;
		ArrayList<String> lore = null;
		Stats stats = RonboLobby.getStats(p);
		
//		item = new ItemStack(Material.ROTTEN_FLESH);
//		im = item.getItemMeta();
//		im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Zombo Attack");
//		lore = new ArrayList<String>();
//		lore.add(ChatColor.GRAY + "Click to Browse!");
//		lore.add("");
//		lore.add(ChatColor.RED + "Currently disabled.");
//		im.setLore(lore);
//		item.setItemMeta(im);
//		inventory.setItem(0, item);
//		
//		item = new ItemStack(Material.FISHING_ROD);
//		im = item.getItemMeta();
//		im.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Siege");
//		lore = new ArrayList<String>();
//		lore.add(ChatColor.GRAY + "Click to Browse!");
//		lore.add("");
//		lore.add(ChatColor.RED + "Currently disabled.");
//		im.setLore(lore);
//		item.setItemMeta(im);
//		inventory.setItem(1, Values.removeAttributes(item));

		
		item = new ItemStack(Material.PAPER);
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Welcome to the Gem Shop!");
		lore = new ArrayList<String>();
		lore.add("");
		lore.add(ChatColor.GOLD + "You currently have:");
		lore.add(ChatColor.YELLOW + "" + stats.points + " Gems");
		lore.add("");
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Buy extra Gems at store.kastia.net!");
		im.setLore(lore);
		item.setItemMeta(im);
		inventory.setItem(0, Values.removeAttributes(item));

		item = new ItemStack(Material.APPLE);
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Goodies");
		lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Click to Browse!");
		lore.add("");
		lore.add(ChatColor.GOLD + "Fun stuff to use in the Hub!");
		lore.add(ChatColor.GOLD + "Pets, effects, outfits, and more!");
		im.setLore(lore);
		item.setItemMeta(im);
		inventory.setItem(2, Values.removeAttributes(item));
		
		item = new ItemStack(Material.DIAMOND_SWORD);
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Ronbattle");
		lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Click to Browse!");
		lore.add("");
		lore.add(ChatColor.GOLD + "Buy Ronbattle Battle Points!");
		im.setLore(lore);
		item.setItemMeta(im);
		inventory.setItem(3, Values.removeAttributes(item));

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLUE.getData());
		im = item.getItemMeta();
		im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "More coming soon!");
		lore = new ArrayList<String>();
		lore.add("");
		im.setLore(lore);
		item.setItemMeta(im);
		inventory.setItem(4, Values.removeAttributes(item));
		
		p.openInventory(inventory);
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		try {
			if(event.getInventory().getName().contains("The Gem Shop")) {
				event.setCancelled(true);
				Player p = (Player)event.getWhoClicked();
				ItemStack clickedItem = event.getCurrentItem();
				p.closeInventory();
				ItemStack item = null;
				ItemMeta im = null;
				ArrayList<String> lore = null;
				if(clickedItem.getItemMeta().getDisplayName().toLowerCase().contains("Lucky Block Wars")) {
					Inventory inventory = Bukkit.createInventory(p, 9, ChatColor.BLACK + "" + "Lucky Block Wars");
					p.openInventory(inventory);
				} else if(clickedItem.getItemMeta().getDisplayName().toLowerCase().contains("goodies")) {
					Inventory inventory = Bukkit.createInventory(p, 9, ChatColor.BLACK + "" + "Kastia Goodies!");
					
					item = new ItemStack(Material.SKULL_ITEM, 1, (byte)SkullType.CREEPER.ordinal());
					im = item.getItemMeta();
					im.setDisplayName(ChatColor.WHITE + "Hub Pets");
					lore = new ArrayList<String>();
					lore.add(ChatColor.GRAY + "Click to Browse!");
					lore.add("");
					lore.add(ChatColor.YELLOW + "Get a cute little pet!");
					im.setLore(lore);
					item.setItemMeta(im);
					inventory.setItem(0, item);
					
					item = new ItemStack(Material.JUKEBOX);
					im = item.getItemMeta();
					im.setDisplayName(ChatColor.WHITE + "Effect Trails");
					lore = new ArrayList<String>();
					lore.add(ChatColor.GRAY + "Click to Browse!");
					lore.add("");
					lore.add(ChatColor.YELLOW + "Wow, you are fancy!");
					im.setLore(lore);
					item.setItemMeta(im);
					inventory.setItem(1, item);
					
					p.openInventory(inventory);
				} else if(clickedItem.getItemMeta().getDisplayName().toLowerCase().contains("ronbattle")) {
					Inventory inventory = Bukkit.createInventory(p, 9, ChatColor.BLACK + "" + "Ronbattle Battle Points");
					
					item = new ItemStack(Material.LAPIS_BLOCK);
					im = item.getItemMeta();
					im.setDisplayName(ChatColor.WHITE + "50 Battle Points");
					lore = new ArrayList<String>();
					lore.add(ChatColor.GOLD + "Price: " + ChatColor.YELLOW + "150 Gems");
					lore.add(ChatColor.GRAY + "Click to Buy!");
					lore.add("");
					lore.add(ChatColor.YELLOW + "Get 50 BP in Ronbattle!");
					im.setLore(lore);
					item.setItemMeta(im);
					inventory.setItem(0, item);
					
					item = new ItemStack(Material.IRON_BLOCK);
					im = item.getItemMeta();
					im.setDisplayName(ChatColor.WHITE + "100 Battle Points");
					lore = new ArrayList<String>();
					lore.add(ChatColor.GOLD + "Price: " + ChatColor.YELLOW + "250 Gems");
					lore.add(ChatColor.GRAY + "Click to Buy!");
					lore.add("");
					lore.add(ChatColor.YELLOW + "Get 100 BP in Ronbattle!");
					im.setLore(lore);
					item.setItemMeta(im);
					inventory.setItem(1, item);
					
					item = new ItemStack(Material.EMERALD_BLOCK);
					im = item.getItemMeta();
					im.setDisplayName(ChatColor.WHITE + "200 Battle Points");
					lore = new ArrayList<String>();
					lore.add(ChatColor.GOLD + "Price: " + ChatColor.YELLOW + "460 Gems");
					lore.add(ChatColor.GRAY + "Click to Buy!");
					lore.add("");
					lore.add(ChatColor.YELLOW + "Get 200 BP in Ronbattle!");
					im.setLore(lore);
					item.setItemMeta(im);
					inventory.setItem(2, item);
					
					item = new ItemStack(Material.GOLD_BLOCK);
					im = item.getItemMeta();
					im.setDisplayName(ChatColor.WHITE + "500 Battle Points");
					lore = new ArrayList<String>();
					lore.add(ChatColor.GOLD + "Price: " + ChatColor.YELLOW + "1000 Gems");
					lore.add(ChatColor.GRAY + "Click to Buy!");
					lore.add("");
					lore.add(ChatColor.YELLOW + "Get 500 BP in Ronbattle!");
					im.setLore(lore);
					item.setItemMeta(im);
					inventory.setItem(3, item);
					
					item = new ItemStack(Material.DIAMOND_BLOCK);
					im = item.getItemMeta();
					im.setDisplayName(ChatColor.WHITE + "1000 Battle Points");
					lore = new ArrayList<String>();
					lore.add(ChatColor.GOLD + "Price: " + ChatColor.YELLOW + "1850 Gems");
					lore.add(ChatColor.GRAY + "Click to Buy!");
					lore.add("");
					lore.add(ChatColor.YELLOW + "Get 1000 BP in Ronbattle!");
					im.setLore(lore);
					item.setItemMeta(im);
					inventory.setItem(4, item);
					
					p.openInventory(inventory);
				} else if(clickedItem.getItemMeta().getDisplayName().toLowerCase().contains("tetris escape")) {
					
				}
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().toLowerCase().contains("hub pets")) {
				PetManager.showMenu((Player)event.getWhoClicked());
			} else if(event.getCurrentItem().getItemMeta().getDisplayName().toLowerCase().contains("effect trails")) {
				EffectManager.showMenu((Player)event.getWhoClicked());
			} else if(!event.getInventory().getName().contains("Kastia Pets") && !event.getInventory().getName().contains("Effects")) {
				int price = -1;
				for(String s : event.getCurrentItem().getItemMeta().getLore()) {
					if(s.contains("Price:")) {
						price = Integer.parseInt(ChatColor.stripColor(s).replaceAll("[^0-9]", ""));
						break;
					}
				}
				if(price > -1) {
					Player p = ((Player)event.getWhoClicked());
					Stats stats = RonboLobby.getStats(p);
					if(stats != null) {
						if(stats.points >= price) {
							stats.points -= price;
							String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
							System.out.println(name.toLowerCase());
							if(name.toLowerCase().contains("battle points")) {
								int numGems = Integer.parseInt(name.replaceAll("[^0-9]", ""));
								SQLManager.execute("update playerdata set kitpoints = kitpoints + " + numGems + " where uuid = '" + stats.uuid + "'");
							}
							p.sendMessage(ChatColor.GOLD + "You bought " + event.getCurrentItem().getItemMeta().getDisplayName() + ChatColor.GOLD + " for " + price + " Gems! " + ChatColor.GRAY + "[" + stats.points + " Gems Left]");
							p.sendMessage(ChatColor.GOLD + "It may take a few minutes for you to receive the perk in-game.");
							SQLManager.execute("update playerdata set points = points - " + price + " where uuid = '" + stats.uuid + "'");
						} else {
							p.sendMessage(ChatColor.RED + "You do not have enough Gems to buy that!");
						}
						p.closeInventory();
					}
				}
			}
		} catch(Exception e) {
			
		}
	}
	
	public GemShopManager(RonboLobby plugin) {
		GemShopManager.plugin = plugin;
	}
	
	public static RonboLobby plugin;
}
