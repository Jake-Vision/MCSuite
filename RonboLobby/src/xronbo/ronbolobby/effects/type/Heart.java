package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Heart extends PacketEffect {

    public Heart(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.HEART);
    }

    @Override
    public String getNmsName() {
        return "heart";
    }

    @Override
    public float getSpeed() {
        return 0F;
    }

    @Override
    public int getParticleAmount() {
        return 10;
    }
}