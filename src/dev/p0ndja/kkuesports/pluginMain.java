package dev.p0ndja.kkuesports;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.WorldCreator;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.p0ndja.kkuesports.API.ActionBarAPI_api;
import dev.p0ndja.kkuesports.EventListener.onPlayerConnection;
import dev.p0ndja.kkuesports.EventListener.onPlayerDeath;
import dev.p0ndja.kkuesports.EventListener.onPlayerPvP;
import dev.p0ndja.kkuesports.EventListener.onPortalTeleport;

public class pluginMain extends JavaPlugin implements Listener {
	
	File f1 = new File(Global.pluginDir, File.separator + "killstats.yml");
	FileConfiguration killData = YamlConfiguration.loadConfiguration(f1);
	
	public void onDisable() {
		Bukkit.broadcastMessage(Prefix.server + getDescription().getName() + " System: " + ChatColor.RED + ChatColor.BOLD + "Disable");
		for (Player player1 : Bukkit.getOnlinePlayers())
			Function.pling(player1);

		getConfig().set("timer.time", Timer.time);
		getConfig().set("timer.message", Timer.eventMessage);
		getConfig().set("timer.target", Timer.eventTargetTime);
		saveConfig();
	}

	public void onEnable() {
		/* Temporary unsupported
		if (getServer().getPluginManager().isPluginEnabled("TTA") == true)
			Global.BarAPIHook = true;
		*/
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(new onPlayerConnection(this), this);
		pm.registerEvents(new onPlayerPvP(this), this);
		pm.registerEvents(new onPortalTeleport(this), this);
		pm.registerEvents(new onPlayerDeath(this), this);

		getConfig().options().copyDefaults(true);
		saveConfig();

		Global.spawnOnJoin = getConfig().getBoolean("spawn_on_join");
		Timer.time = getConfig().getLong("timer.time");
		Timer.eventMessage = getConfig().getString("timer.message");
		Timer.eventTargetTime = getConfig().getLong("timer.target");
		
		for (Player player1 : Bukkit.getOnlinePlayers()) {
			Function.orb(player1);
		}

		saveConfig();
				
		Bukkit.broadcastMessage(Prefix.server + getDescription().getName() + " System: " + ChatColor.GREEN + ChatColor.BOLD + "Enable");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(getDescription().getName() + "'s patch version: " + ChatColor.GREEN + getDescription().getVersion());
		Bukkit.broadcastMessage("Developer: " + ChatColor.GOLD + ChatColor.BOLD + "p0ndja");
		Bukkit.broadcastMessage("");
	}
	
