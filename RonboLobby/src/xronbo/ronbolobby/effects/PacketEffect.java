package xronbo.ronbolobby.effects;

import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public abstract class PacketEffect extends Effect {

    public PacketEffect(EffectHolder effectHolder, ParticleType particleType) {
        super(effectHolder, particleType);
    }

    public PacketPlayOutWorldParticles createPacket() {
        return PacketUtil.createPacket(this);
    }

    public PacketPlayOutWorldParticles createPacket(int amt) {
        return PacketUtil.createPacket(this, amt);
    }

    public abstract String getNmsName();

    public abstract float getSpeed();

    public abstract int getParticleAmount();

    @Override
    public void doPlay() {
        for (Location l : this.displayType.getLocations(this.getHolder())) {
            ReflectionUtil.sendPacket(l, this.createPacket());
        }
    }

    @Override
    public void doPlayAmount(int i) {
        for (Location l : this.displayType.getLocations(this.getHolder())) {
            ReflectionUtil.sendPacket(l, this.createPacket(i));
        }
    }
    
    /*
     * plays to a single player. this will be useful
     */
    public void playTo(Player p) {
        ReflectionUtil.sendPacket(p, this.createPacket());
    }
}