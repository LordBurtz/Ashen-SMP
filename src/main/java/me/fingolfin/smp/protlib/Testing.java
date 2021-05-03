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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Testing implements Listener, CommandExecutor {
    private main plugin;
    private ProtocolManager pmng;

    public static String MOTD;
    public final int COOLDOWN = 5;
    public HashMap<Player, Long> jumpers = new HashMap<>();
    public List<String> muted = new ArrayList<>();

    public Testing(main plugin) {
        this.plugin = plugin;
        plugin.getCommand("7ac").setExecutor(this);
        setMOTD();
        pmng = ProtocolLibrary.getProtocolManager();
        Bukkit.getPluginManager().registerEvents(this, plugin);
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
        Bukkit.getLogger().log(Level.INFO, String.format("[SMP] The MOTD is %s", MOTD));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return true;
        String name = commandSender.getName();
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
}
