package me.fingolfin.smp.protlib;

import me.fingolfin.smp.data.data;
import me.fingolfin.smp.main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class motd implements Listener {
    private main plugin;

    public String motd;

    public motd(main plugin) {
        this.plugin = plugin;
        setMOTD();
    }


    public void setMOTD() {
        data data = new data(plugin, "config.yml");
        data.saveDefaultConfig("config.yml");
        if (data.getConfig("config.yml").contains("MOTD")) {
            motd = data.getConfig("config.yml").getString("MOTD");
        } else {
            data.getConfig("config.yml").set("MOTD", "THIS IS A TEST SERVER");
            motd = "THIS IS A TEST SERVER";
        }
        data.saveConfig("config.yml");
        motd = ChatColor.RED + "" + ChatColor.BOLD + motd;
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        event.setMaxPlayers(-1);
        event.setMotd(motd);
    }
}
