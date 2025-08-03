package onEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class onLuckyDiamondPerk implements Listener {

    HashMap<UUID, Set<ItemStack>> droppedItems = new HashMap<>();

    //
    @EventHandler
    public void onLootArmor(EntityPickupItemEvent pickup) {
        if (!(pickup instanceof Player)) return;
        //bind the dropped items to a <uuid, set> map
    }

    public void onEntityDeath(EntityDeathEvent death) {
        
    }
}
