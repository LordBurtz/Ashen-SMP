package me.fingolfin.smp;

import me.fingolfin.smp.helloworld.helloworldcmd;
import me.fingolfin.smp.horse.horse_spwn_cmd;
import me.fingolfin.smp.meditation.meditation;
import me.fingolfin.smp.plotarmor.armor;
import me.fingolfin.smp.plotarmor.damageEvent;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    @Override
    public void onEnable() {
        new helloworldcmd(this);
        new horse_spwn_cmd(this);
        new armor(this);
        new damageEvent(this);
        new meditation(this);

    }

    @Override
    public void onDisable() {
        meditation.onShutdown();
    }
}
