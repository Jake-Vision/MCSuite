package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Magic extends PacketEffect {

    public Magic(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.MAGIC);
    }

    @Override
    public String getNmsName() {
        return "witchMagic";
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