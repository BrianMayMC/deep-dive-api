package me.deepdive.commands;

import lombok.Getter;
import lombok.Setter;
import me.deepdive.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to create a command with sub commands easily.
 * If you only need a base command, do NOT use this.
 * <p>
 * Simply extend command manager, then add all your SubCommands to the list
 * and the command manager will take care of everything else for you
 */
public abstract class CommandManager implements TabExecutor {

    @Getter
    private final ArrayList<SubCommand> commands = new ArrayList<>();

    @Setter
    private String helpPermission = "";
    @Setter private String helpMessageHeader = "";

    @Setter private SubCommand elseCommand;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        executeCommand(sender, args);
        return true;
    }

    public void executeCommand(CommandSender p, String[] args){
        if(args.length == 1 && args[0].equalsIgnoreCase("help")){
            if(p.hasPermission(helpPermission)) {
                p.sendMessage(Utils.c(helpMessageHeader));
                for (SubCommand s : getCommands()) {
                    p.sendMessage(Utils.c(s.getDescription() + s.getSyntax()));
                }
            }else{
                p.sendMessage(Utils.c("&fUnknown command. Type \"/help\" for help."));
            }
            return;
        }else if(args.length >= 1){
            for(int i = 0; i < getCommands().size(); i++) {
                SubCommand s = getCommands().get(i);
                for (String name : s.getName()) {
                    if (name.equalsIgnoreCase(args[0])) {
                        if (!(p.hasPermission(s.getPermission())) && !(s.getPermission().equalsIgnoreCase(""))) {
                            p.sendMessage(Utils.c("&fUnknown command. Type \"/help\" for help."));
                            return;
                        }
                        if (args.length - 1 < s.getNeededArgs()) {
                            p.sendMessage(Utils.c("&c&l(!)&c Not enough arguments."));
                            return;
                        }
                        s.execute(p, Arrays.copyOfRange(args, 1, args.length));
                        return;
                    }
                }
            }
        }
        if(elseCommand != null){
            if(args.length >= 1) {
                elseCommand.execute(p, Arrays.copyOfRange(args, 1, args.length));
            }else{
                elseCommand.execute(p, new String[]{});
            }
        }
    }



    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
        if(args.length == 1) {
            List<String> commandNames = new ArrayList<>();
            for (SubCommand s : getCommands()) {
                if (sender.hasPermission(s.getPermission()) || s.getPermission().equalsIgnoreCase("")) {
                    commandNames.addAll(s.getName());
                }
            }
            return commandNames;
        }
        for(int i = 1; i < args.length; i++){
            for (SubCommand s : getCommands()) {
                if (sender.hasPermission(s.getPermission()) || s.getPermission().equalsIgnoreCase("")) {
                    for(String name : s.getName()) {
                        if (args[0].equalsIgnoreCase(name)) {
                            return s.getTabCompletion((Player) sender, Arrays.copyOfRange(args, 1, args.length));
                        }
                    }
                }
            }
        }
        return null;
    }
}