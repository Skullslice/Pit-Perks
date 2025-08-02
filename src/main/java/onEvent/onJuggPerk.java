package onEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import onCommand.Permissions;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class onJuggPerk implements Listener {

    NamespacedKey PERK_TAG = new NamespacedKey("pit_perks", "perkitem");

    @EventHandler
    public void onJugg(PlayerJoinEvent join) {
        if (!(join.getPlayer().hasPermission(Permissions.JUGGERNAUT))) return;
        if (join.getPlayer().getInventory().getHelmet() != null) return;
        setJuggPerk(join.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent respawnEvent) {
        if (!(respawnEvent.getPlayer().hasPermission(Permissions.JUGGERNAUT))) return;
        if (respawnEvent.getPlayer().getInventory().getHelmet() != null) return;
        setJuggPerk(respawnEvent.getPlayer());
    }

    public void setJuggPerk(Player player) {

        AttributeModifier gravityModifier = new AttributeModifier(
                new NamespacedKey("pit_perks", "juggernaut_gravity"),
                0.0275,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlotGroup.HEAD
        );


        ItemStack helmet = new ItemStack(Material.IRON_HELMET);
        ItemMeta helmetMeta = helmet.getItemMeta();

        helmetMeta.setUnbreakable(true);
        helmetMeta.displayName(Component.text("Juggernaut Helmet").color(NamedTextColor.LIGHT_PURPLE));
        helmetMeta.addAttributeModifier(Attribute.GRAVITY, gravityModifier);
        helmetMeta.getPersistentDataContainer().set(PERK_TAG, PersistentDataType.BYTE, (byte) 1);

        //set the meta and item
        helmet.setItemMeta(helmetMeta);
        player.getInventory().setItem(39, helmet);
    }
}
