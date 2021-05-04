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
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
    public final int COOLDOWN = 5;
    public HashMap<Player, Long> jumpers = new HashMap<>();
    public List<String> muted = new ArrayList<>();

    public Testing(main plugin) {
        this.plugin = plugin;
        plugin.getCommand("7ac").setExecutor(this);
        setMOTD();
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

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerFly(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (jumpers.containsKey(player)) {
            if ((System.currentTimeMillis() - jumpers.get(event.getPlayer())) / 1000 < COOLDOWN) {
                player.sendMessage(
                        ChatColor.GOLD +
                                String.format("You have to wait %.1f secs to use that again",
                                        ((float) COOLDOWN - ((float) (System.currentTimeMillis() - jumpers.get(event.getPlayer()))) / 1000)));
                event.setCancelled(true);
                return;
            }
            if (!(player.getGameMode().equals(GameMode.CREATIVE))) {
                event.setCancelled(true);
                player.setFlying(false);
                player.setAllowFlight(false);
                Vector vector = player.getLocation().getDirection();
                player.setVelocity(vector.setY(1.1D));
                player.playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 15);
                bossBar(player);
                jumpers.put(player, System.currentTimeMillis());
            }
        } else {
            if (!(player.getGameMode().equals(GameMode.CREATIVE))) {
                event.setCancelled(true);
                player.setFlying(false);
                player.setAllowFlight(false);
                Vector vector = player.getLocation().getDirection();
                player.setVelocity(vector.setY(1.1D));
                player.playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 15);
                bossBar(player);
                jumpers.put(player, System.currentTimeMillis());
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) && !(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR))) {
                player.setAllowFlight(true);
            }
    }

    public void bossBar(Player player, boolean bool) {
        BossBar bar = Bukkit.createBossBar(ChatColor.GOLD + "Cooldown Double Jump", BarColor.YELLOW, BarStyle.SOLID);
        bar.addPlayer(player);
        bar.setProgress(1D);
        Bukkit.getServer().getScheduler().runTaskLater(plugin,() -> {
            bar.setProgress(0.8D);
        } ,20);
        Bukkit.getServer().getScheduler().runTaskLater(plugin,() -> {
            bar.setProgress(0.6D);
        } ,40);
        Bukkit.getServer().getScheduler().runTaskLater(plugin,() -> {
            bar.setProgress(0.4D);
        } ,60);
        Bukkit.getServer().getScheduler().runTaskLater(plugin,() -> {
            bar.setProgress(0.2D);
        } ,80);
        Bukkit.getServer().getScheduler().runTaskLater(plugin,() -> {
            bar.setProgress(0.1D);
        } ,90);
        Bukkit.getServer().getScheduler().runTaskLater(plugin,() -> {
            bar.setProgress(0.0D);
            bar.removeAll();
        } ,100);
        return;
    }

    public void bossBar(Player player) {
        BossBar bar = Bukkit.createBossBar(ChatColor.DARK_PURPLE + "Cooldown Double Jump", BarColor.PURPLE, BarStyle.SEGMENTED_20);
        bar.addPlayer(player);
        bar.setProgress(1D);
        for (double i = 0; i <= 1; i = i + 0.05) {
            double finalI = i;
            Bukkit.getServer().getScheduler().runTaskLater(plugin,() -> {
                bar.setProgress(1.0D - finalI);
            } , (long) (finalI*100));
        }
        Bukkit.getServer().getScheduler().runTaskLater(plugin,() -> {
            bar.setProgress(0.0D);
            bar.removeAll();
        } ,100);
    }

    public void setMOTD() {
        data data = new data(plugin, "config.yml");
        data.saveDefaultConfig("config.yml");
        if (data.getConfig("config.yml").contains("MOTD")) {
            MOTD = data.getConfig("config.yml").getString("MOTD");
        } else {
            data.getConfig("config.yml").set("MOTD", "THIS IS A TEST SERVER");
            MOTD = "THIS IS A TEST SERVER";
        }
        data.saveConfig("config.yml");
        MOTD = ChatColor.RED + "" + ChatColor.BOLD + MOTD;
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        event.setMaxPlayers(-1);
        event.setMotd(MOTD);
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
}
