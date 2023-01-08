package dev.p0ndja.kkuesports.EventListener;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.BanList.Type;
import org.bukkit.command.CommandException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import dev.p0ndja.kkuesports.Function;
import dev.p0ndja.kkuesports.Global;
import dev.p0ndja.kkuesports.Prefix;
import dev.p0ndja.kkuesports.Sound;
import dev.p0ndja.kkuesports.Timer;
import dev.p0ndja.kkuesports.pluginMain;

public class onPlayerConnection implements Listener {
	pluginMain pl;

	public onPlayerConnection(pluginMain pl) {
		this.pl = pl;
	}
	
	public File killstatsFile = new File(Global.pluginDir + File.separator + "killstats.yml");
	public FileConfiguration killstatsData = YamlConfiguration.loadConfiguration(killstatsFile);
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getDisplayName();
		if (player.getScoreboard().getPlayerTeam(player) != null) {
			Team t = player.getScoreboard().getPlayerTeam(player);
			//Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user "+player.getName()+" group set "+t.getName());
			playerName = t.getPrefix() + ChatColor.RESET + player.getName();
			player.setDisplayName(playerName);
		} else {
			boolean isFound = false;
			for(String tt : Global.teamList) {
				try {
					File teamuhcFile = new File(Global.pluginDir + File.separator + "Team/" + tt.toUpperCase() + ".yml");
					FileConfiguration teamuhcData = YamlConfiguration.loadConfiguration(teamuhcFile);
					if (teamuhcData.get("member").toString().contains(player.getUniqueId().toString())) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join "+tt+" "+player.getName());
						//Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user "+player.getName()+" group set " + tt);
						isFound = true;
						playerName = player.getScoreboard().getPlayerTeam(player).getPrefix() + ChatColor.RESET + player.getName();
						player.setDisplayName(playerName);
						break;
					}
				} catch (CommandException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!isFound) {
				try {
					File teamuhcFile = new File(Global.pluginDir + File.separator + "Team/KKU.yml");
					FileConfiguration teamuhcData = YamlConfiguration.loadConfiguration(teamuhcFile);					
					if (teamuhcData.get("member").toString().contains(player.getUniqueId().toString())) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join KKU "+player.getName());
						//Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user "+player.getName()+" group set kku");
						isFound = true;
						
						playerName = player.getScoreboard().getPlayerTeam(player).getPrefix() + ChatColor.RESET + player.getName();
						player.setDisplayName(playerName);
					}
				} catch (CommandException e) {
					e.printStackTrace();
				}
			}
		}
		event.setJoinMessage(ChatColor.GREEN + "Join> " + ChatColor.RESET + playerName);
		Function.egg(1);
		if (!player.getScoreboard().getPlayerTeam(player).getName().toLowerCase().equalsIgnoreCase("kku")) {
			pl.reloadConfig();
			long logoutTime = pl.getConfig().getLong("connection."+player.getUniqueId()+".logout");
			long loginTime = pl.getConfig().getLong("connection."+player.getUniqueId()+".login");
			if (Timer.skipJoinAutoBan == true) {
				Bukkit.broadcastMessage("Skip auto-ban on joining for " + playerName);
			} else if (Timer.time > 1800 && (System.currentTimeMillis() - logoutTime > 60*1000)) { //60 s in ms
				Bukkit.getBanList(Type.NAME).addBan(player.getName(), "[UHC] you're died, due to lost connection more than 60 seconds.", null, null);
				player.kickPlayer("[UHC] you're died, due to lost connection more than 60 seconds.");
				Bukkit.broadcastMessage(player.getDisplayName() + " was died, due to lost connection more than 60 seconds.");
			} else if (Timer.time > 0 && logoutTime < Timer.startServerTime) { //60 s in ms
				Bukkit.getBanList(Type.NAME).addBan(player.getName(), "[UHC] you're died, due to joining the match late.", null, null);
				player.kickPlayer("[UHC] you're died, due to joining the match late.");
				Bukkit.broadcastMessage(player.getDisplayName() + " was died, due to joining the match late.");
			}
		}
		pl.getConfig().set("connection."+event.getPlayer().getUniqueId()+".login", System.currentTimeMillis());
		pl.saveConfig();
	}

	@EventHandler
	public void onPlayerLeft(PlayerQuitEvent event) {
		event.setQuitMessage(ChatColor.RED + "Left> " + ChatColor.RESET + event.getPlayer().getDisplayName());
		pl.getConfig().set("connection."+event.getPlayer().getUniqueId()+".logout", System.currentTimeMillis());
		pl.saveConfig();
		Function.egg(1);
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		event.setFormat(event.getPlayer().getDisplayName() + ChatColor.WHITE + " " + event.getMessage());
		
	}
}
