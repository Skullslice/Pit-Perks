package onEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class onBarbarianPerk implements Listener {

    ItemStack[] barbarianPerkItem = new ItemStack[] {
            createBarbarianAxe(Material.IRON_AXE),
            createBarbarianAxe(Material.DIAMOND_AXE),
            createBarbarianAxe(Material.NETHERITE_AXE)
    };

    private static double getSwordDamage(Material axeType) {
        return switch (axeType) {
            case IRON_AXE -> 6.0;
            case DIAMOND_AXE -> 7.0;
            case NETHERITE_AXE -> 8.0;
            default -> 5.0;
        };
    }


    public static ItemStack createBarbarianAxe(Material type) {

        NamespacedKey PERK_TAG = new NamespacedKey(
                "pit_perks",
                "perkitem"
        );

        AttributeModifier speedModifier = new AttributeModifier(
                new NamespacedKey("pit_perks", "barbarian_speed"),
                0.5,
                AttributeModifier.Operation.ADD_SCALAR,
                EquipmentSlotGroup.HEAD
        );

        if (!Arrays.asList(
                Material.IRON_AXE,
                Material.DIAMOND_AXE,
                Material.NETHERITE_AXE
        ).contains(type)) throw new IllegalArgumentException("Invalid axe material");

        ItemStack axe = new ItemStack(type);
        ItemMeta meta = axe.getItemMeta();
        meta.displayName(Component.text("Barbarian Axe").color(NamedTextColor.LIGHT_PURPLE));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.values());
        meta.addAttributeModifier(Attribute.ATTACK_SPEED ,speedModifier);
        meta.getPersistentDataContainer().set(PERK_TAG, PersistentDataType.BYTE, (byte) 1);

        axe.setItemMeta(meta);

        return axe;
    }


    public boolean isBarbarian(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) return false;
        ItemMeta givenMeta = itemStack.getItemMeta();

        Component expectedName = Component.text("Barbarian Axe").color(NamedTextColor.LIGHT_PURPLE);

        return givenMeta.isUnbreakable() && Objects.equals(givenMeta.displayName(), expectedName);
    }

    public void overrideAxes(HumanEntity human) {
        Inventory inv = human.getInventory();
        ItemStack[] contents = inv.getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item == null) continue;
            if (isBarbarian(item)) inv.removeItem(item);
        }

    }

    public void overrideSwords(HumanEntity human) {
        Inventory inv = human.getInventory();
        ItemStack[] contents = inv.getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item == null) continue;

            if (item.getType() == Material.IRON_SWORD) {
                inv.setItem(i, createBarbarianAxe(Material.IRON_AXE));
            }
            else if (item.getType() == Material.DIAMOND_SWORD) {
                inv.setItem(i, createBarbarianAxe(Material.DIAMOND_AXE));
            }
            else if (item.getType() == Material.NETHERITE_SWORD) {
                inv.setItem(i, createBarbarianAxe(Material.NETHERITE_AXE));
            }
        }
    }


    @EventHandler
    public void onPermissionChange(PermissionChange.PermissionChangeEvent permission) {

        if (!permission.getPermission().equals("perks.barbarian")) return;
        if (permission.wasGranted()) {
            overrideSwords(permission.getPlayer());
        }
        else {
            overrideAxes(permission.getPlayer());
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack droppedItem = event.getItemDrop().getItemStack();
        if (isBarbarian(droppedItem)) {
            event.getPlayer().sendMessage(Component.text("You cannot drop your Barbarian Axe").color(NamedTextColor.RED));
            event.getPlayer().sendMessage(Component.text("Drop key again to delete the item.").color(NamedTextColor.RED));
        }
    }
}
