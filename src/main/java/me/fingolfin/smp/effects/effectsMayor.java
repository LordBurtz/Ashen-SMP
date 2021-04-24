package me.fingolfin.smp.effects;

import me.fingolfin.smp.data.data;
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

import java.util.logging.Level;

public class effectsMayor implements Listener {
    public String mayor;
    
    private final main plugin;

    public effectsMayor(main plugin) {
        this.plugin = plugin;
        setMayor();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getServer().getLogger().log(Level.INFO, String.format("[SMP] The mayor is %s", mayor));
    }

    private void setMayor() {
        data data = new data(plugin, "config.yml");
        data.saveDefaultConfig("config.yml");
        if (data.getConfig("config.yml").contains("mayor.name")) {
            mayor = data.getConfig("config.yml").getString("mayor.name");
        } else {
            data.getConfig("config.yml").set("mayor.name", "GrueziHD");
            mayor = "GrueziHD";
        }
        data.saveConfig("config.yml");
    }
    
    @EventHandler
    public void onConsumeAkwardPot(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (!(player.getName().equals(mayor))) return;
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
