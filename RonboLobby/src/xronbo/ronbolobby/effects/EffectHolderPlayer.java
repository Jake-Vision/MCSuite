package xronbo.ronbolobby.effects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import xronbo.ronbolobby.RonboLobby;

/**
 *
 * @author ralitski
 */
public class EffectHolderPlayer extends EffectHolder implements Listener {
    
    private String user;
    private Player player;
    
    protected EffectHolderPlayer(Player p) {
        super(p.getLocation());
        this.player = p;
        this.user = p.getName();
        Bukkit.getPluginManager().registerEvents(this, RonboLobby.plugin);
    }

    @Override
    public Location getLocation() {
        if(this.player == null) return null;
        return this.player.getLocation();
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(this.player == null) {
            Player p = event.getPlayer();
            if(p.getName().equals(this.user)) {
                this.player = p;
                this.setRunning(true);
            }
        }
    }
    
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if(this.player == null) {
            Player p = event.getPlayer();
            if(p.getName().equals(this.user)) {
                this.player = null;
                this.setRunning(false);
            }
        }
    }
}
