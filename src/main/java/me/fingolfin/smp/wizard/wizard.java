package me.fingolfin.smp.wizard;

import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class wizard implements Listener {
    private final main plugin;
    private long last_shoot;
    public final int COOLDOWN = 17;

    public wizard(main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        last_shoot = 0L;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRightClickFireball(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!(player.getName().equals("Pagnol"))) return;
        if (!(player.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD))) return;
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (((System.currentTimeMillis() - last_shoot) / 1000) < COOLDOWN) {
            player.sendMessage(ChatColor.GOLD +
                    String.format("You have to wait %.1f secs to use that again",
                            ((float) COOLDOWN -( (float) (System.currentTimeMillis() - last_shoot)) /1000)));
            return;
        }
        last_shoot = System.currentTimeMillis();

        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setIsIncendiary(true);
        fireball.setVelocity(player.getLocation().getDirection().multiply(2));
    }
}
