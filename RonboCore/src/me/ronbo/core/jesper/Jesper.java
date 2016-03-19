package me.ronbo.core.jesper;

import me.ronbo.core.RonboCore;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Jesper {

	private static JedisThread thread;
	
	public static void load() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(128);
		JedisPool pool = new JedisPool(config, RonboCore.ip, Integer.parseInt(RonboCore.jedisPort), 0);
		thread = new JedisThread(pool);
		thread.start();
	}
	
	public static void flush() {
		thread.flush();
	}
	
	public static void stop() {
		thread.disable();
		thread.interrupt();
		thread = null;
	}
	
	public static void sendMessage(String s) {
		thread.addMessage(s);
	}
	
	
	
}