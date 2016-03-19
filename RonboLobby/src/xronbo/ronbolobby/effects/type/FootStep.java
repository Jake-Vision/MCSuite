package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;

public class FootStep extends PacketEffect {

    public FootStep(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.FOOTSTEP);
    }

    @Override
    public String getNmsName() {
        return "footstep";
    }

    @Override
    public float getSpeed() {
        return 0;
    }

    @Override
    public int getParticleAmount() {
        return 5;
    }
}