package onCommand;

import Serialization.Init;
import Serialization.PerkManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PerkCommand implements CommandExecutor, TabCompleter {

    private final LuckPerms luckPerms = LuckPermsProvider.get();
    private final PerkManager pm = new PerkManager();

    /**
     * @param args arguments passed to the method.
     * @return only returns true jf the luckperms api successfully saves the user.
     */

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!commandSender.hasPermission(Permissions.ADMIN)) {
            commandSender.sendMessage("No Access");
            return false;
        }
        if (args[0].equalsIgnoreCase("clear")) {
            boolean silent = Arrays.asList(args).contains("-s");
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);


            // Clear out the luckperms permissions
            try {
                pm.clearPerks(player.getUniqueId());
                if (silent) return true;
                commandSender.sendMessage("removed all perks from " + player.getName());
                return true;
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(e.fillInStackTrace().getMessage());
                commandSender.sendMessage("Failed processing the user");
                return false;
            }
        }
        else if (args[0].equalsIgnoreCase("set")) {
            boolean silent = Arrays.asList(args).contains("-s");

            int slot = Integer.parseInt(args[1]);
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
            String permission = args[3];
            try {
                pm.setPerk(player.getUniqueId(), slot, args[3]);
                if (silent) return true;
                commandSender.sendMessage("slot " + slot + " has been set to " + permission + " for " + player.getName());
                return true;
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(e.fillInStackTrace().getMessage());
                commandSender.sendMessage("Failed processing the user");
                return false;
            }
        }
        else if (args[0].equalsIgnoreCase("remove")) {
            int slot = Integer.parseInt(args[1]);
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
            boolean silent = Arrays.asList(args).contains("-s");


            // Remove LuckPerms permission
            try {
                pm.removePerk(player.getUniqueId(), slot, args[3]);
                if (silent) return true;
                commandSender.sendMessage("slot " + slot + " has been removed from " + player);
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(e.fillInStackTrace().getMessage());
                commandSender.sendMessage("Failed processing the user");
                return false;
            }
        }
        else {
            commandSender.sendMessage("Valid arguments are <set/remove/clear>");
            return false;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!sender.hasPermission(Permissions.ADMIN)) return Collections.emptyList();
        switch (strings.length) {
            case 1:

        }
        return List.of();
    }

    public List<String> getPluginPermissions(String prefix) {
        return Bukkit.getPluginManager().getPermissions().stream()
                .map(Permission::getName)
                .filter(name -> name.startsWith(prefix))
                .collect(Collectors.toList());
    }

}
