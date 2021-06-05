package io.github.jefflegendpower.discordreport.Commands;

import io.github.jefflegendpower.discordreport.DiscordReport;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import okhttp3.internal.platform.Platform;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Report implements CommandExecutor {

    private boolean disabled = false;

    private TextChannel textChannel;
    private Color color;
    private final DiscordReport plugin = DiscordReport.getPlugin(DiscordReport.class);

    public Report(JDA bot) {

        try {
            this.textChannel = bot.getTextChannelById(plugin.getConfig().getLong("Textchannel ID"));
        }
        catch (NullPointerException e) {
            DiscordReport.log.warning("You have put an invalid channel id! Disabling report command...");
            disabled = true;
        }

        try {
            color = (Color) Color.class.getField(plugin.getConfig().getString("Embed color")).get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException | NullPointerException e) {
            DiscordReport.log.warning("You have put an invalid color in the config! Defaulting to gray...");
            color = Color.GRAY;
        }
    }

    private Report() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("report") && sender instanceof Player player) {
            if (disabled) {
                sender.sendMessage(ChatColor.AQUA + "[DiscordReport] " + ChatColor.RED + "This command has been disabled due to an invalid config");
                return true;
            }

            if (!(player.hasPermission("discordreport.report") || player.hasPermission("*") || player.isOp())) {
                player.sendMessage(ChatColor.AQUA + "[DiscordReport] " + ChatColor.RED + "You do not have permission to execute that command");
                return true;
            }

            EmbedBuilder builder = new EmbedBuilder();

            builder.setColor(color);

            switch (args.length) {
                case 0 -> player.sendMessage(ChatColor.RED + "Who are you reporting?");
                case 1 -> reportNoReason(builder, player, args[0]);
                default -> reportWithReason(builder, player, args);
            }
            return true;
        }

        return false;
    }

    private void reportNoReason(EmbedBuilder builder, Player player, String reportedPlayer) {
        builder.setTitle(player.getName() + " has reported " + reportedPlayer);
        textChannel.sendMessage(builder.build()).queue();
        player.sendMessage(ChatColor.GREEN + "Report accepted");
    }

    private void reportWithReason(EmbedBuilder builder, Player player, String[] args) {
        String[] reportReason = (String[]) ArrayUtils.remove(args, 0);
        String report = String.join(" ", reportReason);

        builder.setTitle(player.getName() + " has reported " + args[0] + " for");
        builder.setDescription(report);
        textChannel.sendMessage(builder.build()).queue();

        player.sendMessage(ChatColor.GREEN + "Report accepted");
    }

    public void updateBot(JDA bot) {
        this.disabled = false;

        try {
            this.textChannel = bot.getTextChannelById(plugin.getConfig().
                    getLong("Textchannel ID"));

            if (!textChannel.canTalk())
                throw new NullPointerException();
        } catch (NullPointerException e) {
            DiscordReport.log.warning("[DiscordReport] " + "You have put an invalid channel id! Disabling report command...");
            disabled = true;
        }

        try {
            color = (Color) Color.class.getField(plugin.getConfig().getString("Embed color")).get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException | NullPointerException e) {
            DiscordReport.log.warning("[DiscordReport] " + "You have put an invalid color in the config! Defaulting to gray...");
            color = Color.GRAY;
        }
    }

    public static void setDisabled(boolean disabled) {
        Report localInstance = new Report();
        localInstance.disabled = disabled;
    }
}
