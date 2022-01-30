package io.github.bananafalls.burnouttorches.commands;

import io.github.bananafalls.burnouttorches.BurnoutTorches;
import io.github.bananafalls.burnouttorches.util.SaveManagement;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Reload implements CommandExecutor, TabCompleter {

    final BurnoutTorches plugin;

    public Reload() {
        this.plugin = BurnoutTorches.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SaveManagement saveManagement = plugin.getSaveManagement();
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(p.hasPermission("burnouttorches.reload")) {
                if (args.length != 1) {
                    p.sendMessage(translateAlternateColorCodes('&', plugin.getConfig().getString("bad_arguments_message")));
                } else if (args[0].equalsIgnoreCase("reload")) {
                    plugin.reloadConfig();
                    saveManagement.runAutosave();
                    p.sendMessage(translateAlternateColorCodes('&', plugin.getConfig().getString("reload_message")));
                } else {
                    p.sendMessage(translateAlternateColorCodes('&', plugin.getConfig().getString("bad_arguments_message")));
                }
            } else {
                p.sendMessage(translateAlternateColorCodes('&', plugin.getConfig().getString("no_permission_message")));
            }
        } else {
            if (args.length != 1) {
                Bukkit.getConsoleSender().sendMessage(translateAlternateColorCodes('&', plugin.getConfig().getString("bad_arguments_message")));
            } else if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                saveManagement.runAutosave();
                Bukkit.getConsoleSender().sendMessage(translateAlternateColorCodes('&', plugin.getConfig().getString("reload_message")));
            } else {
                Bukkit.getConsoleSender().sendMessage(translateAlternateColorCodes('&', plugin.getConfig().getString("bad_arguments_message")));
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1){
            List<String> completions = new ArrayList<String>(); completions.add("reload");
            return completions;
        } else {
            return new ArrayList<String>();
        }
    }
}
