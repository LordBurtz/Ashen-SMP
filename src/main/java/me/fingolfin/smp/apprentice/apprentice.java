package me.fingolfin.smp.apprentice;

import me.fingolfin.smp.data.data;
import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.Map;

public class apprentice implements Listener, CommandExecutor {
    public static final int MAX_MOBS = 75;
    public String file = "apprentice.yml";

    private static final Map<EntityType, Integer> army = new HashMap<>();
    private String target = "";
    private int mobs_atm = 0;
    private final main plugin;
    private boolean toggled;
    private final me.fingolfin.smp.data.data data;
    private String apprentice;

    public apprentice(main plugin) {
        this.plugin = plugin;
        plugin.getCommand("apprentice").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.data = new data(plugin, file);
        data.saveDefaultConfig(file);
        initializeArny();
        add2ArmyFromFile();
        getMobs();
        setApprentice();
    }

    private void setApprentice() {
        if (data.getConfig(file).contains("apprentice.name")) {
            apprentice = data.getConfig(file).getString("apprentice.name");
        } else {
            data.getConfig(file).set("apprentice.name", "Optinux");
        }
        data.saveConfig(file);
    }

    private void getMobs() {
        if (data.getConfig(file).contains("apprentice.mobsKilled")) {
            mobs_atm = data.getConfig(file).getInt("apprentice.mobsKilled");
        } else {
            data.getConfig(file).set("apprentice.mobsKilled", 0);
        }
        data.saveConfig(file);
    }

    private void initializeArny() {
        army.put(EntityType.ZOMBIE, 0);
        army.put(EntityType.SKELETON, 0);
    }

    public void add2ArmyFromFile() {
        for (Map.Entry<EntityType, Integer> set : army.entrySet()) {
            if (this.data.getConfig(file).contains("apprentice." + set.getKey())) {
                army.replace(set.getKey(), data.getConfig(file).getInt("apprentice." + set.getKey()));
            } else {
                army.replace(set.getKey(), 0);
                this.data.getConfig(file).set("apprentice." + set.getKey(), 0);
            }
        }
        data.saveConfig(file);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("console is gay");
        }
        if (!(commandSender.getName().equals(apprentice))) {
            commandSender.sendMessage(ChatColor.ITALIC  + "" + ChatColor.GRAY + "you are not le apprentice");
            return true;
        }
        switch (strings.length) {
            case 0:
                commandSender.sendMessage("you need more args");
                commandSender.sendMessage("summon to summon your army");
                commandSender.sendMessage("check to see how many of which mobtype you have");
                commandSender.sendMessage("target <name> to set a target");
                break;

            case 1:
                if (strings[0].equals("summon")) {
                    summon(commandSender);
                } else {
                    if (strings[0].equals("check")) {
                        check(commandSender);
                    } else  {
                        if (strings[0].equals("toggle")) {
                            toggle(commandSender);
                        }
                    }
                }
                break;

            case 2:
                switch (strings[0]) {
                    case "target":
                        target = Bukkit.getPlayer(strings[1]).getName();
                        commandSender.sendMessage(ChatColor.BOLD +
                                String.format("Target set to %s", target));
                        break;
                }
        }

        return true;
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        if (!toggled) return;
        if (event.getEntity().getKiller() == null) return;
        if (!(event.getEntity().getKiller().getName().equals(apprentice))) return;
        EntityType type = event.getEntityType();
        if (mobs_atm > MAX_MOBS) {
            event.getEntity().getKiller().sendMessage(
                    ChatColor.GOLD + "MAXIMUM AMOUNT OF MOBS REACHED");
            return;
        }
        if (army.containsKey(type)) {
            army.replace(type, army.get(type) + 1);
            event.getEntity().getKiller().sendMessage(String.format("you killed a %s", type.name()));
            mobs_atm++;
            data.getConfig(file).set("apprentice." + type, data.getConfig(file).getInt("apprentice." + type) + 1);
            data.getConfig(file).set("apprentice.mobsKilled", mobs_atm);
            data.saveConfig(file);
        }
    }

    public void summon(CommandSender sender) {
        Location loc = ((Player) sender).getLocation();
        World world = ((Player) sender).getWorld();
        for (Map.Entry<EntityType, Integer> set : army.entrySet()) {
            for (int i = 0; i < set.getValue(); i++) {
                Mob entity = (Mob) world.spawnEntity(loc, set.getKey());
                Bukkit.getScheduler().runTaskLater(plugin, () ->  {
                    entity.setTarget(Bukkit.getPlayer(target));
                }, 20);
            }
            army.replace(set.getKey(), army.get(set.getKey()), 0);
            data.getConfig(file).set("apprentice." + set.getKey().toString(), 0);
            mobs_atm = 0;
            data.getConfig(file).set("apprentice.mobsKilled", 0);
            data.saveConfig(file);
        }
    }

    public void check(CommandSender sender) {
        for (Map.Entry<EntityType, Integer> set : army.entrySet()) {
            sender.sendMessage(String.format("you have %d of type %s", set.getValue(), set.getKey()));
        }
    }

    public void toggle(CommandSender sender) {
        toggled = !this.toggled;
        sender.sendMessage(String.format("counting mobs toggled to %b", toggled));
    }
}
