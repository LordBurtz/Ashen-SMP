package me.fingolfin.smp.protlib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.fingolfin.smp.main;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Testing implements Listener, CommandExecutor {
    private main plugin;
    private ProtocolManager pmng;

    public final int COOLDOWN = 5;
    public HashMap<Player, Long> jumpers = new HashMap<>();
    public List<String> muted = new ArrayList<>();

    public Testing(main plugin) {
        this.plugin = plugin;
        plugin.getCommand("7ac").setExecutor(this);
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

    @EventHandler
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
}
