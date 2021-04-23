package me.fingolfin.smp.starsword;

import me.fingolfin.smp.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class starsword implements Listener {
    private final main plugin;

    public starsword(main plugin) {
        this.plugin = plugin;
        starsword();
    }

    public void starsword() {
        ItemStack item = sword();
        NamespacedKey key = new NamespacedKey(plugin, "sword_of_the_stars");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape(" E ", " E ", " S ");
        recipe.setIngredient('E', Material.EMERALD_BLOCK);
        recipe.setIngredient('S', Material.STICK);

        Bukkit.addRecipe(recipe);
    }

    public static ItemStack sword()  {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_AQUA + "Sword of the stars");
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 32727);
        item.setDurability((short) 1560);
        return item;
    }
}
