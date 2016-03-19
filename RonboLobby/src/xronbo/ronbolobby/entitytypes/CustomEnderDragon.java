package xronbo.ronbolobby.entitytypes;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R4.DamageSource;
import net.minecraft.server.v1_7_R4.EntityComplexPart;
import net.minecraft.server.v1_7_R4.EntityEnderDragon;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R4.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R4.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R4.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R4.World;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.util.UnsafeList;
import org.bukkit.entity.Player;

import xronbo.ronbolobby.RonboLobby;

public class CustomEnderDragon extends EntityEnderDragon {
	
	public boolean isPet;

	public String player = "";
	private boolean onGround = false;
	private final short[] counter = new short[3];
	private boolean directionChanged = false;
	boolean spout;
	boolean idk = false;
	byte upDown = 0;
	boolean ignoreSneaking = false;
	double fl = 0.0D;
	final double SPEED = 0.5;
	
	@Override
	public void e() {
		if(this.passenger == null) {
			if(!this.onGround) {
				double myY = this.locY - SPEED;
				int imyY = (int)myY;
				if(imyY < 1) {
				  this.onGround = true;
				}
				if((imyY < 1) || ((imyY < this.world.getWorld().getMaxHeight()) && (this.world.getWorld().getBlockAt((int)this.locX, imyY, (int)this.locZ).getType() != Material.AIR))) {
				  this.onGround = true;
				} else {
				  setPosition(this.locX, myY, this.locZ);
				}
			}
	      strangeCode();
	      return;
	    }
	    this.onGround = false;
	    Player p = RonboLobby.plugin.getServer().getPlayerExact(this.player);
	    if (p == null) {
	      return;
	    }
	    if (this.fl > 0.0D) {
	      if (this.counter[2] > 600) {
	        this.fl -= 0.1D;
	        this.counter[2] = 0;
	      } else {
	        int index = 2;
	        counter[index] = ((short)(counter[index] + 1));
	      }
	    } else {
	      this.counter[2] = 0;
	    }
	    double myX = this.locX;
	    double myY = this.locY;
	    double myZ = this.locZ;
	    if(!this.directionChanged) {
	        Location loc = p.getEyeLocation();
	        double oldYaw = this.yaw;
	        this.yaw = (loc.getYaw() + 180.0F);
	        while(this.yaw > 360.0F) {
	          this.yaw -= 360.0F;
	        }
	        while(this.yaw < 0.0F) {
	          this.yaw += 360.0F;
	        }
//	        if((this.yaw < 22.5F) || (this.yaw > 337.5D)) {
//	          this.yaw = 0.0F;
//	        } else if(this.yaw < 67.5F) {
//	          this.yaw = 45.0F;
//	        } else if(this.yaw < 112.5F) {
//	          this.yaw = 90.0F;
//	        } else if(this.yaw < 157.5F) {
//	          this.yaw = 135.0F;
//	        } else if(this.yaw < 202.5F) {
//	          this.yaw = 180.0F;
//	        } else if(this.yaw < 247.5F) {
//	          this.yaw = 225.0F;
//	        } else if(this.yaw < 292.5F) {
//	          this.yaw = 270.0F;
//	        } else {
//	          this.yaw = 315.0F;
//	        }
	        this.upDown = 0;
	        if(loc.getPitch() < -20.0F) {
	        	this.upDown = 1;
	        }
	        if(loc.getPitch() < -50.0F) {
	        	this.upDown = 2;
	        }
	        if(loc.getPitch() < -80.0F) {
	        	this.upDown = 3;
	        }
	        if(loc.getPitch() > 40.0F) {
	        	this.upDown = -1;
	        }
	        if(loc.getPitch() > 60.0F) {
	        	this.upDown = -2;
	        }
	        if(loc.getPitch() > 80.0F) {
	        	this.upDown = -3;
	        }
	        if((oldYaw != this.yaw) || (myY != this.locY)) {
	        	this.directionChanged = true;
	        }
	    }
	    if(this.directionChanged) {
	        if(this.counter[0] < 5) {
	          int index = 0;
	          counter[index] = ((short)(counter[index] + 1));
	        } else {
	          this.counter[0] = 0;
	          this.directionChanged = false;
	        }
	    }
	    double rideSpeed = SPEED + this.fl;
    	myY += rideSpeed * this.upDown;
    	double currentX = myX;
    	double currentZ = myZ;
	    if((this.yaw < 22.5F) || (this.yaw > 337.5F)) {
	    	myZ -= rideSpeed;
	    } else if(this.yaw < 67.5F) {
	    	double halfSpeed = rideSpeed / 2.0D;
	    	myZ -= halfSpeed;
	    	myX += halfSpeed;
	    } else if(this.yaw < 112.5F) {
	    	myX += rideSpeed;
	    } else if(this.yaw < 157.5F) {
	    	double halfSpeed = rideSpeed / 2.0D;
	    	myX += halfSpeed;
	    	myZ += halfSpeed;
	    } else if(this.yaw < 202.5F) {
	    	myZ += rideSpeed;
	    } else if(this.yaw < 247.5F) {
	    	double halfSpeed = rideSpeed / 2.0D;
	    	myZ += halfSpeed;
	    	myX -= halfSpeed;
	    } else if(this.yaw < 292.5D) {
	    	myX -= rideSpeed;
	    } else {
	    	double halfSpeed = rideSpeed / 2.0D;
	    	myX -= halfSpeed;
	    	myZ -= halfSpeed;
	    }
	    if(this.upDown == 3 || this.upDown == -3) {
	    	myX = currentX;
	    	myZ = currentZ;
	    }
	    setPosition(myX, myY, myZ);
	    strangeCode();
	}
	
	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		return false;
	}
	  
	@Override
	public boolean a(EntityComplexPart entitycomplexpart, DamageSource damagesource, float f) {
		return false;
	}
	
	public void strangeCode() {
	      this.aM = this.yaw;
	      this.bq.width = (this.bq.length = 3.0F);
	      this.bs.width = (this.bs.length = 2.0F);
	      this.bt.width = (this.bt.length = 2.0F);
	      this.bu.width = (this.bu.length = 2.0F);
	      this.br.length = 3.0F;
	      this.br.width = 5.0F;
	      this.bv.length = 2.0F;
	      this.bv.width = 4.0F;
	      this.bw.length = 3.0F;
	      this.bw.width = 4.0F;
	      float f1 = (float)(b(5, 1.0F)[1] - b(10, 1.0F)[1]) * 10.0F / 180.0F * 3.141593F;
	      float f2 = MathHelper.cos(f1);
	      float f9 = -MathHelper.sin(f1);
	      float f10 = this.yaw * 3.141593F / 180.0F;
	      float f11 = MathHelper.sin(f10);
	      float f12 = MathHelper.cos(f10);
	      this.br.h();
	      this.br.setPositionRotation(this.locX + f11 * 0.5F, this.locY, this.locZ - f12 * 0.5F, 0.0F, 0.0F);
	      this.bv.h();
	      this.bv.setPositionRotation(this.locX + f12 * 4.5F, this.locY + 2.0D, this.locZ + f11 * 4.5F, 0.0F, 0.0F);
	      this.bw.h();
	      this.bw.setPositionRotation(this.locX - f12 * 4.5F, this.locY + 2.0D, this.locZ - f11 * 4.5F, 0.0F, 0.0F);
	      double[] adouble = b(5, 1.0F);
	      double[] adouble1 = b(0, 1.0F);
	      
	      float f3 = MathHelper.sin(this.yaw * 3.141593F / 180.0F - this.bf * 0.01F);
	      float f13 = MathHelper.cos(this.yaw * 3.141593F / 180.0F - this.bf * 0.01F);
	      
	      this.bq.h();
	      this.bq.setPositionRotation(this.locX + f3 * 5.5F * f2, this.locY + (adouble1[1] - adouble[1]) * 1.0D + f9 * 5.5F, this.locZ - f13 * 5.5F * f2, 0.0F, 0.0F);
	      for (int j = 0; j < 3; j++)
	      {
	        EntityComplexPart entitycomplexpart = null;
	        if (j == 0) {
	          entitycomplexpart = this.bs;
	        }
	        if (j == 1) {
	          entitycomplexpart = this.bt;
	        }
	        if (j == 2) {
	          entitycomplexpart = this.bu;
	        }
	        double[] adouble2 = b(12 + j * 2, 1.0F);
	        float f14 = this.yaw * 3.141593F / 180.0F + (float)MathHelper.g(adouble2[0] - adouble[0]) * 3.141593F / 180.0F * 1.0F;
	        float f15 = MathHelper.sin(f14);
	        float f16 = MathHelper.cos(f14);
	        float f17 = 1.5F;
	        float f18 = (j + 1) * 2.0F;
	        
	        entitycomplexpart.h();
	        entitycomplexpart.setPositionRotation(this.locX - (f11 * f17 + f15 * f18) * f2, this.locY + (adouble2[1] - adouble[1]) * 1.0D - (f18 + f17) * f9 + 1.5D, this.locZ + (f12 * f17 + f16 * f18) * f2, 0.0F, 0.0F);
	      }
	      this.bA = false;
	}
	
	public CustomEnderDragon(World world, boolean isPet) {
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
	
	public CustomEnderDragon(World world) {
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
	    this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
	    this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
	}
	
}