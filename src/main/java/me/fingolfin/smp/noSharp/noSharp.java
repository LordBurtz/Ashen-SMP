package me.fingolfin.smp.noSharp;

import me.fingolfin.smp.data.data;
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

public class noSharp implements Listener {
    public HashMap<Material, Integer> types = new HashMap<>();
    public String mercenary;

    private final main plugin;

    public noSharp(main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        constructMap();
        setMercenary();
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
        if (!(event.getDamager().getName().equals(mercenary))) return;
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;
        Material item = ((Player) event.getDamager()).getInventory().getItemInMainHand().getType();
        if (!types.containsKey(item)) return;
        event.setDamage(types.get(item));
        Bukkit.getLogger().log(Level.WARNING, "damge: " + event.getFinalDamage());
    }
}
