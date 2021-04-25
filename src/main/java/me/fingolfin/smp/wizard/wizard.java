package me.fingolfin.smp.wizard;

import me.fingolfin.smp.data.data;
import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.logging.Level;

public class wizard implements Listener {
    public final int COOLDOWN = 17;
    public static String wizard;

    private final main plugin;
    private long last_shoot;

    public wizard(main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        last_shoot = 0L;
        setWizard();
        Bukkit.getServer().getLogger().log(Level.INFO, String.format("[SMP] The wizard is %s", wizard));
    }

    private void setWizard() {
        data data = new data(plugin, "config.yml");
        data.saveDefaultConfig("config.yml");
        if (data.getConfig("config.yml").contains("wizard.name")) {
            wizard = data.getConfig("config.yml").getString("wizard.name");
        } else {
            data.getConfig("config.yml").set("wizard.name", "Pagnol");
            wizard = "Pagnol";
        }
        data.saveConfig("config.yml");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRightClickFireball(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!(player.getName().equals(wizard))) return;
        if (!(player.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD))) return;
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (((System.currentTimeMillis() - last_shoot) / 1000) < COOLDOWN) {
            player.sendMessage(ChatColor.GOLD +
                    String.format("You have to wait %.1f secs to use that again",
                            ((float) COOLDOWN -( (float) (System.currentTimeMillis() - last_shoot)) /1000)));
            return;
        }
        last_shoot = System.currentTimeMillis();

        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setIsIncendiary(true);
        fireball.setVelocity(player.getLocation().getDirection().multiply(2));
    }
}
