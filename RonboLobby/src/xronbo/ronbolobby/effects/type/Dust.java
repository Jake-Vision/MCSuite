package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Dust extends PacketEffect {

    public int idValue;
    public int metaValue;

    public Dust(EffectHolder effectHolder, int idValue, int metaValue) {
        super(effectHolder, ParticleType.DUST);
        this.idValue = idValue;
        this.metaValue = metaValue;
    }

    @Override
    public String getNmsName() {
        return "blockdust_" + idValue + "_" + metaValue;
    }

    @Override
    public float getSpeed() {
        return 0F;
    }

    @Override
    public int getParticleAmount() {
        return 100;
    }
}