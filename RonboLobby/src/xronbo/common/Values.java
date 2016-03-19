package xronbo.common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import net.minecraft.server.v1_7_R4.NBTTagList;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public final class Values {
	
	public static final int LATENCY_THRESHOLD_HIGH = 300;
	public static final int LATENCY_THRESHOLD_MEDIUM = 150;
	
	public static final double PLAYER_THRESHOLD_HIGH = 0.9;
	public static final double PLAYER_THRESHOLD_MEDIUM = 0.5;
	
	public static final Values values = new Values();

    public static ItemStack removeAttributes(ItemStack item){
    	return removeAttributes(item, false);
    }
    
    public static ItemStack removeAttributes(ItemStack item, boolean glow) {
		try {
		  Object nmsItem = NMSStuff.getNMSItem(item);
		  if(nmsItem == null)
			  return item;
		  Object modifiers = NMSStuff.getNMSClass("NBTTagList").newInstance();
		  Object tag = NMSStuff.getNMSValue("ItemStack", "tag", nmsItem);
		  if(tag == null) {
		      tag = NMSStuff.getNMSClass("NBTTagCompound").newInstance();
		      NMSStuff.setNMSValue("ItemStack", "tag", nmsItem, tag);
		  }
		  Method meth = NMSStuff.getNMSClass("NBTTagCompound").getDeclaredMethod("set", String.class, NMSStuff.getNMSClass("NBTBase"));
		  meth.invoke(tag, "AttributeModifiers", modifiers);
		  if(glow)
			  meth.invoke(tag, "ench", new NBTTagList());
		  NMSStuff.setNMSValue("ItemStack", "tag", nmsItem, tag);
		  return NMSStuff.getBukkitItem(nmsItem);
		} catch (Exception e) {
			
		}
        return item;
    }
    
	public static String capitalize(String s) {
		StringBuilder sb = new StringBuilder("");
		boolean capitalize = true;
		for(int k = 0; k < s.length(); k++) {
			String s2 = s.substring(k, k+1);
			if(capitalize) {
				capitalize = false;
				s2 = s2.toUpperCase();
			}
			if(!s2.matches("[a-zA-z]{1}")) {
				capitalize = true;
			}
			sb.append(s2);
		}
		return sb.toString();
	}

	public static ArrayList<String> longStringToLoreList(ChatColor color, String... strings) {
		ArrayList<String> lore = new ArrayList<String>();
		for(String s : strings) {
			lore.addAll(Arrays.asList(stringToLore(s, color)));
			lore.add("");
		}
		if(lore.size() > 0) {
			if(lore.get(lore.size() - 1).equals(""))
				lore.remove(lore.size() - 1);
		}
		return lore;
	}
	
	public static String[] stringToLore(String s) {
		return stringToLore(s, ChatColor.AQUA);
	}
	
	public static String[] stringToLore(String s, ChatColor color) {
		ArrayList<String> lines = new ArrayList<String>();
		Scanner scan = new Scanner(s);
		StringBuilder temp = new StringBuilder("");
		while(scan.hasNext()) {
			String toAppend = scan.next();
			if(temp.length() + (toAppend + " ").length() > 35) {
				lines.add(color + temp.toString());
				temp.setLength(0);
				temp.append(toAppend + " ");
			} else {
				temp.append(toAppend + " ");
			}
		}
		if(temp.length() > 0)
			lines.add(color + temp.toString());
		scan.close();
		return lines.toArray(new String[lines.size()]);
	}
	
	public static int randInt(int min, int max) {
		return ((int)(Math.random() * (max - min + 1))) + min;
	}
	
	public static double randDouble(double min, double max) {
		return Math.random()*(max - min) + min;
	}
	
	private Values() {
		
	}
}