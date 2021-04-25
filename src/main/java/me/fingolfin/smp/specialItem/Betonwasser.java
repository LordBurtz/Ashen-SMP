package me.fingolfin.smp.specialItem;

import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class Betonwasser implements Listener {
    private final main plugin;

    public Betonwasser(main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onConsumeSoup(PlayerItemConsumeEvent event) {
        ItemStack potion = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.AWKWARD));
        potion.setItemMeta(meta);
        if (!event.getItem().getItemMeta().equals(potion.getItemMeta())) return;
        if (!event.getItem().getItemMeta().getDisplayName().equals("Betonwasser")) return;
        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 0));
    }

}
