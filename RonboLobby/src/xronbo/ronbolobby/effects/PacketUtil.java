package xronbo.ronbolobby.effects;

import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles;

import org.bukkit.Location;

/**
 *
 * @author ralitski
 */
public class PacketUtil {
    
    public static PacketPlayOutWorldParticles createPacket(PacketEffect e) {
        return createPacket(e.getNmsName(),
                e.getHolder().getLocation(),
                e.getSpeed(),
                e.getParticleAmount());
    }
    
    public static PacketPlayOutWorldParticles createPacket(PacketEffect e, int amt) {
        return createPacket(e.getNmsName(),
                e.getHolder().getLocation(),
                e.getSpeed(),
                amt);
    }

    public static PacketPlayOutWorldParticles createPacket(String name, Location loc, float speed, int amt) {
        return createPacket(name, loc, 0.5F, 1.0F, 0.5F, speed, amt);
    }

    public static PacketPlayOutWorldParticles createPacket(String name, Location loc, float x, float y, float z, float speed, int amt) {
        return new PacketPlayOutWorldParticles(
                name,
                (float)loc.getX(),
                (float)loc.getY(),
                (float)loc.getZ(),
                x,
                y,
                z,
                speed,
                amt);
    }
}
