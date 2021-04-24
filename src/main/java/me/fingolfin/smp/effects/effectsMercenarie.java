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

public class effectsMercenarie implements Listener {
    public String mercenary;

    private final main plugin;

    public effectsMercenarie(main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        setMercenary();
        Bukkit.getServer().getLogger().log(Level.INFO, String.format("[SMP] The mercenary is %s", mercenary));
    }

    private void setMercenary() {
        data data = new data(plugin, "config.yml");
        data.saveDefaultConfig("config.yml");
        if (data.getConfig("config.yml").contains("mercenary.name")) {
            mercenary = data.getConfig("config.yml").getString("mercenary.name");
        } else {
            data.getConfig("config.yml").set("mercenary.name", "_Ecl1pse_");
            mercenary = "_Ecl1pse_";
        }
        data.saveConfig("config.yml");
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (!(player.getName().equals(mercenary))) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false, false));
        }, 20);
    }

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPlayedBefore()) return;
        if (!(player.getName().equals(mercenary))) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
    }
}
