package io.github.jefflegendpower.discordreport;

import io.github.jefflegendpower.discordreport.Commands.Report;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

public final class DiscordReport extends JavaPlugin{

    @Override
    public void onEnable() {
        // Plugin startup logic
        JDABuilder jdaBuilder = JDABuilder.createDefault("ODE4MzA0Njg0Mjc1MDA3NDkw.YEWHoA.1Fg9IzG-C2VEPXS1oikO1n3J-es");
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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
