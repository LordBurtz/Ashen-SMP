package me.fingolfin.smp.meditation;

import me.fingolfin.smp.data.data;
import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BetterMeditation implements Listener, CommandExecutor {
    private final main plugin;
    private Player player;
    private static final HashMap<String, Location> meditators = new HashMap<>();
    private final me.fingolfin.smp.data.data data;
    private final String config_file = "meditation.yml";

    public BetterMeditation(main plugin) {
        this.plugin =  plugin;
        plugin.getCommand("med2").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.data = new data(plugin, config_file);
        data.saveDefaultConfig(config_file);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("console goes sleeping");
        } else {
            player = (Player)  commandSender;
            switch (strings.length) {
                case 0:
                    if (meditators.containsKey(player.getName())) {
                        meditateOff(commandSender);
                    } else {
                        meditateOn(commandSender);
                    }
                    break;
                case 1:
                    if (strings[0].equals("on")) {
                        if (!(meditators.containsKey(player.getName()))) {
                            meditateOn(commandSender);
                        } else  {
                            commandSender.sendMessage("you are already meditating");
                        }
                    } else {
                        if (strings[0].equals("off")) {
                            if ((meditators.containsKey(player.getName()))) {
                                meditateOff(commandSender);
                            } else {
                                commandSender.sendMessage("you are already back");
                            }
                        } else {
                            commandSender.sendMessage("need a toggle on/off");
                        }
                    }
                    break;
            }
        }
        return true;
    }

    public void meditateOn(CommandSender commandSender) {

        if (player.getLevel() < 40) {
            commandSender.sendMessage(ChatColor.BOLD + "you are not wise enough");
            return;
        }

        commandSender.sendMessage(ChatColor.ITALIC + "you start to clear your mind");


        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            commandSender.sendMessage(ChatColor.ITALIC + "you take a deep breath");
        }, 20);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            commandSender.sendMessage(ChatColor.ITALIC + "you free your mind from your body");
        }, 50);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            startPlayerMeditate(player);
            meditators.put(player.getName(), player.getLocation());
            data.getConfig(config_file).set("meditators." + player.getName() + ".location.x", player.getLocation().getX());
            data.getConfig(config_file).set("meditators." + player.getName() + ".location.y", player.getLocation().getY());
            data.getConfig(config_file).set("meditators." + player.getName() + ".location.z", player.getLocation().getZ());
            data.saveConfig(config_file);
        }, 80);
    }

    public void startPlayerMeditate(Player player) {
        player.setInvisible(true);
        player.setInvulnerable(true);
        player.setAllowFlight(true);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.hidePlayer(plugin, player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(PlayerInteractEvent event) {
        if (meditators.containsKey(event.getPlayer().getName())) event.setCancelled(true);
    }

    /*
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (meditators.containsKey(event.getPlayer().getName())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockPlaceEvent event) {
        if (meditators.containsKey(event.getPlayer().getName())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(PlayerInteractEvent event) {
        if (meditators.containsKey(event.getPlayer().getName())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(PlayerArmorStandManipulateEvent event) {
        if (meditators.containsKey(event.getPlayer().getName())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(VehicleEnterEvent event) {
        if (meditators.containsKey(event.getEntered().getName())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(PlayerBedEnterEvent event) {
        if (meditators.containsKey(event.getPlayer().getName())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(PlayerBedLeaveEvent event) {
        if (meditators.containsKey(event.getPlayer().getName())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(PlayerBucketEmptyEvent event) {
        if (meditators.containsKey(event.getPlayer().getName())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(PlayerBucketEvent event) {
        if (meditators.containsKey(event.getPlayer().getName())) event.setCancelled(true);
    }
    */
    public void meditateOff(CommandSender sender) {
        player.teleport(meditators.get(player.getName()));
        player.setGameMode(GameMode.SURVIVAL);
        sender.sendMessage(ChatColor.ITALIC + "your body wraps around your mind again");
        meditators.remove(player.getName());
        data.getConfig(config_file).set("meditators." + player.getName(), null);
    }

    public static void onShutdown() {
        for (Map.Entry<String, Location> set : meditators.entrySet()) {
            if (set.getKey() == null || set.getValue() == null) {
                continue;
            } else {
                Player player = Bukkit.getPlayer(set.getKey());
                player.teleport(set.getValue());
                player.setGameMode(GameMode.SURVIVAL);
                meditators.remove(player.getName());

            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        if (this.data.getConfig(config_file).contains("meditators." + player.getName())) {
            Double x = data.getConfig(config_file).getDouble("meditators." + player.getName() + ".location.x");
            Double y = data.getConfig(config_file).getDouble("meditators." + player.getName() + ".location.y");
            Double z = data.getConfig(config_file).getDouble("meditators." + player.getName() + ".location.z");
            Location loc = new Location(player.getWorld(), x, y, z);
            player.teleport(loc);
            player.setGameMode(GameMode.SURVIVAL);
            data.getConfig(config_file).set("meditators." + player.getName(), null);
            data.saveConfig(config_file);
            meditators.remove(player.getName());
        } else return;
    }

    @EventHandler
    public void onLevelUp(PlayerLevelChangeEvent event) {
        if (event.getNewLevel() == 40) {
            event.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "you are now wise enough to meditate");
        }
    }
}
