package me.fingolfin.smp.protlib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.fingolfin.smp.main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.sql.Wrapper;

public class Testing implements Listener, CommandExecutor {
    private main plugin;
    private ProtocolManager pmng;
    private boolean mute;

    public Testing(main plugin) {
        this.plugin = plugin;
        plugin.getCommand("7ac").setExecutor(this);
        pmng = ProtocolLibrary.getProtocolManager();
        pmng.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (!mute) return;
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
        mute = !mute;
        commandSender.sendMessage(String.format("toggled to %b", mute));
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
                    "cannot send stuff lol", c
            );
        }
        return true;
    }
}
