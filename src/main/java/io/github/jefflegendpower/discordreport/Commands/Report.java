package io.github.jefflegendpower.discordreport.Commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Report implements CommandExecutor {

    JDA bot;
    TextChannel textChannel;

    public Report(JDA bot) {
        this.bot = bot;
        this.textChannel = bot.getTextChannelById(844737301238120452L);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("report") && sender instanceof Player player) {
            if (!(player.hasPermission("reporttodiscord.report") || player.hasPermission("*") || player.isOp())) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute that command");
                return false;
            }
            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "Who are you reporting?");
                return true;
            }
            else if (args.length >= 2) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 1; i < args.length; i++) {
                    stringBuffer.append(args[i]);
                    textChannel.sendMessage(player.getName() + " has reported " + args[0] + " for " + stringBuffer.toString()).queue();
                    return true;
                }
            }
            else {
                textChannel.sendMessage(player.getName() + " has reported " + args[0]).queue();
                return true;
            }
        }
        return false;
    }
}
