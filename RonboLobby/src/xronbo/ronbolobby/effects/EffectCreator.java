package xronbo.ronbolobby.effects;

import java.util.HashSet;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import xronbo.ronbolobby.effects.type.BlockBreak;
import xronbo.ronbolobby.effects.type.Critical;
import xronbo.ronbolobby.effects.type.Firework;
import xronbo.ronbolobby.effects.type.ItemSpray;
import xronbo.ronbolobby.effects.type.Potion;
import xronbo.ronbolobby.effects.type.Smoke;
import xronbo.ronbolobby.effects.type.Sound;


public class EffectCreator {

    private static EffectHolder createHolder(EffectHolder effectHolder, HashSet<ParticleDetails> particles) {
        HashSet<Effect> effects = new HashSet<Effect>();
        for (ParticleDetails pd : particles) {
            Effect effect = createEffect(effectHolder, pd.getParticleType(), pd.getDetails());
            if (effect != null) {
                effect.setAmount(pd.getAmmount());
                effects.add(effect);
            }
        }
        effectHolder.setEffects(effects);
        return effectHolder;
    }

    public static EffectHolder createLocHolder(HashSet<ParticleDetails> particles, Location location) {
        EffectHolder effectHolder = new EffectHolder(location);
        return createHolder(effectHolder, particles);
    }

    public static EffectHolder createPlayerHolder(HashSet<ParticleDetails> particles, Player player) {
        EffectHolderPlayer effectHolder = new EffectHolderPlayer(player);
        return createHolder(effectHolder, particles);
    }

    public static Effect createEffect(EffectHolder effectHolder, ParticleType particleType, Object[] o) {
        Effect e = null;

        if (particleType == ParticleType.BLOCKBREAK) {
            if (!checkArray(o, new Class[]{Integer.class, Integer.class})) {
                //Logger.log(Logger.LogLevel.WARNING, "Encountered Class Cast error initiating Trail effect (" + particleType.toString() + ").", true);
                return null;
            }
            e = new BlockBreak(effectHolder, (Integer) o[0], (Integer) o[1]);
        } else if (particleType == ParticleType.CRITICAL) {
            if (!checkArray(o, new Class[]{Critical.CriticalType.class})) {
                //Logger.log(Logger.LogLevel.WARNING, "Encountered Class Cast error initiating Trail effect (" + particleType.toString() + ").", true);
                return null;
            }
            e = new Critical(effectHolder, (Critical.CriticalType) o[0]);
        } else if (particleType == ParticleType.FIREWORK) {
            if (!checkArray(o, new Class[]{FireworkEffect.class})) {
                //Logger.log(Logger.LogLevel.WARNING, "Encountered Class Cast error initiating Trail effect (" + particleType.toString() + ").", true);
                return null;
            }
            e = new Firework(effectHolder, (FireworkEffect) o[0]);
        } else if (particleType == ParticleType.ITEMSPRAY) {
            if (!checkArray(o, new Class[]{Integer.class, Integer.class})) {
                //Logger.log(Logger.LogLevel.WARNING, "Encountered Class Cast error initiating Trail effect (" + particleType.toString() + ").", true);
                return null;
            }
            e = new ItemSpray(effectHolder, (Integer) o[0], (Integer) o[1]);
        }
        /*else if (particleType == ParticleType.NOTE) {
            if (!checkArray(o, new Class[] {Note.NoteType.class})) {
				Logger.log(Logger.LogLevel.WARNING, "Encountered Class Cast error initiating Particle effect (" + particleType.toString() + ").", true);
				return null;
			}
			e = particleType.getNoteInstance(effectHolder, (Note.NoteType) o[0]);
		}*/
        else if (particleType == ParticleType.POTION) {
            if (!checkArray(o, new Class[]{Potion.PotionType.class})) {
                //Logger.log(Logger.LogLevel.WARNING, "Encountered Class Cast error initiating Trail effect (" + particleType.toString() + ").", true);
                return null;
            }
            e = new Potion(effectHolder, (Potion.PotionType) o[0]);
        } else if (particleType == ParticleType.SMOKE) {
            if (!checkArray(o, new Class[]{Smoke.SmokeType.class})) {
                //Logger.log(Logger.LogLevel.WARNING, "Encountered Class Cast error initiating Trail effect (" + particleType.toString() + ").", true);
                return null;
            }
            e = new Smoke(effectHolder, (Smoke.SmokeType) o[0]);
        } else if (particleType == ParticleType.SOUND) {
            if (!checkArray(o, new Class[]{org.bukkit.Sound.class})) {
                //Logger.log(Logger.LogLevel.WARNING, "Encountered Class Cast error initiating Trail effect (" + particleType.toString() + ").", true);
                return null;
            }
            e = new Sound(effectHolder, (org.bukkit.Sound) o[0]);
        } else {
            e = particleType.getEffectInstance(effectHolder);
        }

        return e;
    }

    private static boolean checkArray(Object[] o1, @SuppressWarnings("rawtypes") Class[] classes) {
        for (int i = 0; i < o1.length; i++) {
            if (!(o1[i].getClass().equals(classes[i]))) {
                return false;
            }
        }
        return true;
    }
}