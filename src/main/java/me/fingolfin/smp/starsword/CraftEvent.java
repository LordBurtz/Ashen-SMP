package me.fingolfin.smp.starsword;

import me.fingolfin.smp.data.data;
import me.fingolfin.smp.main;
import me.fingolfin.smp.wizard.wizard;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

public class CraftEvent implements Listener {
    public static String wizard1 = wizard.wizard;

    private final main plugin;

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
            if (entity.getName().equals(wizard1)) {
                return;
            } else {
                event.getInventory().setItem(0, null);
            }
        }
    }
}