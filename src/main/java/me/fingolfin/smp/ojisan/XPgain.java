package me.fingolfin.smp.ojisan;

import me.fingolfin.smp.data.data;
import me.fingolfin.smp.main;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class XPgain {
    public static String oldman;

    private final main plugin;

    public XPgain(main plugin) {
        this.plugin = plugin;

        setOldMan();
        Bukkit.getServer().getLogger().log(Level.INFO, String.format("[SMP] The Old Man is %s", oldman));

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (Bukkit.getPlayer(oldman) == null) {
            } else {
                Bukkit.getPlayer(oldman).giveExpLevels(1);
            }
        }, 0, 12000);
    }

    private void setOldMan() {
        data data = new data(plugin, "config.yml");
        data.saveDefaultConfig("config.yml");
        if (data.getConfig("config.yml").contains("Old_Man.name")) {
            oldman = data.getConfig("config.yml").getString("Old_Man.name");
        } else {
            data.getConfig("config.yml").set("mayor.name", "Fingolf1n");
            oldman = "Fingolf1n";
        }
        data.saveConfig("config.yml");
    }
}
