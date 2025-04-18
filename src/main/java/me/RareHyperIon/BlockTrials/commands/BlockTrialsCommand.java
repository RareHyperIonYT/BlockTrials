package me.RareHyperIon.BlockTrials.commands;

import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.handler.LanguageHandler;
import me.RareHyperIon.BlockTrials.inventories.SelectionScreen;
import me.RareHyperIon.BlockTrials.utility.StringUtility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class BlockTrialsCommand implements CommandExecutor, TabCompleter {

    private final LanguageHandler language;
    private final BlockTrials plugin;

    public BlockTrialsCommand(final BlockTrials plugin, final LanguageHandler language) {
        this.language = language;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if(!sender.hasPermission("blocktrials.admin")) {
            sender.sendMessage(StringUtility.applyColor(this.language.get("no-permission")));
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage(StringUtility.applyColor(this.language.get("correct-usage").replaceAll("\\{usage}", "help")));
            return true;
        }

        final String option = args[0];

        switch (option) {
            case "menu": {
                if(!(sender instanceof Player)) {
                    sender.sendMessage(StringUtility.applyColor(this.language.get("player-command-only")));
                    break;
                }

                ((Player) sender).openInventory(new SelectionScreen(this.plugin).getInventory());
                break;
            }

            case "reload": {
                this.plugin.reload();
                sender.sendMessage(StringUtility.applyColor(this.language.get("reload-success")));
                break;
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label, final String[] args) {
       switch (args.length) {
           case 1: {
               final List<String> options = List.of("menu", "reload");
               return StringUtility.getPartialMatches(args[0], options);
           }
       }

        return List.of();
    }

}
