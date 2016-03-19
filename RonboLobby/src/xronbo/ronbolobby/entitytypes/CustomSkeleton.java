package xronbo.ronbolobby.entitytypes;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R4.Block;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityArrow;
import net.minecraft.server.v1_7_R4.EntityEgg;
import net.minecraft.server.v1_7_R4.EntityFireball;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.EntityProjectile;
import net.minecraft.server.v1_7_R4.EntitySkeleton;
import net.minecraft.server.v1_7_R4.EntitySmallFireball;
import net.minecraft.server.v1_7_R4.EntitySnowball;
import net.minecraft.server.v1_7_R4.EntityWitherSkull;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.Items;
import net.minecraft.server.v1_7_R4.Material;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PathfinderGoalArrowAttack;
import net.minecraft.server.v1_7_R4.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R4.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R4.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R4.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_7_R4.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R4.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R4.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R4.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R4.World;

import org.bukkit.craftbukkit.v1_7_R4.util.UnsafeList;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;


public class CustomSkeleton extends EntitySkeleton {
	
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

	public CustomSkeleton(World world, boolean isPet) {
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
    
	public CustomSkeleton(World world) {
		super(world);
        if (world != null && !world.isStatic) {
            setFightStyle();
        }
	}
	
	@Override
	public void a(EntityLiving entityliving, float f) {
		ItemStack itemstack = getEquipment(0);
	    if(itemstack.getItem() == Items.BOW) {
			EntityArrow entityarrow = new EntityArrow(this.world, this, entityliving, 1.6F, 14 - this.world.difficulty.a() * 4);
		    entityarrow.b(f * 2.0F + this.random.nextGaussian() * 0.25D + this.world.difficulty.a() * 0.11F);
		    makeSound("random.bow", 1.0F, 1.0F / (aI().nextFloat() * 0.4F + 0.8F));
		    this.world.addEntity(entityarrow, SpawnReason.CUSTOM);
	    } else if (itemstack.getItem() == Items.WOOD_HOE || itemstack.getItem() == Items.STONE_HOE) {
	    	EntityProjectile entity = null;
	    	if(itemstack.getItem() == Items.WOOD_HOE) {
	    		entity = new EntityEgg(this.world, this);
	    	} else {
	    		entity = new EntitySnowball(this.world, this);
	    	}
	        double d0 = entityliving.locX + entityliving.motX - this.locX;
	        double d1 = entityliving.locY + entityliving.motY - this.locY;
	        double d2 = entityliving.locZ + entityliving.motZ - this.locZ;
	        float f1 = MathHelper.sqrt(d0 * d0 + d2 * d2);
	        entity.shoot(d0, d1 + f1 * 0.2F, d2, 0.75F, 8.0F);
	        this.world.addEntity(entity, SpawnReason.CUSTOM);
	    } else if (itemstack.getItem() == Items.IRON_HOE || itemstack.getItem() == Items.GOLD_HOE  || itemstack.getItem() == Items.DIAMOND_HOE) {
	    	EntityFireball entity = null;
	    	double d0 = entityliving.locX - this.locX;
   	     	double d1 = entityliving.boundingBox.b + entityliving.length / 2.0F - (this.locY + this.length / 2.0F);
   	     	double d2 = entityliving.locZ - this.locZ;
   	     	float f1 = MathHelper.c(f) * 0.5F;
	    	if(itemstack.getItem() == Items.IRON_HOE || itemstack.getItem() == Items.GOLD_HOE) { //can't use large fireball b/c it can be deflected
	    		 entity = new EntitySmallFireball(this.world, this, d0 + this.random.nextGaussian() * f1, d1, d2 + this.random.nextGaussian() * f1);
	             entity.locY = (this.locY + this.length / 2.0F + 0.5D);
	    	} else {
	    		entity = new EntityWitherSkull(this.world, this, d0 + this.random.nextGaussian() * f1, d1, d2 + this.random.nextGaussian() * f1);
	            entity.locY = (this.locY + this.length / 2.0F + 0.5D);
	    	}
            this.world.addEntity(entity, SpawnReason.CUSTOM);
	    }
	}
	
	private PathfinderGoalArrowAttack rangedLong = new PathfinderGoalArrowAttack(this, 1.2D, 20, 60, 30.0F);
	private PathfinderGoalArrowAttack rangedShort = new PathfinderGoalArrowAttack(this, 1.2D, 20, 60, 8.0F);
	private PathfinderGoalMeleeAttack melee = new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.45D, false);
	  
	public void setFightStyle() {
		try {
			Field gsa = PathfinderGoalSelector.class.getDeclaredField("b");
			gsa.setAccessible(true);
			gsa.set(this.goalSelector, new UnsafeList<Object>());
			gsa.set(this.targetSelector, new UnsafeList<Object>());
		} catch (Exception e) {
			e.printStackTrace();
		}
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));
	    ItemStack itemstack = getEquipment(0);
	    if (itemstack != null) {
	    	if(itemstack.getItem() == Items.BOW) {
	  	      this.goalSelector.a(4, this.rangedLong);
	    	} else if (itemstack.getItem() == Items.WOOD_HOE || itemstack.getItem() == Items.STONE_HOE) {
	    		this.goalSelector.a(4, this.rangedShort);
	    	} else if (itemstack.getItem() == Items.IRON_HOE || itemstack.getItem() == Items.GOLD_HOE  || itemstack.getItem() == Items.DIAMOND_HOE) {
	    		this.goalSelector.a(4, this.rangedLong);
		    } else {
	  	      this.goalSelector.a(4, this.melee);
		    }
	    } else {
	    	this.goalSelector.a(4, this.melee);
	    }
	}
	
	@Override
	public boolean n(Entity entity) {
		boolean r = super.n(entity);
        if (this.getSkeletonType() == 1 && entity instanceof EntityLiving) {
        	if(entity.getBukkitEntity() instanceof Player) {
        		((Player)(entity.getBukkitEntity())).removePotionEffect(PotionEffectType.WITHER);
        	}
        }
		return r;
	}
	
	public void makeWither() {
		setSkeletonType(1);
	}
	
	@Override
	protected String t() {
		return "";
	}
	
	@Override
	protected String aT() {
		return "game.player.hurt";
	}
	
	@Override
	protected String aU() {
		return "mob.villager.death";
	}
	
	@Override
	protected void a(int i, int j, int k, Block block) {
	    //makeSound("mob.skeleton.step", 0.15F, 1.0F);
	}
	
}