package xronbo.ronbolobby.effects.type;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import xronbo.ronbolobby.effects.Effect;
import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.ParticleType;

public class Sound extends Effect {

    private org.bukkit.Sound sound;

    public Sound(EffectHolder effectHolder, org.bukkit.Sound sound) {
        super(effectHolder, ParticleType.SOUND);
        this.sound = sound;
    }
    
    public void playDemo(Player p) {
        p.playSound(this.getHolder().getLocation(), this.sound, 10.0F, 1.0F);
    }

    @Override
    public void doPlay() {
        this.broadcast(this.getHolder().getLocation());
    }

    public void broadcast(Location l) {
        this.getHolder().getLocation().getWorld().playSound(l, this.sound, 10.0F, 1.0F);
    }

    @Override
    public void doPlayAmount(int i) {
        this.doPlay();
    }
}