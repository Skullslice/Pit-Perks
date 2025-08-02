package onEvent;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeMutateEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class PermissionChange implements Listener {

    public static void listen(LuckPerms lp, JavaPlugin plugin) {

        Consumer<NodeMutateEvent> handler = event -> {
            if (!event.isUser()) return;

            Set<Node> before = event.getDataBefore();
            Set<Node> after = event.getDataAfter();

            for (Node node : after) {
                if (!before.contains(node) && node instanceof PermissionNode pNode) {
                    // This permission was added
                    String permission = pNode.getPermission();
                    UUID uuid = ((User) event.getTarget()).getUniqueId();
                    boolean granted = true;
                    notifyBukkitListener(uuid, permission, granted, plugin);
                }
            }
            for (Node node : before) {
                if (!after.contains(node) && node instanceof PermissionNode pNode) {
                    // This permission was removed
                    String permission = pNode.getPermission();
                    UUID uuid = ((User) event.getTarget()).getUniqueId();
                    boolean granted = false;
                    notifyBukkitListener(uuid, permission, granted, plugin);
                }
            }
        };
        lp.getEventBus().subscribe(NodeAddEvent.class, handler);
        lp.getEventBus().subscribe(NodeRemoveEvent.class, handler);
    }

    public static void notifyBukkitListener(UUID uuid, String pNode, boolean granted, JavaPlugin plugin) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                Bukkit.getPluginManager().callEvent(
                        new PermissionChangeEvent(player, pNode, granted)
                );
            }
        });
    }

    public static class PermissionChangeEvent extends Event {
        private static final HandlerList handlers = new HandlerList();
        private final Player player;
        private final String permission;
        private final boolean granted;

        public PermissionChangeEvent(Player player, String permission, boolean granted) {
            this.player = player;
            this.permission = permission;
            this.granted = granted;
        }

        public Player getPlayer() { return player; }
        public String getPermission() { return permission; }
        public boolean wasGranted() { return granted; }
        @Override public HandlerList getHandlers() { return handlers; }
        public static HandlerList getHandlerList() { return handlers; }
    }
}

