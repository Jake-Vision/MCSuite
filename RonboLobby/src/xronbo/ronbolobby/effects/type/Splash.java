package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Splash extends PacketEffect {

    public Splash(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.SPLASH);
    }

    @Override
    public String getNmsName() {
        return "splash";
    }

    @Override
    public float getSpeed() {
        return 1F;
    }

    @Override
    public int getParticleAmount() {
        return 50;
    }
}