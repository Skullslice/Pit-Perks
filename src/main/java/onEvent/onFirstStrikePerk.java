package onEvent;

import onCommand.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class onFirstStrikePerk implements Listener {
    private final HashMap<UUID, Set<UUID>> strikeMap = new HashMap<>();
    //private final Set<UUID> hitSet = new HashSet<>();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent damage) {
        if (!(damage.getDamager() instanceof Player)) return;
        if (!damage.getDamager().hasPermission(Permissions.FIRST_STRIKE)) return;

        UUID target = damage.getEntity().getUniqueId();
        UUID damager = damage.getDamager().getUniqueId();

        Set<UUID> struckTargets = strikeMap.computeIfAbsent(damager, k -> new HashSet<>());

        float increase = 0.35f;

        if (!struckTargets.contains(target)) {
            damage.setDamage(damage.getDamage() * increase);
            struckTargets.add(target);
        }
    }

    @EventHandler
    public void onKill(EntityDeathEvent death) {
        if (!(death.getEntity() instanceof Player)) return;
        if (!(death.getDamageSource().getDirectEntity() instanceof Player)) return;
        if (!death.getDamageSource().getCausingEntity().hasPermission(Permissions.FIRST_STRIKE)) return;

        //if a first strike user dies clear their instance and start the cycle again
        UUID dead = death.getDamageSource().getCausingEntity().getUniqueId();
        strikeMap.remove(dead);
    }
}

