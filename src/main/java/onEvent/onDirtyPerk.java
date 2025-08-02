package onEvent;

import onCommand.Permissions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class onDirtyPerk implements Listener {
    @EventHandler
    public void onDirty(EntityDamageByEntityEvent damage) {
        if (!(damage.getDamager() instanceof Player)) return;
        if (!(damage.getEntity() instanceof LivingEntity)) return;
        if (!(damage.getDamager().hasPermission(Permissions.DIRTY))) return;

        LivingEntity target = (LivingEntity) damage.getEntity();
        double finalDamage = damage.getFinalDamage();

        if (finalDamage >= target.getHealth()) {
            PotionEffect resistance = new PotionEffect(
                    PotionEffectType.RESISTANCE, 4, 1, true, true,true
            );
            ((Player) damage.getDamager()).addPotionEffect(resistance);
        }
    }
}
