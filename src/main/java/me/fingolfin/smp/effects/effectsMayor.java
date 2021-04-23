package me.fingolfin.smp.effects;

import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class effectsMayor implements Listener {
    private final main plugin;

    public effectsMayor(main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onConsumeAkwardPot(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (!(player.getName().equals("GrueziHD"))) return;
        ItemStack potion = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.AWKWARD));
        potion.setItemMeta(meta);
        if (!event.getItem().getItemMeta().equals(potion.getItemMeta())) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1200, 2));
            }, 20);
    }
}
