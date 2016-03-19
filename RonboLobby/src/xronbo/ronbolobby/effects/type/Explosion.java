package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Explosion extends PacketEffect {

    public Explosion(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.EXPLOSION);
    }

    @Override
    public String getNmsName() {
        return "hugeexplosion";
    }

    @Override
    public float getSpeed() {
        return 1F;
    }

    @Override
    public int getParticleAmount() {
        return 1;
    }
}