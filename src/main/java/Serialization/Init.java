package Serialization;

import java.io.Serializable;
import java.util.Objects;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import onCommand.PerkCommand;
import onCommand.Permissions;
import onEvent.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;



public class Init extends JavaPlugin implements Serializable {

    private LuckPerms lp;
    private PerkManager perkManager;

    @Override
    public void onEnable() {

        lp = LuckPermsProvider.get();

        perkManager = new PerkManager();
        PerkCommand perkCommand = new PerkCommand();

        perkManager.loadAllPerks();

        PermissionChange.listen(lp, this);

        Bukkit.getServer().getPluginManager().registerEvents(new EntityMoveMap(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new onGladiatorPerk(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new onVampirismPerk(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new onJuggPerk(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ItemDropManager(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new onFirstStrikePerk(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new onDirtyPerk(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new onStrengthPerk(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PermissionChange(), this);

        getServer().getPluginManager().addPermission(Permissions.ADMIN);
        getServer().getPluginManager().addPermission(Permissions.GLADIATOR);
        getServer().getPluginManager().addPermission(Permissions.JUGGERNAUT);
        getServer().getPluginManager().addPermission(Permissions.BARBARIAN);
        getServer().getPluginManager().addPermission(Permissions.DIRTY);
        getServer().getPluginManager().addPermission(Permissions.VAMPIRISM);
        getServer().getPluginManager().addPermission(Permissions.FIRST_STRIKE);
        getServer().getPluginManager().addPermission(Permissions.GOLDEN_HEADS);
        getServer().getPluginManager().addPermission(Permissions.TASTY_SOUPS);
        getServer().getPluginManager().addPermission(Permissions.THICK);
        getServer().getPluginManager().addPermission(Permissions.STRENGTH_CHAINING);
        getServer().getPluginManager().addPermission(Permissions.STREAKER);
        getServer().getPluginManager().addPermission(Permissions.GOLDEN_STEAKS);
        getServer().getPluginManager().addPermission(Permissions.BONK);
        getServer().getPluginManager().addPermission(Permissions.BOUNTY_HUNTER);
        getServer().getPluginManager().addPermission(Permissions.CONGLOMORATE);
        getServer().getPluginManager().addPermission(Permissions.CO_OP_CAT);
        getServer().getPluginManager().addPermission(Permissions.FISHING_ROD);
        getServer().getPluginManager().addPermission(Permissions.LAVA);
        getServer().getPluginManager().addPermission(Permissions.LUCKY_DIAMOND);
        getServer().getPluginManager().addPermission(Permissions.OLYMPUS);
        getServer().getPluginManager().addPermission(Permissions.RING_MINER);
        getServer().getPluginManager().addPermission(Permissions.MARATHON);
        getServer().getPluginManager().addPermission(Permissions.TRICKLE_DOWN);
        getServer().getPluginManager().addPermission(Permissions.RECON);
        getServer().getPluginManager().addPermission(Permissions.SUPER_STREAKER);
        getServer().getPluginManager().addPermission(Permissions.SPAMMER);
        getServer().getPluginManager().addPermission(Permissions.OVERHEAL);
        getServer().getPluginManager().addPermission(Permissions.RAMBO);

        onStrengthPerk.timerTask.run();

        Objects.requireNonNull(getCommand("pitperks")).setExecutor(perkCommand);
        Objects.requireNonNull(getCommand("pitperks")).setTabCompleter(perkCommand);

    }
    @Override
    public void onDisable() {
        perkManager.saveAllPerks();

    }

}
