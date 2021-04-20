package me.fingolfin.smp.necro;

import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.*;

public class necromancer implements CommandExecutor, Listener {
    public static List<String> mob_types = new ArrayList<>();
    //TODO: besseres upper limit
    public static final int MAX_MOBS = 75;
    public static Map<EntityType, Integer> army = new HashMap<> ();
    public String target = "";
    public int mobs_atm = 0;

    private main plugin;

    public necromancer(main plugin) {
        this.plugin = plugin;
        plugin.getCommand("necro").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        //TODO: Make Hashmap saveable !IMPORTANT!
        initializeArny();
    }

    private void initializeArny() {
        army.put(EntityType.ZOMBIE, 0);
        army.put(EntityType.SKELETON, 0);
        army.put(EntityType.WITHER_SKELETON, 0);
        army.put(EntityType.WITHER, 0);
        army.put(EntityType.SKELETON_HORSE, 0);
        army.put(EntityType.STRAY, 0);
        army.put(EntityType.HUSK, 0);
        army.put(EntityType.ZOMBIFIED_PIGLIN, 0);
        army.put(EntityType.ZOGLIN, 0);

    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("console is gay");
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
                    if (strings.equals("check")) {
                        check(commandSender);
                    }
                }
                break;

            case 2:
                switch (strings[0]) {
                    case "target":
                        target = Bukkit.getPlayer(strings[1]).getName();
                        commandSender.sendMessage(ChatColor.BOLD +
                                String.format("the attack target is {}", target));
                        break;
                }
        }

        return true;
    }

            //TODO: add type to mob_types if not there
            //TODO: add return command to check for the mobs present
            //TODO: add mob spawn
            //TODO: add set target
            //TODO: do necromancer prefix (/necro??)

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        if (!(event.getEntity().getKiller().getName().equals("MINION912") || event.getEntity().getKiller().getName().equals("Fingolf1n"))) return;
        EntityType type = event.getEntityType();
        if (mobs_atm > MAX_MOBS) return;
        if (army.containsKey(type)) {
            army.replace(type, army.get(type) + 1);
            event.getEntity().getKiller().sendMessage(String.format("you killed a %s", type.name()));
            mobs_atm++;
        }
    }

    public void summon(CommandSender sender) {
        return;
    }

    public void check(CommandSender sender) {
        for (Map.Entry<EntityType, Integer> set : army.entrySet()) {
            sender.sendMessage(String.format("you have "));
        }
    }
}
