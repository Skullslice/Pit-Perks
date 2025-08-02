package onEvent;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class EntityMoveMap implements Listener {
    public HashMap<UUID, Location> nearMap = new HashMap<>();

    @EventHandler
    public void onEntityMove(EntityMoveEvent moveEvent) {
        boolean isPlayer = moveEvent.getEntity() instanceof Player;
        UUID uuid = moveEvent.getEntity().getUniqueId();
        Location el = moveEvent.getEntity().getLocation();

        //go ahead and store all the entities in a map to later calculate distances.
        nearMap.put(uuid, el);
    }
}
