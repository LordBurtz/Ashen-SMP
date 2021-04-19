package me.fingolfin.smp;

import me.fingolfin.smp.helloworld.helloworldcmd;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    @Override
    public void onEnable() {
        new helloworldcmd(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
