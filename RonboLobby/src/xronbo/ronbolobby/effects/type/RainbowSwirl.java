package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class RainbowSwirl extends PacketEffect {

    public RainbowSwirl(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.RAINBOWSWIRL);
    }

    @Override
    public String getNmsName() {
        return "mobSpell";
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