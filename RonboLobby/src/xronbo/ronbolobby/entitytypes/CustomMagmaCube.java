package xronbo.ronbolobby.entitytypes;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.Material;
import net.minecraft.server.v1_7_R4.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R4.World;

import org.bukkit.craftbukkit.v1_7_R4.util.UnsafeList;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;


public class CustomMagmaCube extends CustomSlime {

	public boolean isPet;

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
	public CustomMagmaCube(World world, boolean isPet) {
		super(world);
		this.isPet = true;
		try {
			Field gsa = PathfinderGoalSelector.class.getDeclaredField("b");
			gsa.setAccessible(true);
			gsa.set(this.goalSelector, new UnsafeList<Object>());
			gsa.set(this.targetSelector, new UnsafeList<Object>());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//https://forums.bukkit.org/threads/tutorial-1-7-5-wasd-entity-riding.163019/
    @Override
	public void e(float sideMot, float forMot) {
        if (!isPet || this.passenger == null || !(this.passenger instanceof EntityHuman)) {
            super.e(sideMot, forMot);
            this.W = 0.5F;    // Make sure the entity can walk over half slabs, instead of jumping
            return;
        }
 
        this.lastYaw = this.yaw = this.passenger.yaw;
        this.pitch = this.passenger.pitch * 0.5F;
 
        // Set the entity's pitch, yaw, head rotation etc.
        this.b(this.yaw, this.pitch);
        this.aO = this.aM = this.yaw;
 
        this.W = 1.0F;    // The custom entity will now automatically climb up 1 high blocks
 
        sideMot = ((EntityLiving) this.passenger).bd * 0.5F;
        forMot = ((EntityLiving) this.passenger).be;
 
        if (forMot <= 0.0F) {
            forMot *= 0.25F;    // Make backwards slower
        }
        sideMot *= 0.75F;    // Also make sideways slower
 
        float speed = 0.30F;    // 0.2 is the default entity speed. I made it slightly faster so that riding is better than walking
        this.i(speed);    // Apply the speed
        super.e(sideMot, forMot);    // Apply the motion to the entity
 
 
        Field jump = null;
        try {
            jump = EntityLiving.class.getDeclaredField("bc");
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
            return;
        }
        jump.setAccessible(true);
 
        if (jump != null && this.onGround) {    // Wouldn't want it jumping while on the ground would we?
            try {
                if (jump.getBoolean(this.passenger)) {
                    double jumpHeight = 0.6D;
                    this.motY = jumpHeight;    // Used all the time in NMS for entity jumping
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    
	public CustomMagmaCube(World world) {
		super(world);
	}
	
}