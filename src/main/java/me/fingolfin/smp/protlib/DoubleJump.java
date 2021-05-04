package me.fingolfin.smp.protlib;

import me.fingolfin.smp.main;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class DoubleJump implements Listener {
    private main plugin;

    public final int COOLDOWN = 5;
    public HashMap<Player, Long> jumpers = new HashMap<>();

    public DoubleJump(main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerFly(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (jumpers.containsKey(player)) {
            if ((System.currentTimeMillis() - jumpers.get(event.getPlayer())) / 1000 < COOLDOWN) {
                player.sendMessage(
                        ChatColor.GOLD +
                                String.format("You have to wait %.1f secs to use that again",
                                        ((float) COOLDOWN - ((float) (System.currentTimeMillis() - jumpers.get(event.getPlayer()))) / 1000)));
                event.setCancelled(true);
                return;
            }
            if (!(player.getGameMode().equals(GameMode.CREATIVE))) {
                event.setCancelled(true);
                player.setFlying(false);
                player.setAllowFlight(false);
                Vector vector = player.getLocation().getDirection();
                player.setVelocity(vector.setY(1.1D));
                player.playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 15);
                bossBar(player);
                jumpers.put(player, System.currentTimeMillis());
            }
        } else {
            if (!(player.getGameMode().equals(GameMode.CREATIVE))) {
                event.setCancelled(true);
                player.setFlying(false);
                player.setAllowFlight(false);
                Vector vector = player.getLocation().getDirection();
                player.setVelocity(vector.setY(1.1D));
                player.playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 15);
                bossBar(player);
                jumpers.put(player, System.currentTimeMillis());
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) && !(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR))) {
            player.setAllowFlight(true);
        }
    }

    public void bossBar(Player player, boolean bool) {
        BossBar bar = Bukkit.createBossBar(ChatColor.GOLD + "Cooldown Double Jump", BarColor.YELLOW, BarStyle.SOLID);
        bar.addPlayer(player);
        bar.setProgress(1D);
        Bukkit.getServer().getScheduler().runTaskLater(plugin,() -> {
            bar.setProgress(0.8D);
        } ,20);
        Bukkit.getServer().getScheduler().runTaskLater(plugin,() -> {
            bar.setProgress(0.6D);
        } ,40);
        Bukkit.getServer().getScheduler().runTaskLater(plugin,() -> {
            bar.setProgress(0.4D);
        } ,60);
        Bukkit.getServer().getScheduler().runTaskLater(plugin,() -> {
            bar.setProgress(0.2D);
        } ,80);
        Bukkit.getServer().getScheduler().runTaskLater(plugin,() -> {
            bar.setProgress(0.1D);
        } ,90);
        Bukkit.getServer().getScheduler().runTaskLater(plugin,() -> {
            bar.setProgress(0.0D);
            bar.removeAll();
        } ,100);
        return;
    }

    public void bossBar(Player player) {
        BossBar bar = Bukkit.createBossBar(ChatColor.DARK_PURPLE + "Cooldown Double Jump", BarColor.PURPLE, BarStyle.SEGMENTED_20);
        bar.addPlayer(player);
        bar.setProgress(1D);
        for (double i = 0; i <= 1; i = i + 0.05) {
            double finalI = i;
            Bukkit.getServer().getScheduler().runTaskLater(plugin,() -> {
                bar.setProgress(1.0D - finalI);
            } , (long) (finalI*100));
        }
        Bukkit.getServer().getScheduler().runTaskLater(plugin,() -> {
            bar.setProgress(0.0D);
            bar.removeAll();
        } ,100);
    }
}
