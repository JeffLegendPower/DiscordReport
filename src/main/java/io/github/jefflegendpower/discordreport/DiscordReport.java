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

public final class DiscordReport extends JavaPlugin{

    private JDA bot = null;
    public static Logger log = Bukkit.getLogger();
    private Report report;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();

        JDABuilder jdaBuilder = JDABuilder.createDefault(this.getConfig().getString("Bot token"));
        jdaBuilder.setActivity(Activity.playing(this.getConfig().getString("Activity") + "reloaded"));
        try {
            bot = jdaBuilder.build();
            bot.awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }

        report = new Report(bot);
        Reload reload = new Reload(this, bot);

        this.getCommand("report").setExecutor(report);
        this.getCommand("reload-discordreport").setExecutor(reload);

        System.out.println("Bot booted");


        this.saveConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void updateBot() {
        JDABuilder jdaBuilder = null;
        try {
            jdaBuilder = JDABuilder.createDefault(this.getConfig().getString("Bot token"));
        } catch (NullPointerException e) {
            System.out.println("Invalid bot token! Disabling report...");
            report.setDisabled(true);
        }
        jdaBuilder.setActivity(Activity.playing(this.getConfig().getString("Activity")));

        try {
            bot = jdaBuilder.build();
            bot.awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
        report.updateBot(bot);
    }
}
