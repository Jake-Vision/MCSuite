package xronbo.ronbolobby.effects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;


public class GeometryUtil {
    
    private static Random r;
    
    public static Random r() {
        if(r == null) r = new Random();
        return r;
    }

    public static float generateRandomFloat(float min, float max) {
        float f = min + (r().nextFloat() * ((1 + max) - min));
        return r().nextBoolean() ? f : -f;
    }

    public static float generateRandomFloat() {
        float f = r().nextFloat();
        return r().nextBoolean() ? f : -f;
    }

    public static List<Location> circle(Location loc, int r, int h, boolean hollow, boolean sphere, boolean includeAir) {
        List<Location> blocks = new ArrayList<Location>();
        int cx = loc.getBlockX(),
                cy = loc.getBlockY(),
                cz = loc.getBlockZ();
        for (int x = cx - r; x <= cx + r; x++)
            for (int z = cz - r; z <= cz + r; z++)
                for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        Location l = new Location(loc.getWorld(), x, y, z);
                        if (!includeAir && l.getBlock().getType() == Material.AIR) {
                            continue;
                        }
                        blocks.add(l);
                    }
                }
        return blocks;
    }

    public static List<Entity> getNearbyEntities(Location l, int range) {
        List<Entity> entities = new ArrayList<Entity>();
        for (Entity entity : l.getWorld().getEntities()) {
            if (isInBorder(l, entity.getLocation(), range)) {
                entities.add(entity);
            }
        }
        return entities;
    }

    public static boolean isInBorder(Location center, Location l, int range) {
        int x = center.getBlockX(), z = center.getBlockZ();
        int x1 = l.getBlockX(), z1 = l.getBlockZ();
        if (x1 >= (x + range) || z1 >= (z + range) || x1 <= (x - range) || z1 <= (z - range)) {
            return false;
        }
        return true;
    }
}