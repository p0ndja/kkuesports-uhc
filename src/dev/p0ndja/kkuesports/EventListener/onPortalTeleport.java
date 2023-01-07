package dev.p0ndja.kkuesports.EventListener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import dev.p0ndja.kkuesports.pluginMain;

public class onPortalTeleport implements Listener {
	
	public static boolean allowPortal = true;
	
	pluginMain pl;
	public onPortalTeleport(pluginMain pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void portal(PlayerPortalEvent event) {
		if (allowPortal == false)
			event.setCancelled(true);
	}
}
