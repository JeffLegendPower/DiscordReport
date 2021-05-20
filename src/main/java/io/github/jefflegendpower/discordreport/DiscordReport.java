package io.github.jefflegendpower.discordreport;

import io.github.jefflegendpower.discordreport.Commands.Report;
import io.github.jefflegendpower.discordreport.Files.CustomConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.logging.Logger;

public final class DiscordReport extends JavaPlugin{

    public static Logger log = Bukkit.getLogger();

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Config setup
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        CustomConfig.setUp();
        CustomConfig.getConfig().options().copyDefaults(true);

        JDABuilder jdaBuilder = JDABuilder.createDefault(CustomConfig.getConfig().getString("Bot token"));
//        JDABuilder jdaBuilder = JDABuilder.createDefault("ODE4MzA0Njg0Mjc1MDA3NDkw.YEWHoA.1Fg9IzG-C2VEPXS1oikO1n3J-es");
        jdaBuilder.setActivity(Activity.playing("testo testo"));
        JDA bot = null;
        try {
            bot = jdaBuilder.build();
            bot.awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Bot booted");

        Report report = new Report(bot);
        this.getCommand("report").setExecutor(report);

        CustomConfig.save();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
