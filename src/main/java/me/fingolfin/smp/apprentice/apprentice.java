package me.fingolfin.smp.apprentice;

import me.fingolfin.smp.data.data;
import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class apprentice implements Listener, CommandExecutor {
    public static final int MAX_MOBS = 75;
    public String file = "apprentice.yml";

    private static Map<EntityType, Integer> army = new HashMap<>();
    private String target = "";
    private int mobs_atm = 0;
    private main plugin;
    private boolean toggled;
    private me.fingolfin.smp.data.data data;

    public apprentice(main plugin) {
        this.plugin = plugin;
        plugin.getCommand("necro").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.data = new data(plugin, file);
        data.saveDefaultConfig(file);
        //TODO: Make Hashmap saveable !IMPORTANT!
        initializeArny();
        add2ArmyFromFile();
        getMobs();
    }

    private void getMobs() {
        if (data.getConfig(file).contains("necro.mobsKilled")) {
            mobs_atm = data.getConfig(file).getInt("necro.mobsKilled");
        } else {
            data.getConfig(file).set("necro.mobsKilled", 0);
        }
    }

    private void initializeArny() {
        army.put(EntityType.ZOMBIE, 0);
        army.put(EntityType.SKELETON, 0);
    }

    public void add2ArmyFromFile() {
        for (Map.Entry<EntityType, Integer> set : army.entrySet()) {
            if (this.data.getConfig(file).contains("apprentice." + set.getKey())) {
                army.replace(set.getKey(), data.getConfig(file).getInt("necro." + set.getKey()))
            }
        }
    }
}
