package me.fingolfin.smp.data;

import me.fingolfin.smp.main;
import me.fingolfin.smp.meditation.meditation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.*;

public class data {

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
}
