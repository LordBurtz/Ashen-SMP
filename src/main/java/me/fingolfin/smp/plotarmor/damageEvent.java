package me.fingolfin.smp.plotarmor;

import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;

import java.util.ArrayList;
import java.util.List;

public class damageEvent implements Listener {

    public String hero = armor.hero;

    private final main plugin;
    private static final List<EntityDamageEvent.DamageCause> damage_types = new ArrayList<>();

    public damageEvent(main plugin) {
        this.plugin = plugin;
        addDamageTypes();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity().getName().equals(hero))) return;
        Player player = (Player) event.getEntity();
        EntityEquipment inv = player.getEquipment();
        try {
            if (
                    inv.getHelmet().getItemMeta().hasLore() &&
                    inv.getChestplate().getItemMeta().hasLore() &&
                    inv.getLeggings().getItemMeta().hasLore() &&
                    inv.getBoots().getItemMeta().hasLore()) {

                if (damage_types.contains(event.getCause())) {
                    event.setCancelled(true);
                }
            }
        } catch (java.lang.NullPointerException exception) {}
    }

    public static void addDamageTypes() {
        //AIR ELEMENT
        damage_types.add(EntityDamageEvent.DamageCause.FALL);
        //WATER ELEMENT
        damage_types.add(EntityDamageEvent.DamageCause.DROWNING);
        //FIRE ELEMENT
        damage_types.add(EntityDamageEvent.DamageCause.HOT_FLOOR);
        damage_types.add(EntityDamageEvent.DamageCause.FIRE_TICK);
        damage_types.add(EntityDamageEvent.DamageCause.FIRE);
        damage_types.add(EntityDamageEvent.DamageCause.LAVA);
        //EARTH ELEMENT
        damage_types.add(EntityDamageEvent.DamageCause.SUFFOCATION);
        damage_types.add(EntityDamageEvent.DamageCause.FALLING_BLOCK);
        //MAGIC/AETHER ELEMENT
        damage_types.add(EntityDamageEvent.DamageCause.WITHER);
        damage_types.add(EntityDamageEvent.DamageCause.VOID);
        damage_types.add(EntityDamageEvent.DamageCause.SUICIDE);
    }
}
