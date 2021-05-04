package me.fingolfin.smp.protlib;

import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DeathChest implements Listener {
    private main plugin;

    public DeathChest(main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().clear();
        Inventory inv = event.getEntity().getInventory();
        Location loc = event.getEntity().getLocation();
        Block block = loc.getBlock();
        block.setType(Material.CHEST);
        Chest chest = (Chest) loc.getBlock().getState();
        Inventory chestinv = chest.getInventory();
        int y = 1;
        for (ItemStack item : inv) {
            if (y<27) {
            if (item == null) {
                continue;
            } else {
                chestinv.setItem(y, item);
                y++;
            }} else {
                event.getEntity().getWorld().dropItem(loc, item);
            }
        }

        event.getEntity().sendMessage(String.format("your deathchest is at %d %d %d", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));

        event.getEntity().getInventory().clear();
        event.getDrops().clear();
        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
            chestinv.clear();
        }, 8400L);
    }
}
