package xronbo.ronbolobby.effects;

public abstract class Effect {
    
    private EffectHolder holder;
    protected DisplayType displayType;
    protected ParticleType particleType;
    private int amt;

    public Effect(EffectHolder holder, ParticleType particleType) {
        this.holder = holder;
        this.particleType = particleType;

        this.displayType = this.particleType.getDisplayType();
        if (this.displayType == null) {
            this.displayType = DisplayType.NORMAL;
        }
    }
    
    public void setAmount(int i) {
        this.amt = i;
    }
    
    public int getAmount() {
        return this.amt;
    }
    
    public EffectHolder getHolder() {
        return this.holder;
    }
    
    public void play() {
        if(this.amt == 0) this.doPlay();
        else this.doPlayAmount(this.amt);
    }
    
    protected abstract void doPlayAmount(int i);

    protected abstract void doPlay();

//    public abstract void playDemo(Player p);

    public ParticleType getParticleType() {
        return particleType;
    }
}