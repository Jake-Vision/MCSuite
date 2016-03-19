package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Note extends PacketEffect {

    public Note(EffectHolder effectHolder) {
        super(effectHolder, ParticleType.NOTE);
    }

    @Override
    public String getNmsName() {
        return "note";
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