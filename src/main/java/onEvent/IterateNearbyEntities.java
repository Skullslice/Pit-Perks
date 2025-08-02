package onEvent;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class IterateNearbyEntities {

    public static float getDamageReduction(Location playerLocation, HashMap<UUID, Location> nearMap) {
        float nearbyEntities = 0;

        for (Location otherLocation : nearMap.values()) {
            if (otherLocation == null || !otherLocation.getWorld().equals(playerLocation.getWorld())) continue;

            double distanceSquared = otherLocation.distanceSquared(playerLocation);
            if (distanceSquared <= 144) { // 12-block radius squared
                nearbyEntities++;
            }
        }

        // Reduce damage by 3% per nearby entity, capped at 39% reduction
        return Math.min(0.61f, 1.0f - (nearbyEntities * 3.0f / 100.0f));
    }


}
