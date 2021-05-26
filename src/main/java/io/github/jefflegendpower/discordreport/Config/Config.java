//package io.github.jefflegendpower.discordreport.Config;
//
//import io.github.jefflegendpower.discordreport.DiscordReport;
//import org.bukkit.Bukkit;
//import org.bukkit.configuration.file.FileConfiguration;
//import org.bukkit.configuration.file.YamlConfiguration;
//
//import java.io.File;
//import java.io.IOException;
//
//public class Config {
//
//    private static File file;
//    private static FileConfiguration customFile;
//    private DiscordReport plugin = DiscordReport.getPlugin(DiscordReport.class);
//
//    // Finds or generates config.yml
//    public static void setUp() {
//        file = new File(Bukkit.getServer().getPluginManager().getPlugin("DiscordReport").getDataFolder(),
//                "config.yml");
//
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                DiscordReport.log.info("config.yml creation failed.");
//                e.printStackTrace();
//            }
//        }
//        customFile = YamlConfiguration.loadConfiguration(file);
//    }
//
//    public static FileConfiguration getConfig() {
//        return customFile;
//    }
//
//    public static void save() {
//        try {
//            customFile.save(file);
//        } catch (IOException e) {
//            DiscordReport.log.info("Could not save the file");
//            e.printStackTrace();
//        }
//    }
//
//    public static void reload() {
//        customFile = YamlConfiguration.loadConfiguration(file);
//    }
//}
