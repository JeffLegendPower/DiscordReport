package io.github.jefflegendpower.discordreport.Commands;

import io.github.jefflegendpower.discordreport.DiscordReport;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class Reload implements CommandExecutor {

    private final DiscordReport plugin;
    private TextChannel textChannel;
    private JDA bot;

    public Reload(DiscordReport instance, JDA bot) {
        plugin = instance;
        this.bot = bot;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("reload-discordreport")) {
            if (args.length > 0) {
                sender.sendMessage(ChatColor.RED + "Too many arguments!");
                return false;
            }
            PluginDescriptionFile file = plugin.getDescription();

            if (sender instanceof Player player) {

                if (hasReload(player)) {

                    reloadBot();
                    sender.sendMessage(ChatColor.AQUA + file.getFullName() + ChatColor.GRAY + " reloaded files succesfully");
                    System.out.println("[DiscordReport] " + (sender.getName() + " reloaded" + (file.getFullName())));
                }
                else {
                    sender.sendMessage(ChatColor.RED + "You do not have access to that.");
                }
            }
            else {
                reloadBot();
                DiscordReport.log.info("[pluginname] " + file.getName() + " was succesfully reloaded");
            }
            return true;
        }
        return false;
    }

    private boolean hasReload(Player player) {

        if (player.hasPermission("discordreport.reload") || player.isOp()) {
            return true;
        } else return player.hasPermission("discordreport.*");
    }

    private void reloadBot() {

        plugin.reloadConfig();
        plugin.saveConfig();
        plugin.updateBot();
    }
}