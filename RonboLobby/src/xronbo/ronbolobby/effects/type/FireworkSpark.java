package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class FireworkSpark extends PacketEffect {

    public FireworkSpark(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.FIREWORKSPARK);
    }

    @Override
    public String getNmsName() {
        return "fireworksSpark";
    }

    @Override
    public float getSpeed() {
        return 1F;
    }

    @Override
    public int getParticleAmount() {
        return 5;
    }
}