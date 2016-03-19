package xronbo.ronbolobby.entitytypes;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityVillager;
import net.minecraft.server.v1_7_R4.Material;
import net.minecraft.server.v1_7_R4.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R4.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R4.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R4.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R4.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R4.World;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.util.UnsafeList;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import xronbo.common.ArcheryManager;
import xronbo.common.GrappleManager;
import xronbo.ronbolobby.RonboLobby;
import xronbo.ronbolobby.bungee.MinigameManager;
import xronbo.ronbolobby.bungee.MinigameManager.MinigameType;

public class CustomVillager extends EntityVillager {

	public long lastWaterTP = 0;
	
	@Override
	public boolean M() { //inwater
		if(super.M()) {
			LivingEntity le = (LivingEntity)this.getBukkitEntity();
			le.addPotionEffect(PotionEffectType.SPEED.createEffect(10, 10));
			if(System.currentTimeMillis() - lastWaterTP > 7000) {
				lastWaterTP = System.currentTimeMillis();
	            double d0 = 16.0D;
	            EntityHuman p = this.world.findNearbyVulnerablePlayer(this, d0);
	            if(p != null) {
	            	this.getBukkitEntity().teleport(p.getBukkitEntity().getLocation());
	            }
			}
			if(!this.world.a(this.boundingBox.grow(0.0D, -0.4000000059604645D, 0.0D).shrink(0.001D, 0.001D, 0.001D), Material.WATER, this)) {
				le.removePotionEffect(PotionEffectType.SPEED);
			}
			return true;
		}
		return false;
	}
	
	public long lastLavaJump = 0;
	
	@Override
	public boolean P() { //in lava
		if(super.P()) {
			if(System.currentTimeMillis() - lastLavaJump > 1000) {
				lastLavaJump = System.currentTimeMillis();
				LivingEntity le = (LivingEntity)this.getBukkitEntity();
				le.setVelocity(new Vector(le.getVelocity().getX(), 9, le.getVelocity().getZ()).normalize());
			}
			LivingEntity le = (LivingEntity)this.getBukkitEntity();
			double x = le.getVelocity().getX() * 10;
			if(x < 3)
				x = 3;
			double z = le.getVelocity().getZ() * 10;
			if(z < 3)
				z = 3;
			le.setVelocity(new Vector(x, le.getVelocity().getY(), z).normalize());
			return true;
		} else {
			return false;
		}
	}

	public CustomVillager(World world) {
		super(world);
		try {
			Field gsa = PathfinderGoalSelector.class.getDeclaredField("b");
			gsa.setAccessible(true);
			gsa.set(this.goalSelector, new UnsafeList<Object>());
			gsa.set(this.targetSelector, new UnsafeList<Object>());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(1, new PathfinderGoalRandomLookaround(this));
		this.goalSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
	}
	
	public String toString() {
		return this.getCustomName() + " " + this.getUniqueID();
	}
	
	public void allowWalk() {
	    this.goalSelector.a(3, new PathfinderGoalRandomStroll(this, 0.6D));
	}
	
	@Override
	public void collide(Entity entity) {
		
	}
	
	@Override
	public void g(double d0, double d1, double d2) {
		
	}
	
	public static String[] lines = {
		"I'm King Ronbo, ruler of Kastia!",
		"I was named after Ronbo, one of the Seven Spirits.",
		"I rule with an open heart and an iron fist.",
		"Stop by the Stonehelm throne room sometime!",
		"What class am I? Well, I keep that a secret.",
		"Lucky Blocks are created by our best magicians!",
		"Salut! Vous parlez français? Moi je le parle un peu. Vous pouvez toujours me parlez en français, j'aime le faire!",
		"こんにちは！こちらでアニメへの参照の多くありますね！日本語はまだうまくないけど, 頑張ります。",
		"你好！我的中文名字是范龙博。我是中国人，可是我在加拿大出生的。",
	};
	
	public boolean a(EntityHuman entityhuman) {
        if (this.isAlive()) {
            if (!this.world.isStatic) {
                this.a_(entityhuman);
                if(((LivingEntity)this.getBukkitEntity()).getCustomName() != null) {
                	String name = ((LivingEntity)this.getBukkitEntity()).getCustomName();
                    Player p = (Player)(entityhuman.getBukkitEntity());
                	if(name.contains("Archer")) {
                		ArcheryManager.supply(p);
                	} else if(name.contains("Siege")) {
                		GrappleManager.supply(p);
                	} else if(name.contains("King")) {
                		p.sendMessage(ChatColor.GOLD + "King Ronbo" + ChatColor.WHITE + ": " + ChatColor.AQUA + lines[(int)(Math.random() * lines.length)]);
                	} else if(name.contains("GamingGetsEnder")) {
                		p.sendMessage(ChatColor.WHITE + "GamingGetsEnder" + ChatColor.WHITE + ": Hi, I'm GamingGetsEnder! I afked in the hub for like 3 weeks non-stop, so Ronbo made me into an NPC because I became a symbol of the Kastia hub and also he's scared maybe I died in real life while playing Kastia!");
                	} else if(name.contains("Fred")) {
                		p.sendMessage(ChatColor.RED + "Factions Fred" + ChatColor.WHITE + ": Hiya! Have fun on Kastia Factions!");
                	} else if(name.contains("Lucky Block Wars")) {
                		MinigameManager.showMenu(MinigameType.SKY, p);
                	} else if(name.contains("Ronbattle")) {
                		MinigameManager.showMenu(MinigameType.RONBATTLE, p);
                	} else if(name.contains("Survival Games")) {
                		MinigameManager.showMenu(MinigameType.SG, p);
                	} else if(name.contains("Factions")) {
    					p.teleport(new Location(RonboLobby.plugin.getServer().getWorld("fhub"), -1743.5, 107.6, 93.5, 90f, -1.95f).add((int)(Math.random() * 5 - 2), 0, (int)(Math.random() * 5 - 2)));
    					RonboLobby.plugin.setFactionsInventory(p);
                	}
                }
            }
            return true;
        } else {
            return super.a(entityhuman);
        }
    }
	
}