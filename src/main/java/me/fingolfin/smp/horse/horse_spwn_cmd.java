package me.fingolfin.smp.horse;

import me.fingolfin.smp.effects.effectsMercenarie;
import me.fingolfin.smp.main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class horse_spwn_cmd  implements CommandExecutor {

    public static String mercenary = effectsMercenarie.mercenary;

    private final main plugin;
    private Player player;

    public horse_spwn_cmd(main plugin) {
        this.plugin = plugin;
        plugin.getCommand("3ac").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("dum shyte only player lol");
        } else {
            if (commandSender.getName().equals("_Ecl1pse_") || commandSender.isOp()) {

                player = (Player) commandSender;
                Horse horse = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
                horse.setCustomName("Moondancer");
                horse.setJumpStrength(1.2);
                horse.setTamed(true);
                horse.setOwner(player);
                horse.setColor(Horse.Color.WHITE);
                horse.setMaxHealth(200);
                horse.setAdult();
                horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999, 1));
                horse.setOwner(player);
                horse.teleport(player);
                System.out.println("horse created func end");
                commandSender.sendMessage("horse created func end");
            } else {
                commandSender.sendMessage(" na na na Finger weg! custom command pour gefallener engel");
            }
        }
        return true;
    }
}
