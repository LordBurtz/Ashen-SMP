package me.fingolfin.smp.effects;

import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class effects implements Listener {
    private main plugin;

    public effects(main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (!(player.getName().equals("Fingolf1n") || player.getName().equals("FallenAngel_0103"))) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false, false));
        }, 20);
    }

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPlayedBefore()) return;
        if (!(player.getName().equals("Fingolf1n") || player.getName().equals("FallenAngel_0103"))) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 9999, 1, false, false));
    }
}
