package me.ronbo.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class SQLManager {

	public static volatile HikariDataSource ds;
	
	public static void loadSQLConnection(RonboCore plugin) {
		SQLManager.plugin = plugin;
		loadSQLConnection();
	}
	
	public static void stop() {
		if(ds != null) {
			try {
				ds.shutdown();
				ds.close();
				ds = null;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void loadSQLConnection() {
		try {
			System.out.println("Establishing connection...");
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl("jdbc:mysql://" + RonboCore.ip + ":" + RonboCore.SQLPort + "/kastia");
			config.setUsername(RonboCore.SQLUser);
			config.setPassword(RonboCore.SQLPass);
			config.addDataSourceProperty("connectionTimeout", "15000");
			config.addDataSourceProperty("cachePrepStmts", "true");
			config.addDataSourceProperty("prepStmtCacheSize", "250");
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			config.addDataSourceProperty("useServerPrepStmts", "true");
			config.setMaximumPoolSize(10);
			if(ds != null) {
				try {
					ds.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			ds = new HikariDataSource(config);
			System.out.println("HikariDataSource initialized: " + ds);
			System.out.println("Connection complete.");
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("FATAL ERROR: COULD NOT CONNECT TO SQL DATABASE!");
			plugin.getServer().shutdown();
		}
	}
	
	public static void execute(final String input) {
		execute(input, false);
	}
	
	public static void execute(final String input, boolean forced) {
		Runnable r = new Runnable() {
			public void run() {
				String s = input;
				Statement statement = null;
				Connection conn = null;
				if(ds == null) {
					System.out.println("Failed attempt to execute " + input);
					return;
				}
				try {
					conn = ds.getConnection();
					try {
						statement = conn.createStatement();
						statement.execute(s);
					} catch(Exception e) {
						System.out.println("Error: " + input);
						e.printStackTrace();
					} finally {
						if(statement != null)
							statement.close();
					}
				} catch (SQLException e) {
					System.out.println("Error: " + input);
					e.printStackTrace();
				} finally {
					try {
						if(conn != null)
							conn.close();
					} catch(Exception e) {
						System.out.println("Error: " + input);
						e.printStackTrace();
					}
				}
				statement = null;
				conn = null;
			}
		};
		if(forced) {
			r.run();
		} else {
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin, r);
		}
	}
	
	public static void executePrepared(final String input, final boolean forced, final String... values) {
		Runnable r = new Runnable() {
			public void run() {
				String s = input;
				PreparedStatement ps = null;
				Connection conn = null;
				if(ds == null) {
					System.out.println("Failed attempt to execute " + input);
					return;
				}
				try {
					conn = ds.getConnection();
					try {
						ps = conn.prepareStatement(s);
						for(int k = 0; k < values.length; k++)
							ps.setString(k + 1, values[k]);
						ps.executeUpdate();
					} catch (Exception e) {
						System.out.println("Error: " + input);
						e.printStackTrace();
					} finally {
						if(ps != null)
							ps.close();
					}
				} catch (SQLException e) {
					System.out.println("Error: " + input);
					e.printStackTrace();
				} finally {
					try {
						if(conn != null)
							conn.close();
					} catch(Exception e) {
						System.out.println("Error: " + input);
						e.printStackTrace();
					}
				}
				ps = null;
				conn = null;
			}
		};
		if(forced) {
			r.run();
		} else {
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin, r);
		}
	}
	
	public static volatile int current = 0;
	private static final boolean QUERY_DEBUG = false;
	public static CachedRowSet executeQuery(String s) {
		int my = current++;
		long start = System.currentTimeMillis();
		Statement statement = null;
		ResultSet rs = null;
		CachedRowSet crs = null;
		Connection conn = null;
		if(ds == null) {
			System.out.println("Failed attempt to execute " + s);
			return null;
		}
		try {
			if(QUERY_DEBUG)
				System.out.println("Current time0 (" + my + "): " + (System.currentTimeMillis() - start) + " ms.");
			conn = ds.getConnection();
			try {
				if(QUERY_DEBUG)
					System.out.println("Current time1 (" + my + "): " + (System.currentTimeMillis() - start) + " ms.");
				statement = conn.createStatement();
				if(QUERY_DEBUG)
					System.out.println("Current time2 (" + my + "): " + (System.currentTimeMillis() - start) + " ms.");
				try {
					rs = statement.executeQuery(s);
					if(QUERY_DEBUG)
						System.out.println("Current time3 (" + my + "): " + (System.currentTimeMillis() - start) + " ms.");
					crs = RowSetProvider.newFactory().createCachedRowSet();
					crs.populate(rs);
					if(QUERY_DEBUG)
						System.out.println("Current time4 (" + my + "): " + (System.currentTimeMillis() - start) + " ms.");
				} catch (Exception e) {
					System.out.println("Error: " + s);
					e.printStackTrace();
				} finally {
					if(rs != null)
						rs.close();
				}
			} catch (Exception e) {
				System.out.println("Error: " + s);
				e.printStackTrace();
			} finally {
				if(statement != null)
					statement.close();
			}
		} catch (SQLException e) {
			System.out.println("Error: " + s);
			e.printStackTrace();
		} finally {
			try {
				if(conn != null)
					conn.close();
			} catch(Exception e) {
				System.out.println("Error: " + s);
				e.printStackTrace();
			}
		}
		statement = null;
		rs = null;
		conn = null;
		if(QUERY_DEBUG)
			System.out.println("Time to execute (" + my + "): " + (System.currentTimeMillis() - start) + " ms.");
		return crs;
	}
	
	public static RonboCore plugin;
	private SQLManager() {
		
	}
}
