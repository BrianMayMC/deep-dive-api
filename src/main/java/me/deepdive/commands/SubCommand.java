package me.deepdive.commands;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Implement this class if you want to make a sub command for your command manager.
 */
public interface SubCommand {

    List<String> getName();

    int getNeededArgs();

    String getSyntax();

    String getDescription();

    String getPermission();

    void execute(CommandSender sender, String[] args);

    List<String> getTabCompletion(Player p, String args[]);
}