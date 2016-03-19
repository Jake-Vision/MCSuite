package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Snowball extends PacketEffect {

    public Snowball(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.SNOWBALL);
    }

    @Override
    public String getNmsName() {
        return "snowballpoof";
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