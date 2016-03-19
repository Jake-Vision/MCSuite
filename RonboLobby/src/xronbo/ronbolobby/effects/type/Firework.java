package xronbo.ronbolobby.effects.type;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import xronbo.ronbolobby.effects.Effect;
import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketUtil;
import xronbo.ronbolobby.effects.ParticleType;
import xronbo.ronbolobby.effects.ReflectionUtil;


public class Firework extends Effect {

    public FireworkEffect fireworkEffect;

    public Firework(EffectHolder effectHolder, FireworkEffect fireworkEffect) {
        super(effectHolder, ParticleType.FIREWORK);
        this.fireworkEffect = fireworkEffect;
    }
    
    public void doPlay() {
        for (Location l : this.displayType.getLocations(this.getHolder())) {
            ReflectionUtil.spawnFirework(new Location(l.getWorld(), l.getX(), l.getY() + 1, l.getZ()), this.fireworkEffect);
        }
    }

    public void playDemo(Player p) {
        ReflectionUtil.sendPacket(p, PacketUtil.createPacket("fireworksSpark", p.getLocation(), 50F, 30));
    }

    @Override
    public void doPlayAmount(int i) {
        this.doPlay();
    }
}