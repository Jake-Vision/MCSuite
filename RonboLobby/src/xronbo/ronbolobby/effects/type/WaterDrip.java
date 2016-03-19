package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class WaterDrip extends PacketEffect {

    public WaterDrip(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.WATERDRIP);
    }

    @Override
    public String getNmsName() {
        return "dripWater";
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