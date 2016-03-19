package xronbo.ronbolobby.entitytypes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntitySilverfish;
import net.minecraft.server.v1_7_R4.Material;
import net.minecraft.server.v1_7_R4.World;

public class CustomSilverfish extends EntitySilverfish {
	
	public CustomSilverfish(World world) {
		super(world);
	}

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
	
}