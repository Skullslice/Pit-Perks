package onEvent;

import onCommand.Permissions;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

public class onVampirismPerk implements Listener {

    @EventHandler
    public void onVampirism(EntityDamageByEntityEvent heal) {
        if (!(heal.getDamager() instanceof Player)) return;
        if (!(heal.getEntity() instanceof LivingEntity)) return;
        if (!(heal.getDamager().hasPermission(Permissions.VAMPIRISM))) return;
        LivingEntity target = (LivingEntity) heal.getEntity();
        double finalDamage = heal.getFinalDamage();

        if (finalDamage >= target.getHealth()) {
            PotionEffect regen = new PotionEffect(
                    PotionEffectType.REGENERATION, 8, 0, true, true,true
            );
            ((Player) heal.getDamager()).addPotionEffect(regen);
        }
        double health = ((Player) heal.getDamager()).getHealth();
        @Nullable AttributeInstance max = ((Player) heal.getDamager()).getAttribute(Attribute.MAX_HEALTH);
        if (max != null) {
            ((Player) heal.getDamager()).setHealth(Math.min(max.getValue(), (((Player) heal.getDamager()).getHealth() + 1)));
        }
    }
}
