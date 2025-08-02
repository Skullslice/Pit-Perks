package onEvent;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class onThickPerk {

    NamespacedKey thickPerk = new NamespacedKey("pit_perks", "thick_health");


    AttributeModifier maxHealthModifier = new AttributeModifier(
            thickPerk,
            4,
            AttributeModifier.Operation.ADD_NUMBER
    );

    public void onThickEquip(InventoryClickEvent equip) {
        //todo - add deluxemenus API
        //todo - when deluxemenus sends the signal that a valid gui interaction has occured: do something

        HumanEntity p = equip.getWhoClicked();


        // Ensure no duplicate modifiers with the same key
        AttributeInstance attr = p.getAttribute(Attribute.MAX_HEALTH);
        if (attr != null && attr.getModifiers().stream().noneMatch(mod -> mod.getKey().equals(thickPerk))) {
            attr.addModifier(maxHealthModifier);
        }

    }
    public void onThickUnequip(InventoryClickEvent unequip) {

        HumanEntity p = unequip.getWhoClicked();

        // Ensure player HAS the modifier equipped
        AttributeInstance attr = p.getAttribute(Attribute.MAX_HEALTH);
        if (attr != null && attr.getModifiers().stream().anyMatch(mod -> mod.getKey().equals(thickPerk))) {
            attr.removeModifier(maxHealthModifier);
        }

    }
}
