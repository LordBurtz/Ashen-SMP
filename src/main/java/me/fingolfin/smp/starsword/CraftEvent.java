package me.fingolfin.smp.starsword;

import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class CraftEvent implements Listener {
    private main plugin;

    public CraftEvent(main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCraftEvent(PrepareItemCraftEvent event) {
        if (event.getRecipe() == null || event.getRecipe().getResult() == null)
            return;
        if (!event.getRecipe().getResult().equals(starsword.sword())) return;

        for(HumanEntity entity : event.getViewers()) {
            if (entity.getName().equals("Pagnol")) {
                return;
            } else {
                event.getInventory().setItem(0, null);
            }
        }

    }
}
