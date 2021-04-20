package me.fingolfin.smp.data;

import me.fingolfin.smp.main;
import me.fingolfin.smp.meditation.meditation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.*;
import java.util.logging.Level;

public class data {

    private main plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public data(main plugin, String file) {
        this.plugin = plugin;
        saveDefaultConfig(file);
    }

    public void reloadConfig(String file) {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), file);

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaulStream = this.plugin.getResource(file);
        if (defaulStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaulStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig(String file) {
        if (this.dataConfig == null) reloadConfig(file);
        return this.dataConfig;
    }

    public void saveConfig(String file) {
        if (this.dataConfig == null || this.configFile == null) return;
        try {
            this.getConfig(file).save(this.configFile);
        } catch (IOException exception) {
            this.plugin.getLogger().log(Level.SEVERE, "Couldnt save config to " + this.configFile, exception);
        }
    }

    public void saveDefaultConfig(String file) {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), file);

        if (!this.configFile.exists()) {
            this.plugin.saveResource(file, false);
        }
    }


}
/*
public static FileConfiguration loadData(main plugin, String fileName) {
        try {
            File file = new File(plugin.getDataFolder() + File.separator + fileName);
            System.out.println(plugin.getDataFolder());

            if (!file.exists()) {
                file.createNewFile();
            }

            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            return config;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 */