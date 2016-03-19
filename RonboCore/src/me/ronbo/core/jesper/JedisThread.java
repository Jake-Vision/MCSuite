package me.ronbo.core.jesper;

import java.util.Vector;

import me.ronbo.core.RonboCore;
import me.ronbo.core.ranks.RankManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisThread extends Thread {
	
	private static final boolean DEBUG = false;
	
	public long lastLength = -1;
	
	@Override
	public void run() {
        while(true) {
    		if(disabled || pool.isClosed())
        		return;
        	Jedis jedis = pool.getResource();
        	try {
            	while(!messageQueue.isEmpty()) {
            		String s = messageQueue.remove(0);
            		try {
            			if(DEBUG)
            				System.out.println("Pushing message: " + s);
            			jedis.rpush("jedis", ChatColor.GRAY + "" + ChatColor.ITALIC + "(" + RonboCore.serverName + ") " + ChatColor.WHITE + s);
            		} catch(Exception e) {
            			e.printStackTrace();
            		}
            	}
            	Long length = jedis.llen("jedis");
            	if(lastLength != length) {
            		lastLength = length;
                	String message = jedis.lindex("jedis", lastLength - 1);
                	if(message != null) {
            			if(DEBUG)
            				System.out.println("Received message: " + message);
                		System.out.println("[JEsper] " + message);
                		for(Player p : RonboCore.plugin.getServer().getOnlinePlayers()) {
                			if(RankManager.check(p, "helper") || p.getName().equals("xRonbo") || p.getName().equals("Edasaki"))
                				p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "JEsper" + ChatColor.GRAY + "] " + ChatColor.WHITE + message);
                		}
                	}
            	}
        		pool.returnResource(jedis);
        		jedis = null;
                try {
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
        	} finally {
        		if(jedis != null)
        			if(pool == null || pool.isClosed())
        				jedis.close();
        			else
        				pool.returnResource(jedis);
        	}
        }
	}
	protected void flush() {
		Jedis jedis = pool.getResource();
		jedis.flushAll();
		pool.returnResource(jedis);
	}
	protected void disable() {
		pool.close();
		pool = null;
		messageQueue = null;
		disabled = true;
	}
	protected void addMessage(String s) {
		messageQueue.add(s);
	}
	private boolean disabled = false;
	private Vector<String> messageQueue = new Vector<String>();
	private JedisPool pool;
	public JedisThread(JedisPool pool) {
		this.pool = pool;
	}
}