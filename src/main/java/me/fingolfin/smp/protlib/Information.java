package me.fingolfin.smp.protlib;

import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.UUID;
import java.util.Date;

public class Information implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID uuid;
    private String name;
    private Date issuedDate;

    public Information(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName(){
        this.name = name;
        return name;
    }

    public void setIssuedDate(Date date) {
        this.issuedDate = date;
    }

    public Date getIssuedDate() {
        return this.issuedDate;
    }
}
