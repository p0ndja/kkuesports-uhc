package dev.p0ndja.kkuesports.EventListener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import dev.p0ndja.kkuesports.pluginMain;

public class onPlayerPvP implements Listener {
	
	public static boolean peacefulMode = false;
	
	pluginMain pl;
	public onPlayerPvP(pluginMain pl) {
		this.pl = pl;
	}
	
    @EventHandler
    public void onTestEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player){
            if (event.getEntity() instanceof Player) {
            	if (peacefulMode == true) {
            		event.setCancelled(true);
            	}
            }
        }
    }
}
