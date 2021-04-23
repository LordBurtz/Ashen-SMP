package me.fingolfin.smp;

import me.fingolfin.smp.apprentice.apprentice;
import me.fingolfin.smp.effects.effectsMayor;
import me.fingolfin.smp.effects.effectsMercenarie;
import me.fingolfin.smp.effects.effectsRoyal;
import me.fingolfin.smp.helloworld.helloworldcmd;
import me.fingolfin.smp.horse.horse_spwn_cmd;
import me.fingolfin.smp.meditation.meditation;
import me.fingolfin.smp.necro.necromancer;
import me.fingolfin.smp.noSharp.noSharp;
import me.fingolfin.smp.ojisan.XPgain;
import me.fingolfin.smp.plotarmor.armor;
import me.fingolfin.smp.plotarmor.damageEvent;
import me.fingolfin.smp.specialItem.Ramen;
import me.fingolfin.smp.starsword.CraftEvent;
import me.fingolfin.smp.starsword.starsword;
import me.fingolfin.smp.wizard.wizard;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    @Override
    public void onEnable() {
        new helloworldcmd(this);
        new horse_spwn_cmd(this);
        new armor(this);
        new damageEvent(this);
        new meditation(this);
        new necromancer(this);
        new apprentice(this);
        new noSharp(this);
        new effectsMercenarie(this);
        new effectsRoyal(this);
        new effectsMayor(this);
        new wizard(this);
        new Ramen(this);
        new XPgain(this);
        new starsword(this);
        new CraftEvent(this);
    }

    @Override
    public void onDisable() {
        meditation.onShutdown();
    }
}
