package xronbo.ronbolobby.effects.type;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import xronbo.ronbolobby.RonboLobby;
import xronbo.ronbolobby.effects.Effect;
import xronbo.ronbolobby.effects.EffectHolder;
import xronbo.ronbolobby.effects.ParticleType;

public class ItemSpray extends Effect {

    public static ArrayList<ItemSprayRemoveTask> TASKS = new ArrayList<ItemSprayRemoveTask>();
    public static ArrayList<UUID> UUID_LIST = new ArrayList<UUID>();

    public int idValue;
    public int metaValue;

    public ItemSpray(EffectHolder effectHolder, int idValue, int metaValue) {
        super(effectHolder, ParticleType.ITEMSPRAY);
        this.idValue = idValue;
        this.metaValue = metaValue;
    }

    @Override
    public void doPlay() {
        for (Location l : this.displayType.getLocations(this.getHolder())) {
            for (int i = 1; i <= 5; i++) {
                Item item = this.getHolder().getLocation().getWorld().dropItemNaturally(l, new ItemStack(this.idValue, 1, (short) this.metaValue));
                item.setVelocity(item.getVelocity().normalize().multiply(0.4F));
                item.setPickupDelay(Integer.MAX_VALUE);
                new ItemSprayRemoveTask(item);
            }
        }
    }

    @Override
    public void doPlayAmount(int i) {
        this.doPlay();
    }
    
//    public void playDemo(Player p) {
//        for (int i = 1; i <= 5; i++) {
//            Item item = this.getHolder().getLocation().getWorld().dropItemNaturally(new Location(p.getWorld(), p.getLocation().getX() + 0.5D, p.getLocation().getY(), p.getLocation().getZ() + 0.5D), new ItemStack(this.idValue, 1, (short) this.metaValue));
//            item.setPickupDelay(Integer.MAX_VALUE);
//            new ItemSprayRemoveTask(item);
//        }
//    }

    public class ItemSprayRemoveTask extends BukkitRunnable {

        private Item item;
        private UUID uuid;

        ItemSprayRemoveTask(Item item) {
            this.item = item;
            this.uuid = item.getUniqueId();
            TASKS.add(this);
            UUID_LIST.add(this.uuid);
            this.runTaskLater(RonboLobby.plugin, 50L);
        }

        @Override
        public void run() {
            this.executeFinish(true);
        }

        public void executeFinish(boolean removeFromLists) {
            if (this.item != null && !this.item.isDead()) {
                this.item.remove();
            }
            if (removeFromLists) {
                TASKS.remove(this);
                UUID_LIST.remove(this.uuid);
            }
        }

        @Override
        public synchronized void cancel() throws IllegalStateException {
            super.cancel();
        }
    }
}