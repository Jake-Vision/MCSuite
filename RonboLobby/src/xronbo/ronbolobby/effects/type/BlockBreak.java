package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class BlockBreak extends PacketEffect {

    public int idValue;
    public int metaValue;

    public BlockBreak(EffectHolder effectHolder, int idValue, int metaValue) {
        super(effectHolder, ParticleType.BLOCKBREAK);
        this.idValue = idValue;
        this.metaValue = metaValue;
    }

    @Override
    public String getNmsName() {
        return "blockcrack_" + idValue + "_" + metaValue;
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