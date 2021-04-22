package me.fingolfin.smp.noSharp;

import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.logging.Level;

import static java.lang.String.valueOf;

public class noSharp implements Listener {
    private main plugin;
    public HashMap<Material, Integer> types = new HashMap<>();

    public noSharp(main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        constructMap();
    }

    public void constructMap() {
        types.put(Material.WOODEN_SWORD, 7);
        types.put(Material.WOODEN_AXE, 10);

        types.put(Material.STONE_SWORD, 8);
        types.put(Material.STONE_AXE, 12);

        types.put(Material.IRON_SWORD, 8);
        types.put(Material.IRON_AXE, 12);

        types.put(Material.GOLDEN_SWORD, 7);
        types.put(Material.GOLDEN_AXE, 10);

        types.put(Material.DIAMOND_SWORD, 10);
        types.put(Material.DIAMOND_AXE, 12);

        types.put(Material.NETHERITE_SWORD, 11);
        types.put(Material.NETHERITE_AXE, 13);
    }

    @EventHandler
    public void onDamageEvent(EntityDamageByEntityEvent event) {
        if (!(event.getDamager().getName().equals("Fallen_Angel0103"))) return;
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;
        Material item = ((Player) event.getDamager()).getInventory().getItemInMainHand().getType();
        if (!types.containsKey(item)) return;
        event.setDamage(types.get(item));
        Bukkit.getLogger().log(Level.WARNING, "damge: " + valueOf(event.getFinalDamage()));
    }
}
