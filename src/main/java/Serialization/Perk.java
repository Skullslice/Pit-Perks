package Serialization;

import org.bukkit.Material;

import java.awt.*;
import java.util.List;

public class Perk {
    String name;
    String permissionNode;
    String displayName;
    Material material;
    List<String> lore;

    public Perk(String name, String permissionNode, String displayName, Material material, List<String> lore, List<String> aliases) {
        this.name = name;
        this.permissionNode = permissionNode;
        this.displayName = displayName;
        this.material = material;
        this.lore = lore;
    }

    @Override
    public String toString() {
        return displayName + " [" + permissionNode + "]";
    }
}
