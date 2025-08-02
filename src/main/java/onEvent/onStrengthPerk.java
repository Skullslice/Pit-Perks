package onEvent;

import Placeholders.StrengthExpansion;
import Serialization.Init;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import onCommand.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.*;

public class onStrengthPerk implements Listener {

    static Map<UUID, Float> strengthMap = new HashMap<>();
    static Map<UUID, Long> timerMap = new HashMap<>();

    @EventHandler
    public void onStrength(EntityDamageByEntityEvent damage) {
        if (!(damage.getDamager() instanceof Player damager)) return;
        if (!(damage.getEntity() instanceof LivingEntity target)) return;
        if (!(damage.getDamager().hasPermission(Permissions.STRENGTH_CHAINING))) return;

        UUID playerUUID = damager.getUniqueId();
        float boost = strengthMap.getOrDefault(playerUUID, 0.0f);
        double modifiedDamage = damage.getDamage() * (1.0 + boost);
        damage.setDamage(modifiedDamage);
        double finalDamage = damage.getFinalDamage();

        if (finalDamage >= target.getHealth()) {
            timerMap.put(playerUUID, System.currentTimeMillis());
            strengthMap.put(playerUUID, Math.min(boost + 0.08f, 0.4f));

        }
    }

    public static Map<UUID, Float> getStrengthMapView() {
        return Collections.unmodifiableMap(strengthMap);
    }

    public static Map<UUID, Long> getTimerMapView() {
        return Collections.unmodifiableMap(timerMap);
    }

    public static BukkitRunnable timerTask = new BukkitRunnable() {
        @Override
        public void run() {
            Iterator<Map.Entry<UUID, Long>> iterator = timerMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<UUID, Long> entry = iterator.next();
                UUID uuid = entry.getKey();
                long remaining = entry.getValue() - 250;

                if (remaining <= 0) {
                    strengthMap.remove(uuid); // Remove strength multiplier
                    iterator.remove(); // Remove cooldown entry
                } else {
                    timerMap.put(uuid, remaining); // Update timer
                }
            }
        }
    };


}
