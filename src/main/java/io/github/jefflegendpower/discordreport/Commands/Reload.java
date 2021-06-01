package io.github.jefflegendpower.discordreport.Commands;

import io.github.jefflegendpower.discordreport.DiscordReport;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class Reload implements CommandExecutor {

    private final DiscordReport plugin = DiscordReport.getPlugin(DiscordReport.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("discordreport")) {

            PluginDescriptionFile file = plugin.getDescription();

            if (args.length < 1) {
                sender.sendMessage(ChatColor.AQUA + "[DiscordReport] " + ChatColor.GRAY + " Correct syntax: discordreport reload");
                return true;
            }
            if (!args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage(ChatColor.AQUA + "[DiscordReport] " + ChatColor.GRAY + " Correct syntax: discordreport reload");
                return true;
            }

            if (sender instanceof Player player) {

                if (canReload(player)) {

                    reloadBot();
                    sender.sendMessage(ChatColor.AQUA + "[DiscordReport] " + ChatColor.GRAY + " reloaded files succesfully");
                    DiscordReport.log.info("[DiscordReport] " + (sender.getName() + " reloaded" + (file.getFullName())));
                }
                else {
                    sender.sendMessage(ChatColor.AQUA + "[DiscordReport] " + ChatColor.RED + "You do not have access to that.");
                }
            }

            else {
                reloadBot();
                DiscordReport.log.info("[DiscordReport] successfully reloaded");
            }
            return true;
        }
        return false;
    }

    private boolean canReload(Player player) {

        if (player.hasPermission("discordreport.reload") || player.isOp()) {
            return true;
        } else return player.hasPermission("discordreport.*");
    }

    private void reloadBot() {
        plugin.reloadConfig();
        plugin.saveConfig();
        Report.setDisabled(false);
        plugin.buildJDA();
    }
}