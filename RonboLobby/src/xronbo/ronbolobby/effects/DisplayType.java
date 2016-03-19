package xronbo.ronbolobby.effects;

import java.util.ArrayList;

import org.bukkit.Location;


public enum DisplayType {

    NORMAL,
    CIRCLE,
    BALL,
    DOUBLE,
    ABOVE,
    FEET;

    public ArrayList<Location> getLocations(EffectHolder holder) {
        Location l = holder.getLocation();
        ArrayList<Location> locations = new ArrayList<Location>();

        if (this == DisplayType.NORMAL) {
            locations.add(l);
        } else if (this == DisplayType.CIRCLE) {
            for (Location location : GeometryUtil.circle(l, 4, 1, true, false, true)) {
                locations.add(location);
            }
        } else if(this == DisplayType.BALL) {
            for (Location location : GeometryUtil.circle(l, 1, 1, true, true, true)) {
                locations.add(location);
            }
        } else if (this == DisplayType.DOUBLE) {
            locations.add(l);
            locations.add(new Location(l.getWorld(), l.getX(), l.getY() + 1.0D, l.getZ()));
        } else if (this == DisplayType.ABOVE) {
            locations.add(new Location(l.getWorld(), l.getX(), l.getY() + 2.0D, l.getZ()));
        }

        return locations;
    }
}