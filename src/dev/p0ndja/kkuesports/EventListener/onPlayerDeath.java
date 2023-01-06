package dev.p0ndja.kkuesports.EventListener;

import java.io.File;
import java.io.IOException;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import dev.p0ndja.kkuesports.Global;
import dev.p0ndja.kkuesports.pluginMain;

public class onPlayerDeath implements Listener {
	
	pluginMain pl;
	public onPlayerDeath(pluginMain pl) {
		this.pl = pl;
	}
	
	File f1 = new File(Global.pluginDir, File.separator + "killstats.yml");
	FileConfiguration killData = YamlConfiguration.loadConfiguration(f1);
	
	@EventHandler
    public void playerDeath(PlayerDeathEvent event) {
		if (event.getEntity().getKiller() instanceof Player) {
			Player killer = event.getEntity().getKiller();
			Team tk = killer.getScoreboard().getPlayerTeam(killer);
			
			String format = "killstats." + tk.getName() + "." + killer.getName();
			try {
				killData.set(format, killData.getInt(format) + 1);
				killData.save(f1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		died(event.getEntity());
	}
	
	public static void died(Player died) {
		if (!died.isOp() && !died.getPlayer().getScoreboard().getPlayerTeam(died).getName().toLowerCase().equalsIgnoreCase("kku")) {
			Bukkit.getBanList(Type.NAME).addBan(died.getName(), "[UHC] you're died", null, null);
			died.kickPlayer("[UHC] you're died");
		}
	}

}
