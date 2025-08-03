package Serialization;

import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class PerkManager {
    JavaPlugin plugin = Init.getProvidingPlugin(Init.class);
    LuckPerms luckPerms = LuckPermsProvider.get();
    private final File dataFolder;
    private static final Map<UUID, Map<Integer, Permission>> slotMap = new HashMap<>();
    private static final NamespacedKey PERK_TAG = new NamespacedKey("pitperks", "perkitem");

    private static volatile PerkManager instance;

    public static synchronized PerkManager getInstance() {
        if (instance == null) {
            instance = new PerkManager();
        }
        return instance;
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }


    private PerkManager() {
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

    public void loadPerks(UUID uuid) {
        File file = new File(dataFolder, uuid.toString() + ".yml");
        HashMap<Integer, Permission> slots = new HashMap<>();
        if (!file.exists()) return;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("perks");

        if (section != null) {
            List<String> keys = new ArrayList<>(section.getKeys(false));
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                String permissionName = config.getString("perks." + key);

                if (permissionName != null && !permissionName.isEmpty()) {
                    Permission p = new Permission(permissionName);
                    slots.put(i + 1, p); // Keeping 1-based indexing
                } else {
                    Bukkit.getConsoleSender().sendMessage(Component.text(key + "' is missing a valid permission string."));
                }
            }
        }
        slotMap.put(uuid, slots);
    }

    public void savePerks(UUID uuid) {
        File file = new File(dataFolder, uuid.toString() + ".yml");
        YamlConfiguration config = new YamlConfiguration();
        Map<Integer, Permission> slots = slotMap.get(uuid);
        if (slots != null) {
            for (int i = 0; i < slots.size(); i++) {
                config.set("perks." + (i + 1), slots.get(i));
            }
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

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

    public void saveAllPerks() {
        for (UUID uuid : slotMap.keySet()) {
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

    public boolean addPerkTag(@NotNull ItemStack itemStack) {
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(PERK_TAG)) {
            itemStack.getItemMeta().getPersistentDataContainer().set(PERK_TAG, PersistentDataType.STRING, "true");
            return true;
        }
        return false;
    }
    public boolean removePerkTag(@NotNull ItemStack itemStack) {
        if (itemStack.getItemMeta().getPersistentDataContainer().has(PERK_TAG)) {
            itemStack.getItemMeta().getPersistentDataContainer().remove(PERK_TAG);
            return true;
        }
        return false;
    }

}

