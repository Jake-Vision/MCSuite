package xronbo.ronbolobby.effects.type;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import xronbo.ronbolobby.effects.Effect;
import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.ParticleType;


public class Potion extends Effect {

    public PotionType potionType;

    public Potion(EffectHolder effectHolder, PotionType potionType) {
        super(effectHolder, ParticleType.POTION);
        this.potionType = potionType;
    }

    @Override
    public void doPlay() {
        for (Location l : this.displayType.getLocations(this.getHolder())) {
            this.getHolder().getLocation().getWorld().playEffect(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()), org.bukkit.Effect.POTION_BREAK, this.potionType.getValue());
        }
    }
    
    /*
     * never gets called
     */
    public void playDemo(Player p) {
        p.playEffect(new Location(p.getWorld(), p.getLocation().getX() + 0.5D, p.getLocation().getY(), p.getLocation().getZ() + 0.5D), org.bukkit.Effect.POTION_BREAK, PotionType.AQUA.getValue());
    }

    @Override
    public void doPlayAmount(int i) {
        this.doPlay();
    }
    
    /*
     * oh this is nice
     */
    public enum PotionType {
        AQUA(2),
        BLACK(8),
        BLUE(0),
        CRIMSON(5),
        DARKBLUE(6),
        DARKGREEN(4),
        DARKRED(12),
        GOLD(3),
        GRAY(10),
        GREEN(20),
        PINK(1),
        RED(9);

        private int value;

        PotionType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}