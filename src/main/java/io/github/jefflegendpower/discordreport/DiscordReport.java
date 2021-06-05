package io.github.jefflegendpower.discordreport;

import io.github.jefflegendpower.discordreport.Commands.Reload;
import io.github.jefflegendpower.discordreport.Commands.Report;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.logging.Logger;

public final class DiscordReport extends JavaPlugin {

    private JDA bot = null;
    private Report report;
    public static Logger log = Bukkit.getLogger();

    @Override
    public void onEnable() {

        if (getServer().getPluginManager().getPlugin("JDA") == null) {
            log.warning("[DiscordReport] " + "JDA not found! Disabling plugin...");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        // Plugin startup logic
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        buildJDA();

        report = new Report(bot);
        Reload reload = new Reload();

        this.getCommand("report").setExecutor(report);
        this.getCommand("discordreport").setExecutor(reload);

        System.out.println("Bot booted");

        this.saveConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (bot != null)
            bot.shutdown();
    }

    public void buildJDA() {
        try {
            JDABuilder jdaBuilder = JDABuilder.createDefault(this.getConfig().getString("Bot token"));
            jdaBuilder.setActivity(Activity.playing(this.getConfig().getString("Activity") + ""));

            bot = jdaBuilder.build();
            bot.awaitReady();
            if (report != null)
                report.updateBot(bot);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Report.setDisabled(true);
        } catch (LoginException | NullPointerException e) {
            log.warning("[DiscordReport] " + "Invalid bot token! Disabling report command...");
            Report.setDisabled(true);
        }
    }
}
