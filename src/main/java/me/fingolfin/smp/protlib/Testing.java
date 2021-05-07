package me.fingolfin.smp.protlib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.fingolfin.smp.data.data;
import me.fingolfin.smp.main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.TileState;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.logging.Level;

public class Testing implements Listener, CommandExecutor {
    private main plugin;
    private ProtocolManager pmng;

    public Inventory inventory;
    public static String MOTD;
    public List<String> muted = new ArrayList<>();

    public Testing(main plugin) {
        this.plugin = plugin;
        plugin.getCommand("7ac").setExecutor(this);
        pmng = ProtocolLibrary.getProtocolManager();
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(this, plugin);
        pmng.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (!(muted.contains(event.getPlayer().getName()))) return;
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();
                if (packet.getStrings().read(0).equals("/7ac") || packet.getStrings().read(0).equals("/mute")) return;
                event.setCancelled(true);
            }
        });
        createInv();
        Bukkit.getLogger().log(Level.INFO, String.format("[SMP] The MOTD is %s", MOTD));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return true;
        String name = commandSender.getName();

            commandSender.sendMessage(Bukkit.getServer().getWorld("world").getName());
            commandSender.sendMessage("done");
            //return true;

        if (1 == 1) return true;
        if (strings[0].equals("gui")) {
            ((Player) commandSender).openInventory(inventory);
            return true;
        }

        if (strings[0].equals("backpack")) {
            Player player = (((Player) commandSender).getPlayer());
            ItemStack item = new ItemStack(Material.CHEST);
            ItemMeta meta = item.getItemMeta();

            PersistentDataContainer container = meta.getPersistentDataContainer();

            return true;
        }

        if (strings[0].equals("pass")) {
            Player player = (Player) commandSender;
            Information info = new Information((player.getPlayer()));
            info.setIssuedDate(new Date());
            player.getInventory().addItem(getID(info));
            return true;
        }

        if (muted.contains(name)) {
            muted.remove(name);
            commandSender.sendMessage(String.format("toggled to %b", false));
        } else {
            muted.add(name);
            commandSender.sendMessage(String.format("toggled to %b", true));
        }
        System.out.println(muted);
        PacketContainer fakeExplosion = pmng.createPacket(PacketType.Play.Server.EXPLOSION);
        Player player = (Player) commandSender;
        fakeExplosion.getDoubles().
                write(0, player.getLocation().getX()).
                write(0, player.getLocation().getY()).
                write(0, player.getLocation().getZ());
        fakeExplosion.getFloat().write(0, 3.0F);
        try {
            pmng.sendServerPacket(player, fakeExplosion);
        } catch (Exception e) {
            throw new RuntimeException(
                    "cannot send stuff lol", e
            );
        }
        return true;
    }

    public void createInv() {
        inventory = Bukkit.createInventory(null, 9, ChatColor.GOLD + "do the testing");
        ItemStack item = new ItemStack(Material.BLUE_WOOL);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.DARK_BLUE + "blue");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Click to join a team");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inventory.setItem(0, item);

        item.setType(Material.BARRIER);
        meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.WHITE + "close");
        lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Close le GUI");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inventory.setItem(8, item);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(inventory)) return;
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getItemMeta() == null) return;
        if (event.getCurrentItem().getItemMeta().getLore() == null) return;

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() == 0) {
            player.getInventory().addItem( new ItemStack(Material.ACACIA_BOAT));
        }
        if (event.getSlot() == 8) {
            player.closeInventory();
        }
        return;
    }


    @EventHandler
    public void onPlace (BlockPlaceEvent event) {
        if (!event.getPlayer().isSneaking()) return;
        if (!event.getBlock().getType().equals(Material.CHEST)) return;
        if (!(event.getBlock().getState() instanceof TileState)) return;

        TileState state = (TileState) event.getBlock().getState();
        PersistentDataContainer container = state.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "private-chest");

        container.set(key, PersistentDataType.STRING, event.getPlayer().getUniqueId().toString());
        state.update();

        event.getPlayer().sendMessage("chest locked");
    }

    @EventHandler
    public void onAirRightClickPaper(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getInventory().getItemInMainHand().getType().equals(Material.PAPER)) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if (!(meta.getDisplayName().equals(ChatColor.DARK_GRAY + player.getName() + "'s ID"))) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "license");
        Information info = container.get(key, new InformationDataType());
        player.sendMessage(info.getName());
        player.sendMessage(info.getUuid().toString());
        player.sendMessage(info.getIssuedDate().toString());
    }


    @EventHandler
    public void onChestOpen(PlayerInteractEvent event) {
        if (!event.hasBlock()) return;
        if (!event.getClickedBlock().getType().equals(Material.CHEST)) return;
        if (!(event.getClickedBlock().getState() instanceof TileState)) return;

        TileState state = (TileState) event.getClickedBlock().getState();
        PersistentDataContainer container = state.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "private-chest");

        if (!container.has(key, PersistentDataType.STRING)) return;
        if (event.getPlayer().getUniqueId().toString().equalsIgnoreCase(
                container.get(key, PersistentDataType.STRING)
        )) return; else {
            event.getPlayer().sendMessage(ChatColor.GRAY + "this chest is locked!");
            event.setCancelled(true);
        }
    }

    public ItemStack getID(Information information) {
        ItemStack id = new ItemStack(Material.PAPER);
        ItemMeta meta = id.getItemMeta();

        meta.setDisplayName(ChatColor.DARK_GRAY + information.getName() + "'s ID");

        NamespacedKey key = new NamespacedKey(plugin, "license");

        meta.getPersistentDataContainer().set(key, new InformationDataType(), information);

        id.setItemMeta(meta);
        return id;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onEnderEyeThrow(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() == null || event.getItem().getType() != Material.ENDER_EYE) return;
        if ((event.getAction().equals(Action.RIGHT_CLICK_AIR))) return;
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;
        if (event.getItem().getType().equals(Material.ENDER_EYE)) {
            event.setCancelled(true);
            event.setUseItemInHand(Event.Result.DENY);
            //TODO: integrate with Terra generation
            Location target = player.getLocation();
            Bukkit.getServer().getWorld("world").getSpawnLocation();
        }
    }
}
