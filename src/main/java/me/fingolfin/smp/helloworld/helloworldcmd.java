package me.fingolfin.smp.helloworld;

import me.fingolfin.smp.main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class helloworldcmd implements CommandExecutor {

    private main plugin;

    public helloworldcmd(main plugin) {
        this.plugin = plugin;
        plugin.getCommand("hello").setExecutor(this);
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only player should execute dad shyte");
            return true;
        }

        Player p = (Player) sender;

        if (p.hasPermission("hello.use")) {
            p.sendMessage("yo bro cool you got some nice ass permissions");
            p.sendMessage(String.format("yo %s's adress is %s", p.getDisplayName(), p.getAddress()));
            return true;
        } else {
            p.sendMessage("Nope yer permissions not cool");
        }
        return false;
    }
}
