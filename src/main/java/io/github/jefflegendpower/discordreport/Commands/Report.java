package io.github.jefflegendpower.discordreport.Commands;

import io.github.jefflegendpower.discordreport.DiscordReport;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Report implements CommandExecutor {

    private boolean disabled = false;
    private JDA bot;
    private TextChannel textChannel;
    private Color color;
    private final DiscordReport plugin = DiscordReport.getPlugin(DiscordReport.class);

    public Report(JDA bot) {
        try {
            this.textChannel = bot.getTextChannelById(plugin.getConfig().getLong("Textchannel ID"));
            this.bot = bot;
        }
        catch (NullPointerException e) {
            DiscordReport.log.warning("You have put an invalid channel id! Disabling plugin...");
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

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("report") && sender instanceof Player player) {
            if (!(player.hasPermission("discordreport.report") || player.hasPermission("*") || player.isOp())) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute that command");
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
        else if(disabled)
            sender.sendMessage(ChatColor.RED + "This command has been disabled. Please contact a server administrator if you see this problem");

        else {
            DiscordReport.log.info("Only players can report!");
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

        builder.setTitle(player.getName() + " has reported " + args[0] + " for:");
        builder.setDescription(report);
        textChannel.sendMessage(builder.build()).queue();

        player.sendMessage(ChatColor.GREEN + "Report accepted");
    }

    public void updateBot(JDA bot) {
        try {
            this.bot = bot;
        } catch (NullPointerException e) {
            DiscordReport.log.warning("You have put an invalid bot id! Disabling command...");
            disabled = true;
        }
        try {
            this.textChannel = bot.getTextChannelById(plugin.getConfig().
                    getLong("Textchannel ID"));
        } catch (NullPointerException e) {
            DiscordReport.log.warning("You have put an invalid channel id! Disabling command...");
            disabled = true;
        }
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
