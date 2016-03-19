package xronbo.ronbolobby.effects.type;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import xronbo.ronbolobby.effects.Effect;
import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.ParticleType;


public class Ender extends Effect {

    public Ender(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.ENDER);
    }

    @Override
    public void doPlay() {
        for (Location l : this.displayType.getLocations(this.getHolder())) {
            this.getHolder().getLocation().getWorld().playEffect(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()), org.bukkit.Effect.ENDER_SIGNAL, 0);
        }
    }

    public void playDemo(Player p) {
        p.playEffect(p.getLocation(), org.bukkit.Effect.ENDER_SIGNAL, 0);
    }

    @Override
    public void doPlayAmount(int i) {
        this.doPlay();
    }
}