package me.fingolfin.smp.anvilInfRepair;

import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import java.util.logging.Level;

public class AnvilInfRepair implements Listener {

    private main plugin;
    private final int REPAIR_CAP = 33;

    public AnvilInfRepair(main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getLogger().log(Level.INFO, "anvil loaded");
    }

    @EventHandler
    public void onInventoryClickEvent (InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        ItemStack item = event.getCurrentItem();
        if (item.getType() == Material.AIR) return;
        if (item.getItemMeta() == null) return;
        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof Repairable)) return;
        Repairable repair = (Repairable) meta;
        if (!repair.hasRepairCost()) return;
        repair.setRepairCost(REPAIR_CAP -1);
        item.setItemMeta((ItemMeta) repair);
    }
}
