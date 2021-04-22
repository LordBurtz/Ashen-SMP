package me.fingolfin.smp.specialItem;

import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Ramen implements Listener {
    private main plugin;

    public Ramen(main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onConsumeSoup(PlayerItemConsumeEvent event) {
        if (!event.getItem().equals(new ItemStack(Material.MUSHROOM_STEM))) return;
        if (!event.getItem().getItemMeta().getDisplayName().equals("Ramen")) return;
        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 0));
    }
}
