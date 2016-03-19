package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Fire extends PacketEffect {

    public Fire(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.FIRE);
    }

    @Override
    public String getNmsName() {
        return "flame";
    }

    @Override
    public float getSpeed() {
        return 0.05F;
    }

    @Override
    public int getParticleAmount() {
        return 50;
    }
}