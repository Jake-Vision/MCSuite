package xronbo.ronbolobby.effects;

//import xronbo.ronbomc.effects.type.Swirl;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;

import xronbo.ronbolobby.effects.type.Critical;
import xronbo.ronbolobby.effects.type.Potion;
import xronbo.ronbolobby.effects.type.Smoke;


public class ParticleDetails {

    private ParticleType particleType;
    private int amt;

    public Integer blockId = 1;
    public Integer blockMeta = 0;
    public Critical.CriticalType criticalType = Critical.CriticalType.NORMAL;
    public FireworkEffect fireworkEffect = FireworkEffect.builder().withColor(Color.WHITE).with(FireworkEffect.Type.BALL).withFade(Color.WHITE).build();
    //public Note.NoteType noteType = Note.NoteType.GREEN;
    public Potion.PotionType potionType = Potion.PotionType.AQUA;
    public Smoke.SmokeType smokeType = Smoke.SmokeType.BLACK;
//    public Swirl.SwirlType swirlType = Swirl.SwirlType.WHITE;
    public org.bukkit.Sound sound = Sound.ANVIL_BREAK;

    public ParticleDetails(ParticleType particleType) {
        this(particleType, 0);
    }

    public ParticleDetails(ParticleType particleType, int amt) {
        this.particleType = particleType;
        this.amt = amt;
    }
    
    public int getAmmount() {
        return this.amt;
    }

    public ParticleType getParticleType() {
        return this.particleType;
    }

    public Object[] getDetails() {
        Object[] o = new Object[]{};
        if (particleType == ParticleType.BLOCKBREAK || particleType == ParticleType.ITEMSPRAY) {
            return new Object[]{this.blockId, this.blockMeta};
        } else if (particleType == ParticleType.CRITICAL) {
            return new Object[]{this.criticalType};
        } else if (particleType == ParticleType.FIREWORK) {
            return new Object[]{this.fireworkEffect};
        }
        /*else if (particleType == ParticleType.NOTE) {
            return new Object[] {this.noteType};
		}*/
        else if (particleType == ParticleType.POTION) {
            return new Object[]{this.potionType};
        } else if (particleType == ParticleType.SMOKE) {
            return new Object[]{this.smokeType};
        }
//        else if (particleType == ParticleType.SWIRL) {
//            return new Object[]{this.swirlType, this.uuid};
//        }

        else if (particleType == ParticleType.SOUND) {
            return new Object[]{this.sound};
        }
        return o;
    }
}