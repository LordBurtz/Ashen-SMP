package me.fingolfin.smp.convertable;

import me.fingolfin.smp.data.data;
import me.fingolfin.smp.main;
import me.fingolfin.smp.ojisan.XPgain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class DaBaby implements Listener, CommandExecutor {
    public static String daBaby;
    public static final int COOLDOWN = 30;

    private List<Entity>  dababies = new ArrayList<>();
    private main plugin;
    private long last_shoot;

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

        BukkitTask killDababies = new BukkitRunnable() {
            @Override
            public void run() {
                destroyDaBabys();
            }
        }.runTaskTimer(plugin, 20L, 1200L);
        Bukkit.getServer().getLogger().log(Level.INFO, "killing all the unmounted dababies");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return true;
        if (!commandSender.getName().equals(daBaby)) {
            commandSender.sendMessage("noo you are not dababy");
            return true;
        }

        if (((System.currentTimeMillis() - last_shoot) / 1000) < COOLDOWN) {
            commandSender.sendMessage(ChatColor.GOLD +
                    String.format("You have to wait %.1f secs to use that again",
                            ((float) COOLDOWN -( (float) (System.currentTimeMillis() - last_shoot)) /1000)));
            return true;
        }
        last_shoot = System.currentTimeMillis();

        Player player = (((Player) commandSender).getPlayer());
        player.getInventory().addItem(new ItemStack(Material.CARROT_ON_A_STICK));
        Entity pig = player.getWorld().spawnEntity(player.getLocation(), EntityType.PIG);
        pig.setPassenger(player);
        dababies.add(pig);
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
    public void onDababyDeath(PlayerDeathEvent event) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.getName().equals(XPgain.oldman)) {
                continue;
            }
            online.showPlayer(plugin, (Player) event.getEntity());
        }
        destroyDaBabys();
        event.getEntity().getInventory().remove(Material.CARROT_ON_A_STICK);
    }

    @EventHandler
    public  void onPigLeave (VehicleExitEvent event) {
        if (!(event.getExited() instanceof  Player)) return;
        if (event.getVehicle().getName() == null) return;
        if (event.getVehicle().getCustomName() == null) return;
        if ((event.getVehicle().getCustomName().isEmpty())) return;
        if (!(event.getVehicle().getCustomName().equals("a convertable"))) return;
        if (!(event.getExited().getName().equals(daBaby))) return;
        if (!(event.getVehicle().getType().equals(EntityType.PIG))) return;
        event.getVehicle().setSilent(true);
        ((Player) event.getExited()).setInvisible(false);
        event.getVehicle().remove();
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.getName().equals(XPgain.oldman)) {
                continue;
            }
            online.showPlayer(plugin, (Player) event.getExited());
        }
        ((Player) event.getExited()).getInventory().remove(Material.CARROT_ON_A_STICK);
    }

    public void destroyDaBabys() {
        if (dababies.isEmpty()) return; //is this necessary to not throw a null exception?
        for (Entity entity : dababies)  {
            if (entity.getPassengers().isEmpty()) {
                entity.remove();
            }
        }
    }

    @EventHandler
    public void onRejoin(PlayerJoinEvent event) {
        if (!(event.getPlayer().getName().equals(daBaby))) return;
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.getName().equals(XPgain.oldman)) {
                continue;
            }
            online.showPlayer(plugin, event.getPlayer());
        }
        if (event.getPlayer().isInvisible()) event.getPlayer().setInvisible(false);
    }

    @EventHandler
    public  void onPigDie (VehicleDestroyEvent event) {
        if ((event.getVehicle().getPassengers().isEmpty())) return;
        if (!(event.getVehicle().getPassengers().get(0) instanceof  Player)) return;
        if (!(event.getVehicle().getCustomName().equals("a convertable"))) return;
        if (!(event.getVehicle().getPassengers().get(0).getName().equals(daBaby))) return;
        if (!(event.getVehicle().getType().equals(EntityType.PIG))) return;
        event.getVehicle().setSilent(true);
        ((Player) event.getVehicle().getPassengers().get(0)).setInvisible(false);
        event.getVehicle().remove();
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.getName().equals(XPgain.oldman)) {
                continue;
            }
            online.showPlayer(plugin, (Player) event.getVehicle().getPassengers().get(0));
        }
        ((Player) event.getVehicle().getPassengers().get(0)).getInventory().remove(Material.CARROT_ON_A_STICK);
    }
}
