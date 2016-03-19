package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Cookie extends PacketEffect {

    public Cookie(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.COOKIE);
    }

    @Override
    public String getNmsName() {
        return "angryVillager";
    }

    @Override
    public float getSpeed() {
        return 0F;
    }

    @Override
    public int getParticleAmount() {
        return 15;
    }
}