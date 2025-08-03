package onEvent;

import Serialization.PerkManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemDropManager implements Listener {

    private final Map<UUID, Long> dropAttempts = new HashMap<>();
    private final long DROP_WINDOW_MS = 500; // 3 seconds
    NamespacedKey PERK_TAG = new NamespacedKey("pit_perks", "perkitem");
    PerkManager pm = PerkManager.getInstance();

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack droppedItem = event.getItemDrop().getItemStack();
        long now = System.currentTimeMillis();
        long lastAttempt = dropAttempts.getOrDefault(uuid, 0L);

        // First, check if they are trying to drop a perk item.
        if (droppedItem.getPersistentDataContainer().has(PERK_TAG)) {
            event.getPlayer().sendMessage(Component.text("You cannot drop perk items.").color(NamedTextColor.RED));
            event.setCancelled(true);
        }
        else if (now - lastAttempt <= DROP_WINDOW_MS) {
            // Confirmed item drop.
            player.sendMessage(Component.text("you dropped your item.").color(NamedTextColor.RED));
            dropAttempts.remove(uuid);
        } else {
            // First warning
            event.setCancelled(true);
            dropAttempts.put(uuid, now);
            player.sendMessage(Component.text("Are you sure you want to drop the item?").color(NamedTextColor.RED));
            player.sendMessage(Component.text("Drop again to confirm.").color(NamedTextColor.RED));
        }
    }
}
