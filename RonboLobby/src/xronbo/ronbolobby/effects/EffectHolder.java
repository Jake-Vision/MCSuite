package xronbo.ronbolobby.effects;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Location;

import xronbo.ronbolobby.effects.type.BlockBreak;
import xronbo.ronbolobby.effects.type.Critical;
import xronbo.ronbolobby.effects.type.ItemSpray;
import xronbo.ronbolobby.effects.type.Potion;
import xronbo.ronbolobby.effects.type.Smoke;

public class EffectHolder {

    private HashSet<Effect> effects = new HashSet<>();
    private Location loc;
    
    //particle motion (redstone dust color)
    public float motX;
    public float motY;
    public float motZ;
    
    private int ticks;
    
    private boolean doParticleFrequency;
    
    private boolean running;

    protected EffectHolder(Location loc) {
        this.loc = loc;
    }
    
    public boolean isRunning() {
        return this.running;
    }
    
    public  void setRunning(boolean run) {
        this.running = run;
    }

    public void setEffects(HashSet<Effect> effects) {
        for (Effect e : effects) {
            this.effects.add(e);
        }
    }
    
    public void setDoParticleFrequency(boolean doIt) {
        this.doParticleFrequency = doIt;
    }

    public HashSet<Effect> getEffects() {
        return this.effects;
    }

    public boolean addEffect(ParticleType particleType) {
        ParticleDetails pd = new ParticleDetails(particleType);
        Effect effect = EffectCreator.createEffect(this, particleType, pd.getDetails());
        if (effect != null) {
            this.effects.add(effect);
        }
        return true;
    }

    public boolean addEffect(ParticleDetails particleDetails) {
        Effect effect = EffectCreator.createEffect(this, particleDetails.getParticleType(), particleDetails.getDetails());
        if (effect != null) {
            this.effects.add(effect);
        }
        return true;
    }

    public boolean hasEffect(ParticleType pt) {
        for (Effect e : effects) {
            if (e.getParticleType().equals(pt)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEffect(ParticleDetails pd) {
        for (Effect e : effects) {
            if (e.getParticleType().equals(pd.getParticleType())) {
                ParticleType pt = pd.getParticleType();
                if (pt == ParticleType.BLOCKBREAK) {
                    if (pd.blockId == ((BlockBreak) e).idValue && pd.blockMeta == ((BlockBreak) e).metaValue) {
                        return true;
                    }
                } else if (pt == ParticleType.CRITICAL) {
                    if (pd.criticalType.equals(((Critical) e).criticalType)) {
                        return true;
                    }
                } else if (pt == ParticleType.ITEMSPRAY) {
                    if (pd.blockId == ((ItemSpray) e).idValue && pd.blockMeta == ((ItemSpray) e).metaValue) {
                        return true;
                    }
                } else if (pt == ParticleType.FIREWORK) {
                    return true;
                } else if (pt == ParticleType.POTION) {
                    if (pd.potionType.equals(((Potion) e).potionType)) {
                        return true;
                    }
                } else if (pt == ParticleType.SMOKE) {
                    if (pd.smokeType.equals(((Smoke) e).smokeType)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public void removeEffect(ParticleType particleType) {
        Iterator<Effect> i = this.effects.iterator();
        while (i.hasNext()) {
            Effect e = i.next();
            if (e.getParticleType().equals(particleType)) {
                i.remove();
            }
        }
    }

    public void removeEffect(ParticleDetails particleDetails) {
        Iterator<Effect> i = this.effects.iterator();
        while (i.hasNext()) {
            Effect e = i.next();
            if (e.getParticleType().equals(particleDetails.getParticleType())) {
                ParticleType pt = e.getParticleType();
                if (pt == ParticleType.BLOCKBREAK) {
                    if (particleDetails.blockId == ((BlockBreak) e).idValue && particleDetails.blockMeta == ((BlockBreak) e).metaValue) {
                        i.remove();
                    }
                } else if (pt == ParticleType.ITEMSPRAY) {
                    if (particleDetails.blockId == ((ItemSpray) e).idValue && particleDetails.blockMeta == ((ItemSpray) e).metaValue) {
                        i.remove();
                    }
                } else if (pt == ParticleType.CRITICAL) {
                    if (particleDetails.criticalType.equals(((Critical) e).criticalType)) {
                        i.remove();
                    }
                } else if (pt == ParticleType.FIREWORK) {
                    i.remove();
                }
                /*else if (pt == ParticleType.NOTE) {
                    if (particleDetails.noteType.equals(((Note) e).noteType)) {
						i.remove();
					}
				}*/
                else if (pt == ParticleType.POTION) {
                    if (particleDetails.potionType.equals(((Potion) e).potionType)) {
                        i.remove();
                    }
                } else if (pt == ParticleType.SMOKE) {
                    if (particleDetails.smokeType.equals(((Smoke) e).smokeType)) {
                        i.remove();
                    }
                } else {
                    i.remove();
                }
            }
        }
    }

    public Location getLocation() {
        return this.loc;
    }

    public void setLocation(Location l) {
        this.loc = l;
    }

    public void update() {
        this.ticks++;
        if(this.isRunning()) {
            Iterator<Effect> iter = this.effects.iterator();
            while (iter.hasNext()) {
                Effect e = iter.next();
                if (!this.doParticleFrequency || ticks % e.particleType.getFrequency() == 0) {
                    e.play();
                }
            }
        }
    }
}