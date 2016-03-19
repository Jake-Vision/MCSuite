package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class MCVoid extends PacketEffect {

    public MCVoid(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.MCVOID);
    }

    @Override
    public String getNmsName() {
        return "townaura";
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