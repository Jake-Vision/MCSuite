package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Ember extends PacketEffect {

    public Ember(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.EMBER);
    }

    @Override
    public String getNmsName() {
        return "lava";
    }

    @Override
    public float getSpeed() {
        return 0F;
    }

    @Override
    public int getParticleAmount() {
        return 25;
    }
}