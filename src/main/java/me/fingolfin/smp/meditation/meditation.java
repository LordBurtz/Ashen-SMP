package me.fingolfin.smp.meditation;

import me.fingolfin.smp.data.data;
import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class meditation implements CommandExecutor {

    private main plugin;
    private Player player;
    private Location loc;
    //private static List<String> meditators = new ArrayList<String>();
    private static HashMap<String, Location> meditators = new HashMap<>();

    public meditation(main plugin) {
        this.plugin =  plugin;
        plugin.getCommand("meditate").setExecutor(this);
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

        data.loadData(plugin, "test.yml");


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
            player.setGameMode(GameMode.SPECTATOR);
            meditators.put(player.getName(), player.getLocation());
        }, 80);
    }

    public void meditateOff(CommandSender sender) {
        player.teleport(meditators.get(player.getName()));
        player.setGameMode(GameMode.SURVIVAL);
        sender.sendMessage(ChatColor.ITALIC + "your body wraps around your mind again");
        meditators.remove(player.getName());
    }

    public static void onShutdown() {
       for (Map.Entry<String, Location> set : meditators.entrySet()) {
           Player player = Bukkit.getPlayer(set.getKey());
           player.teleport(set.getValue());
           player.setGameMode(GameMode.SURVIVAL);
           meditators.remove(player.getName());
       }
    }
}
