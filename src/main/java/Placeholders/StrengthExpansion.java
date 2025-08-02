package Placeholders;

import Serialization.Init;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import onEvent.onStrengthPerk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StrengthExpansion extends PlaceholderExpansion {

    private final JavaPlugin plugin;

    private final Map<UUID, Float> strengthMap;
    private final Map<UUID, Long> cooldownMap;

    public StrengthExpansion() {
        plugin = Init.getProvidingPlugin(Init.class);
        strengthMap = onStrengthPerk.getStrengthMapView();
        cooldownMap = onStrengthPerk.getTimerMapView();
    }

    @Override
    public String getIdentifier() {
        return "pitperks";
    }

    @Override
    public String getAuthor() {
        return "Holiday";
    }

    @Override
    public String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (!identifier.equals("strength_timer")) return null;
        float percent = strengthMap.get(player.getUniqueId());
        long remaining = 8000 - (System.currentTimeMillis() - cooldownMap.get(player.getUniqueId()));
        return "Strength: " + percent + "% " + Math.max(remaining / 1000, 1) + "s";
    }
}
