package xronbo.ronbolobby.effects;

import org.bukkit.Material;

import xronbo.ronbolobby.RonboLobby;
import xronbo.ronbolobby.effects.type.BlockBreak;
import xronbo.ronbolobby.effects.type.Cloud;
import xronbo.ronbolobby.effects.type.Cookie;
import xronbo.ronbolobby.effects.type.Critical;
import xronbo.ronbolobby.effects.type.Dust;
import xronbo.ronbolobby.effects.type.Ember;
import xronbo.ronbolobby.effects.type.Ender;
import xronbo.ronbolobby.effects.type.Explosion;
import xronbo.ronbolobby.effects.type.Fire;
import xronbo.ronbolobby.effects.type.Firework;
import xronbo.ronbolobby.effects.type.FireworkSpark;
import xronbo.ronbolobby.effects.type.FootStep;
import xronbo.ronbolobby.effects.type.Heart;
import xronbo.ronbolobby.effects.type.ItemSpray;
import xronbo.ronbolobby.effects.type.LavaDrip;
import xronbo.ronbolobby.effects.type.MCVoid;
import xronbo.ronbolobby.effects.type.Magic;
import xronbo.ronbolobby.effects.type.Note;
import xronbo.ronbolobby.effects.type.Portal;
import xronbo.ronbolobby.effects.type.Potion;
import xronbo.ronbolobby.effects.type.RainbowSwirl;
import xronbo.ronbolobby.effects.type.Runes;
import xronbo.ronbolobby.effects.type.Slime;
import xronbo.ronbolobby.effects.type.Smoke;
import xronbo.ronbolobby.effects.type.Snow;
import xronbo.ronbolobby.effects.type.Snowball;
import xronbo.ronbolobby.effects.type.Sound;
import xronbo.ronbolobby.effects.type.Sparkle;
import xronbo.ronbolobby.effects.type.Splash;
import xronbo.ronbolobby.effects.type.WaterDrip;


public enum ParticleType {
    BLOCKBREAK(BlockBreak.class, 20, Material.IRON_PICKAXE, (short) 0, "Block Break", true, true),
    CLOUD(Cloud.class, 20, Material.getMaterial(351), (short) 15, "Cloud", false, true),
    COOKIE(Cookie.class, 20, Material.COOKIE, (short) 0, "Cookie", false, true),
    CRITICAL(Critical.class, 20, Material.IRON_SWORD, (short) 0, "Critical", true, true),
    DUST(Dust.class, 20, Material.SULPHUR, (short) 0, "Dust", true, true),
    EMBER(Ember.class, 20, Material.TORCH, (short) 0, "Ember", false, true),
    ENDER(Ender.class, 20, Material.EYE_OF_ENDER, (short) 0, "Ender", false, true),
    EXPLOSION(Explosion.class, 20, Material.TNT, (short) 0, "Explosion", false, true),
    ITEMSPRAY(ItemSpray.class, 20, Material.DIAMOND, (short) 0, "ItemSpray", true, true),
    FIRE(Fire.class, 20, Material.FIRE, (short) 0, "Fire", false, true),
    FIREWORK(Firework.class, 20, Material.FIREWORK, (short) 0, "Firework", true, true),
    FIREWORKSPARK(FireworkSpark.class, 20, Material.FIREWORK, (short) 0, "FireworkSpark", false, true),
    FOOTSTEP(FootStep.class, 20, Material.CHAINMAIL_BOOTS, (short) 0, "FootStep", false, true),
    HEART(Heart.class, 20, Material.NAME_TAG, (short) 0, "Heart", false, true),
    LAVADRIP(LavaDrip.class, 20, Material.LAVA, (short) 0, "Lava Drip", false, true),
    MAGIC(Magic.class, 20, Material.ENCHANTED_BOOK, (short) 0, "Magic", false, true),
    NOTE(Note.class, 20, Material.getMaterial(2259), (short) 0, "Rainbow Note", false, true),
    PORTAL(Portal.class, 20, Material.PORTAL, (short) 0, "Portal", false, true),
    POTION(Potion.class, 20, Material.getMaterial(373), (short) 8193, "Potion", true, true),
    RAINBOWSWIRL(RainbowSwirl.class, 20, Material.getMaterial(373), (short) 16385, "Rainbow Swirl", false, true),
    RUNES(Runes.class, 20, Material.ENCHANTMENT_TABLE, (short) 0, "Runes", false, true),
    SLIME(Slime.class, 20, Material.SLIME_BALL, (short) 0, "Slime", false, true),
    SMOKE(Smoke.class, 20, Material.COAL, (short) 0, "Smoke", true, true),
    SNOW(Snow.class, 20, Material.SNOW, (short) 0, "Snow", false, true),
    SNOWBALL(Snowball.class, 20, Material.SNOW_BALL, (short) 0, "Snowball", false, true),
    SPARKLE(Sparkle.class, 20, Material.EMERALD, (short) 0, "Sparkle", false, true),
    SPLASH(Splash.class, 20, Material.WATER, (short) 0, "Splash", false, true),
    //SWIRL(Swirl.class, 20, Material.BEACON, (short) 0, "Swirl", true, true),
    MCVOID(MCVoid.class, 20, Material.ENDER_PORTAL, (short) 0, "Void", false, true),
    WATERDRIP(WaterDrip.class, 20, Material.WATER_LILY, (short) 0, "Water Drip", false, true),

    SOUND(Sound.class, 20, Material.JUKEBOX, (short) 0, "Sound", false, false);

    private Class<? extends Effect> effectClass;
    private int frequency;
    
	@SuppressWarnings("unused")
	private Material material;
    
    @SuppressWarnings("unused")
	private short data;
    private String name;
    private boolean requiresDataMenu;
    private boolean includeInMenu;

    ParticleType(Class<? extends Effect> effectClass, int frequency, Material material, short data, String name, boolean requiresDataMenu, boolean includeInMenu) {
        this.effectClass = effectClass;
        this.frequency = frequency;
        this.material = material;
        this.data = data;
        this.name = name;
        this.requiresDataMenu = requiresDataMenu;
        this.includeInMenu = includeInMenu;
    }

    public int getFrequency() {
        return RonboLobby.plugin.getConfig().getInt("effects." + this.toString().toLowerCase() + ".frequency", this.frequency);
    }

    public boolean requiresDataMenu() {
        return this.requiresDataMenu;
    }

    public boolean includeInMenu() {
        return this.includeInMenu;
    }

    public DisplayType getDisplayType() {
        String s = RonboLobby.plugin.getConfig().getString("effects." + this.toString().toLowerCase() + ".playType", "normal");
        if (EnumUtil.isEnumType(DisplayType.class, s.toUpperCase())) {
            DisplayType dt = DisplayType.valueOf(s.toUpperCase());
            if (dt != null) {
                return dt;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public Effect getEffectInstance(EffectHolder effectHolder) {
        Effect effect = null;
        try {
            Object o = this.effectClass.getConstructor(EffectHolder.class).newInstance(effectHolder);
            if (o instanceof Effect) {
                effect = (Effect) o;
            }
        } catch (Exception e) {
            //Logger.log(Logger.LogLevel.SEVERE, "Failed to create new Effect instance [" + this.toString() + "].", e, true);
        }
        return effect;
    }
}