package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Runes extends PacketEffect {

    public Runes(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.RUNES);
    }

    @Override
    public String getNmsName() {
        return "enchantmenttable";
    }

    @Override
    public float getSpeed() {
        return 1F;
    }

    @Override
    public int getParticleAmount() {
        return 100;
    }
}