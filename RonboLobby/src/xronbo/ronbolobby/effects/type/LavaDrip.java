package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class LavaDrip extends PacketEffect {

    public LavaDrip(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.LAVADRIP);
    }

    @Override
    public String getNmsName() {
        return "dripLava";
    }

    @Override
    public float getSpeed() {
        return 0F;
    }

    @Override
    public int getParticleAmount() {
        return 50;
    }
}