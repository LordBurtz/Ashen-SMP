package me.fingolfin.smp.wizard;

import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class wizard implements Listener {
    private main plugin;
    private Integer last_shoot;

    public wizard(main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        last_shoot = 0;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRightClickFireball(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!(player.getName().equals("Pagnol") || player.getName().equals("Fingolf1n"))) return;
        System.out.println("called");
        if (!(player.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD))) return;
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;

        //TODO: COOOLDOWNsd

        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setIsIncendiary(true);
        fireball.setVelocity(player.getLocation().getDirection().multiply(2));
    }
}
