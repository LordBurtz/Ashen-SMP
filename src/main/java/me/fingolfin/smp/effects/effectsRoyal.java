package me.fingolfin.smp.effects;

import me.fingolfin.smp.data.data;
import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.logging.Level;

public class effectsRoyal implements Listener {
    public String Royal;
    
    private final main plugin;

    public effectsRoyal(main plugin) {
        this.plugin = plugin;
        setRoyal();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getServer().getLogger().log(Level.INFO, String.format("[SMP] The Royal Guard is %s", Royal));
    }

    private void setRoyal() {
        data data = new data(plugin, "config.yml");
        data.saveDefaultConfig("config.yml");
        if (data.getConfig("config.yml").contains("royal.name")) {
            Royal = data.getConfig("config.yml").getString("royal.name");
        } else {
            data.getConfig("config.yml").set("royal.name", "Celestrom");
            Royal = "Celestrom";
        }
        data.saveConfig("config.yml");
    }
    
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (!player.getName().equals(Royal)) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false, false));
        }, 20);
    }

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPlayedBefore()) return;
        if (!(player.getName().equals(Royal))) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false, false));
        }, 20);
    }
}
