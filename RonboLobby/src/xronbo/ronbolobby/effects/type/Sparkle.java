package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Sparkle extends PacketEffect {

    public Sparkle(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.SPARKLE);
    }

    @Override
    public String getNmsName() {
        return "happyVillager";
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