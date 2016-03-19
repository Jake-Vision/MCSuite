package xronbo.ronbolobby.effects.type;

import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.PacketEffect;
import xronbo.ronbolobby.effects.ParticleType;


public class Smoke extends PacketEffect {

    public SmokeType smokeType;

    public Smoke(EffectHolder effectHolder, SmokeType smokeType) {
        super(effectHolder, ParticleType.SMOKE);
        this.smokeType = smokeType;
    }

    @Override
    public String getNmsName() {
        return this.smokeType.getNmsName();
    }

    @Override
    public float getSpeed() {
        return this.smokeType.getSpeed();
    }

    @Override
    public int getParticleAmount() {
        return this.smokeType.getAmount();
    }

    public enum SmokeType {
        BLACK("largesmoke", 0.2F, 50),
        RED("reddust", 0F, 100),
        RAINBOW("reddust", 1F, 100);

        private String nmsName;
        private float speed;
        private int amount;

        SmokeType(String nmsName, float speed, int amount) {
            this.nmsName = nmsName;
            this.speed = speed;
            this.amount = amount;
        }

        public String getNmsName() {
            return this.nmsName;
        }

        public float getSpeed() {
            return this.speed;
        }

        public int getAmount() {
            return this.amount;
        }
    }
}