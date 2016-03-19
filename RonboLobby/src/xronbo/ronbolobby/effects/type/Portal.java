package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Portal extends PacketEffect {

    public Portal(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.PORTAL);
    }

    @Override
    public String getNmsName() {
        return "portal";
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