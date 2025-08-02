package Serialization;

import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.UUID;

public final class PerkManager {
    JavaPlugin plugin = Init.getProvidingPlugin(Init.class);
    LuckPerms luckPerms = LuckPermsProvider.get();
    private final File dataFolder;
    private static final Map<UUID, Map<Integer, Permission>> slotMap = new HashMap<>();
    private final Map<UUID, LinkedHashSet<Permission>> perkMap = new HashMap<>();
    private static final Exception PerkException = new Exception("There was an issue updating the perk most likely caused by null luckperms data", new NullPointerException());

    public PerkManager() {

        this.dataFolder = new File(plugin.getDataFolder(), "player data");
        try {
            if (!dataFolder.exists() && !dataFolder.mkdirs()) {
                Bukkit.getConsoleSender().sendMessage(Component.text("Failed to create data folder"));
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(Component.text("Error creating data folder: " + e.getMessage()));
        }
    }

    public void clearPerks(UUID uuid) {
        Map<Integer, Permission> slots = slotMap.getOrDefault(uuid, new HashMap<>());
        luckPerms.getUserManager().loadUser(uuid).thenAcceptAsync(threadsafe -> {
            if (threadsafe != null) {
                for (Permission p : slots.values()) {
                    threadsafe.data().remove(Node.builder(p.getName()).build());
                }
                luckPerms.getUserManager().saveUser(threadsafe);
                slotMap.remove(uuid);
            }
        });
    }

    public void setPerk(UUID uuid, int slot, String permissionNode) {
        Permission p = new Permission(permissionNode);
        luckPerms.getUserManager().loadUser(uuid).thenAcceptAsync(threadsafe -> {
           if (threadsafe != null) {
               threadsafe.data().add(Node.builder(permissionNode).build());
               luckPerms.getUserManager().saveUser(threadsafe);
               slotMap.computeIfAbsent(uuid, u -> new HashMap<>()).put(slot, p);
           }
        });
    }

    public void removePerk(UUID uuid, int slot, String permissionNode) {
        Map<Integer, Permission> slots = slotMap.getOrDefault(uuid, new HashMap<>());
        if (slots.get(slot) != null && slots.get(slot).getName().equals(permissionNode)) {
            luckPerms.getUserManager().loadUser(uuid).thenAcceptAsync(threadsafe -> {
               threadsafe.data().remove(Node.builder(permissionNode).build());
               luckPerms.getUserManager().saveUser(threadsafe);
               slots.remove(slot);
               Bukkit.getConsoleSender().sendMessage("");
            });
        }
    }

    // 📥 Load perks for one player
    public void loadPerks(UUID uuid) {
        File file = new File(dataFolder, uuid.toString() + ".yml");
        if (!file.exists()) return;
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        LinkedHashSet<Permission> perks = new LinkedHashSet<>();
        ConfigurationSection section = config.getConfigurationSection("perks");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                String permissionName = config.getString("perks." + key);
                if (permissionName != null && !permissionName.isEmpty()) {
                    Permission p = new Permission(permissionName);
                    perks.add(p);
                } else {
                    // optional: log the offending key or handle it gracefully
                    Bukkit.getConsoleSender().sendMessage(Component.text(key + "' is missing a valid permission string."));
                }
            }
        }
        perkMap.put(uuid, perks);
    }

    // 💾 Save perks for one player
    public void savePerks(UUID uuid) {
        File file = new File(dataFolder, uuid.toString() + ".yml");
        YamlConfiguration config = new YamlConfiguration();
        LinkedHashSet<Permission> perks = perkMap.get(uuid);
        if (perks != null) {
            int index = 0;
            for (Permission p : perks) {
                config.set("perks." + index, p);
                index++;
            }
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    // 🔄 Load perks for all players
    public void loadAllPerks() {
        File[] files = dataFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                String uuidStr = file.getName().replace(".yml", "");
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    loadPerks(uuid);
                } catch (IllegalArgumentException ignored) { }
            }
        }
    }

    // 💾 Save perks for all players
    public void saveAllPerks() {
        for (UUID uuid : perkMap.keySet()) {
            savePerks(uuid);
        }
    }

    public void assignPerkToSlot(UUID uuid, int slot, Permission perk) {
        slotMap.computeIfAbsent(uuid, u -> new HashMap<>()).put(slot, perk);
    }
    public static Permission getPermissionFromSlot(UUID uuid, int slot) {
        Map<Integer, Permission> slots = slotMap.get(uuid);
        if (slots != null) {
            return slots.get(slot);
        }
        return null;
    }

    public void strengthPerkTaskTimer() {

    }

}

