package me.fingolfin.smp.ojisan;

import me.fingolfin.smp.main;
import org.bukkit.Bukkit;

public class XPgain {
    private main plugin;

    public XPgain(main plugin) {
        this.plugin = plugin;

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (Bukkit.getPlayer("Fingolf1n") == null) {
            } else {
                Bukkit.getPlayer("Fingolf1n").giveExpLevels(1);
            }
        }, 0, 12000);
    }
}
