package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Snow extends PacketEffect {

    public Snow(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.SNOW);
    }

    @Override
    public String getNmsName() {
        return "snowshovel";
    }

    @Override
    public float getSpeed() {
        return 0.02F;
    }

    @Override
    public int getParticleAmount() {
        return 50;
    }
}