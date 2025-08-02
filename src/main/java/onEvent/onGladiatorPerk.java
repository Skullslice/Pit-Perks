package onEvent;

import onCommand.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class onGladiatorPerk implements Listener {
    EntityMoveMap moveInstance = new EntityMoveMap();

    @EventHandler
    public void onGladiator(EntityDamageByEntityEvent damage) {
        if (!(damage.getEntity() instanceof Player)) return;
        if (!(damage.getEntity().hasPermission(Permissions.GLADIATOR))) return;
        float amount = (float)damage.getDamage();
        float calculateDamageReduction = IterateNearbyEntities.getDamageReduction(damage.getEntity().getLocation(), moveInstance.nearMap);
        damage.setDamage(amount * calculateDamageReduction);
    }

}
