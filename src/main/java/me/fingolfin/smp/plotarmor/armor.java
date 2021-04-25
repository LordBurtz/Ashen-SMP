package me.fingolfin.smp.plotarmor;

import me.fingolfin.smp.data.data;
import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.logging.Level;

public class armor implements CommandExecutor {

    private final main plugin;
    private Player player;

    public static String hero;

    public armor(main plugin) {
        this.plugin = plugin;
        plugin.getCommand("plotarmor").setExecutor(this);
        setHero();
        Bukkit.getServer().getLogger().log(Level.INFO, String.format("[SMP] The Hero is %s", hero));
    }

    public void setHero() {
        data data = new data(plugin, "config.yml");
        data.saveDefaultConfig("config.yml");
        if (data.getConfig("config.yml").contains("hero.name")) {
            hero = data.getConfig("config.yml").getString("hero.name");
        } else {
            data.getConfig("config.yml").set("hero.name", "XFeam");
            hero = "XFeam";
        }
        data.saveConfig("config.yml");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(" dum shyte only players lol");
        } else {
            if (commandSender.getName().equals(hero) || commandSender.isOp()) {
                player = (Player) commandSender;

                ItemStack helmet = armor_piece("helmet");
                ItemStack chestplate = armor_piece("chest");
                ItemStack leggins = armor_piece("leggins");
                ItemStack boots = armor_piece("boots");

                player.getInventory().setHelmet(helmet);
                player.getInventory().setChestplate(chestplate);
                player.getInventory().setLeggings(leggins);
                player.getInventory().setBoots(boots);
            } else {
                commandSender.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "you are not le hero :sad:");
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