	int s = Integer.MAX_VALUE;
	int ss = Integer.MAX_VALUE;

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		if (CommandLabel.equalsIgnoreCase("test")) {
			Player p = (Player) sender;
			p.setInvisible(false);
		}
		if (CommandLabel.equalsIgnoreCase("spawn")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (p.isOp()) {
					if (args.length == 0) {
						p.teleport(new Location(p.getWorld(), 0,64,0));
					} else if (args.length > 0) {
						String targetWorld = args[0];
						if (targetWorld.equalsIgnoreCase("nether")) targetWorld = "world_nether";
						if (Bukkit.getWorld(targetWorld) != null) {
							p.teleport(new Location(Bukkit.getWorld(targetWorld), 0,64,0));
						} else {
							p.sendMessage(Prefix.server + "Invalid world name. [Worlds: world, nether]");
						}
					}
				} else {
					p.sendMessage(Prefix.server + "You don't have permission.");
				}
			}
		}
		
		if (CommandLabel.equalsIgnoreCase("killstats")) {
			if ((sender instanceof Player && sender.isOp() ||
				sender instanceof ConsoleCommandSender ||
				sender instanceof BlockCommandSender) && args.length > 0) {
				if (Bukkit.getPlayer(args[0]) != null) {
					Player t = Bukkit.getPlayer(args[0]);
					sender.sendMessage(Prefix.server + args[0] + " killed " + killData.get("killstats." + t.getScoreboard().getPlayerTeam(t).getName() + "." + t.getName())  + " player");
				}	
			}
		}
		
		if (CommandLabel.equalsIgnoreCase("b") || CommandLabel.equalsIgnoreCase("broadcast")) {
			if (sender.isOp()) {
				String msg = "";
				for(int i = 0; i < args.length; i++) {
					msg += args[i];
				}
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Broadcast> " + ChatColor.WHITE + msg);
				Bukkit.broadcastMessage("");
				Function.pling(1);
			}
		}
		
		if (CommandLabel.equalsIgnoreCase("a") || CommandLabel.equalsIgnoreCase("admin")) {
			if (sender.isOp()) {
				String msg = "";
				for(int i = 0; i < args.length; i++) {
					msg += args[i];
				}
				for(Player p : Bukkit.getOnlinePlayers()) {
					if (p.isOp()) {
						p.sendMessage(ChatColor.RED + "AdminChat> " + ChatColor.RESET + sender.getName() + " " + ChatColor.WHITE + msg);
						Function.pickup(p, 1);
					}
				}
			}
		}
		
		if (CommandLabel.equalsIgnoreCase("uhcadmin")) {
			if (sender instanceof Player && sender.isOp() ||
				sender instanceof ConsoleCommandSender ||
				sender instanceof BlockCommandSender) {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.YELLOW + " " + ChatColor.BOLD + "=== UHC Admin ===");
					sender.sendMessage("/uhcadmin pvp - Toggle PvP");
					sender.sendMessage("");
					sender.sendMessage("/uhcadmin start - Start Game Routine");
					sender.sendMessage("/uhcadmin pause - Pause Game Routine");
					sender.sendMessage("/uhcadmin continue - Pause Game Routine");
					sender.sendMessage("/uhcadmin stop - Clear Game Routine");
					sender.sendMessage("/uhcadmin end - End Game Notify");
				} else {
					if (args[0].equalsIgnoreCase("pvp")) {
						onPlayerPvP.peacefulMode = !onPlayerPvP.peacefulMode;
						if (onPlayerPvP.peacefulMode) {
							Bukkit.broadcastMessage(Prefix.server + "PvP " + ChatColor.RED + "ได้ถูกปิดแล้ว");
							Function.pling(0);
						} else {
							Bukkit.broadcastMessage(Prefix.server + "PvP " + ChatColor.GREEN + "ได้ถูกเปิดแล้ว");
							Function.pling(1);
						}
					} else if (args[0].equalsIgnoreCase("start")) {
						Timer.startServerTime = System.currentTimeMillis();
						Timer.time = (args.length > 1 && isNumeric(args[1])) ? Long.parseLong(args[1]) : 0;
						s = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
							@Override
							public void run() {
								Timer.run();
							}
						}, 0L, 20L);
					} else if (args[0].equalsIgnoreCase("stop")) {
						Timer.startServerTime = 0;
						Timer.time = 0;
						Timer.eventMessage = null;
						Timer.eventTargetTime = -1;
						Bukkit.getServer().getScheduler().cancelTask(s);
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage(Prefix.server + "การแข่งขันได้ถูก" + ChatColor.RED + "ยกเลิก" + ChatColor.GRAY + " โดย " + sender.getName());
						Bukkit.broadcastMessage("");
					} else if (args[0].equalsIgnoreCase("pause") && Timer.pause == false) {
						Timer.pause = true;
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage(Prefix.server + "การแข่งขันได้ถูก" + ChatColor.GOLD + "หยุดพักชั่วคราว" + ChatColor.GRAY + " โดย " + sender.getName());
						Bukkit.broadcastMessage("");
						getConfig().set("timer.time", Timer.time);
						getConfig().set("timer.message", Timer.eventMessage);
						getConfig().set("timer.target", Timer.eventTargetTime);
						saveConfig();
					} else if (args[0].equalsIgnoreCase("continue") && Timer.pause == true) {
						Timer.pause = false;
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage(Prefix.server + "การแข่งขันได้ถูก" + ChatColor.AQUA + ChatColor.BOLD + "ดำเนินการต่อ" + ChatColor.RESET + " โดย " + sender.getName());
						Bukkit.broadcastMessage("");	
					} else if (args[0].equalsIgnoreCase("end")) {
						Timer.startServerTime = 0;
						Timer.time = 0;
						Timer.eventMessage = null;
						Timer.eventTargetTime = -1;
						Bukkit.getServer().getScheduler().cancelTask(s);
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage(Prefix.server + "การแข่งขันได้" + ChatColor.GREEN + ChatColor.BOLD + "สิ้นสุดลงแล้ว");
						String df = "การแข่งขันสิ้นสุดลงแล้ว";
						if (args.length > 1 && args[1] != null && sender instanceof Player) {
							Team t = ((Player) sender).getScoreboard().getTeam(args[1].toUpperCase());
							if (t != null)
								df = t.getPrefix().replace("[", "").replace("]", "") + " - " + t.getDisplayName() + ChatColor.RESET + " เป็นทีมที่ชนะ";
						}
						Function.yes();
						for (Player p : Bukkit.getOnlinePlayers()) {
							p.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "UHC", df, 10, 1000, 10);
							p.setInvulnerable(true);
						}
						Bukkit.broadcastMessage(Prefix.server + df);
 						Bukkit.broadcastMessage("");
 						ss = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
							@Override
							public void run() {
		 						spawnFirework();
							}
						}, 0L, 3L);
					} else if (args[0].equalsIgnoreCase("stopfirework")) {
						Bukkit.getServer().getScheduler().cancelTask(ss);
					} else if (args[0].equalsIgnoreCase("startfirework")) {
						ss = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
							@Override
							public void run() {
		 						spawnFirework();
							}
						}, 0L, 3L);
					}
				}
			}
		}
		return true;
	}
	
	public static boolean isNumeric(String str) {
	    if (str == null) {
	        return false;
	    }
	    int sz = str.length();
	    for (int i = 0; i < sz; i++) {
	        if (Character.isDigit(str.charAt(i)) == false) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public static void spawnFirework() {
		Color[] c = {Color.fromRGB(255, 0, 0), Color.fromRGB(252,68,68), Color.fromRGB(252,100,4), Color.fromRGB(252,212,68), Color.fromRGB(140,196,60), Color.fromRGB(2,150,88),Color.fromRGB(26,188,156), Color.fromRGB(91,192,222), Color.fromRGB(100,84,172)};
		Type[] t = {Type.BALL, Type.BALL_LARGE, Type.BURST, Type.CREEPER, Type.STAR};
		Location loc = new Location(Bukkit.getWorld("world"), 0, 64, 0).add(new Vector(Math.random()-0.5, Math.random(), Math.random()-0.5).multiply(25));
		Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		fwm.setPower(2);
		fwm.addEffect(FireworkEffect.builder().withColor(c[new Random().nextInt(c.length)]).withFade(c[new Random().nextInt(c.length)]).with(t[new Random().nextInt(t.length)]).build());
		fw.setFireworkMeta(fwm);
		fw.detonate();
	}
}
