package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Slime extends PacketEffect {

    public Slime(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.SLIME);
    }

    @Override
    public String getNmsName() {
        return "slime";
    }

    @Override
    public float getSpeed() {
        return 1F;
    }

    @Override
    public int getParticleAmount() {
        return 20;
    }
}