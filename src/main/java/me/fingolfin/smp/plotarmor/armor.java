package me.fingolfin.smp.plotarmor;

import me.fingolfin.smp.main;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class armor implements CommandExecutor {

    private final main plugin;
    private Player player;

    public armor(main plugin) {
        this.plugin = plugin;
        plugin.getCommand("plotarmor").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(" dum shyte only players lol");
        } else {
            if (commandSender.getName().equals("XFeam") || commandSender.isOp()) {
                player = (Player) commandSender;

                ItemStack helmet = armor_piece("helmet");
                ItemStack chestplate = armor_piece("chest");
                ItemStack leggins = armor_piece("leggins");
                ItemStack boots = armor_piece("boots");

                player.getInventory().setHelmet(helmet);
                player.getInventory().setChestplate(chestplate);
                player.getInventory().setLeggings(leggins);
                player.getInventory().setBoots(boots);
            }
        }
        return true;
    }

    public static ItemStack armor_piece(String piece) {

        ItemStack item;

        switch (piece) {
            case "helmet":
                item = new ItemStack(Material.GOLDEN_HELMET);
                break;
            case "chest":
                item = new ItemStack(Material.GOLDEN_CHESTPLATE);
                break;
            case "leggins":
                item = new ItemStack(Material.GOLDEN_LEGGINGS);
                break;
            case "boots":
                item = new ItemStack(Material.GOLDEN_BOOTS);
                break;
            default:
                item = new ItemStack(Material.GOLDEN_HELMET);
                break;
        }

        int max = 32000;
        ItemMeta itemmeta = item.getItemMeta();

        itemmeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
        itemmeta.addEnchant(Enchantment.OXYGEN, 2, true);
        itemmeta.addEnchant(Enchantment.WATER_WORKER, 2, true);
        itemmeta.addEnchant(Enchantment.DURABILITY, max, false);
        itemmeta.addEnchant(Enchantment.DEPTH_STRIDER, 2, true);

        itemmeta.setLore(Collections.singletonList("plot-relevant"));
        itemmeta.setDisplayName("le Plotarmor");
        itemmeta.setUnbreakable(true);
        item.setItemMeta(itemmeta);
        return item;
    }


}
