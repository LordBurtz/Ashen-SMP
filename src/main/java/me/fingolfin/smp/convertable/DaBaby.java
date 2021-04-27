package me.fingolfin.smp.convertable;

import me.fingolfin.smp.data.data;
import me.fingolfin.smp.main;
import me.fingolfin.smp.ojisan.XPgain;
import net.bytebuddy.build.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.logging.Level;

public class DaBaby implements Listener, CommandExecutor {
    public static String daBaby;

    private main plugin;

    public DaBaby(main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this,plugin);
        plugin.getCommand("convertable").setExecutor(this);
        setDaBaby();
        Bukkit.getLogger().log(Level.INFO, String.format("[SMP] Da Baby is %s\n less goo", daBaby));
    }

    public void setDaBaby() {
        data data = new data(plugin, "config.yml");
        data.saveDefaultConfig("config.yml");
        if (data.getConfig("config.yml").contains("DaBaby.name")) {
            daBaby = data.getConfig("config.yml").getString("DaBaby.name");
        } else {
            data.getConfig("config.yml").set("DaBaby.name", "Dajo_Di_Majo");
            daBaby = "Dajo_Di_Majo";
        }
        data.saveConfig("config.yml");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return true;
        if (!commandSender.getName().equals(daBaby)) {
            commandSender.sendMessage("noo you are not dababy");
            return true;
        }
        Player player = (((Player) commandSender).getPlayer());
        player.getInventory().addItem(new ItemStack(Material.CARROT_ON_A_STICK));
        Entity pig = player.getWorld().spawnEntity(player.getLocation(), EntityType.PIG);
        pig.setPassenger(player);
        pig.setCustomName("a convertable");
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.getName().equals(XPgain.oldman)) {
                continue;
            }
            online.hidePlayer(plugin, player);
        }
        ((Pig) pig).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 17, true, false));
        commandSender.sendMessage("less goo");
        return true;
    }

    @EventHandler
    public void onPigLeave (EntityDismountEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!event.getEntity().getName().equals(daBaby)) return;
        if (!event.getDismounted().getCustomName().equals("a convertable")) return;
        event.getDismounted().setSilent(true);
        ((Player) event.getEntity()).setInvisible(false);
        event.getDismounted().setFallDistance(100);
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.getName().equals(XPgain.oldman)) {
                continue;
            }
            online.showPlayer(plugin, (Player) event.getEntity());
        }
        ((Player) event.getEntity()).getInventory().remove(Material.CARROT_ON_A_STICK);
    }
}
