package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Cloud extends PacketEffect {

    public Cloud(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.CLOUD);
    }

    @Override
    public String getNmsName() {
        return "explode";
    }

    @Override
    public float getSpeed() {
        return 0.1F;
    }

    @Override
    public int getParticleAmount() {
        return 50;
    }
}