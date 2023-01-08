package dev.p0ndja.kkuesports;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.p0ndja.kkuesports.Prefix;
import dev.p0ndja.kkuesports.API.ActionBarAPI_api;
import dev.p0ndja.kkuesports.EventListener.onPlayerPvP;
import dev.p0ndja.kkuesports.EventListener.onPortalTeleport;
import dev.p0ndja.kkuesports.Global;

public class Timer {

	private static boolean CountdownDisplayMessageBoolean = false;
	public static long time = 0;
	public static String eventMessage = null;
	public static long eventTargetTime = -1;
	public static boolean pause = false;
	public static long startServerTime = 0;
	public static boolean skipJoinAutoBan = false;

	public static void run() {
		UHCEventHandler();
		long currentTime = time;
		long currentHour = currentTime/3600;
		long currentMinute = (currentTime%3600)/60;
		long currentSecond = (currentTime%3600)%60;

		if (pause == true) {
			ActionBarAPI_api.send(String.format("[" + ChatColor.GREEN + "UHC" + ChatColor.RESET + "] " + ChatColor.YELLOW + "Match Time: " + ChatColor.RESET + "%02d:%02d:%02d" + ChatColor.RED + ChatColor.BOLD + " PAUSED", currentHour, currentMinute, currentSecond));
		} else {
			if (eventMessage != null && eventTargetTime != -1) {
				if (currentTime >= (eventTargetTime - (5*60))) {
					if (currentTime%6 == 0)
						CountdownDisplayMessageBoolean = !CountdownDisplayMessageBoolean;
					if (CountdownDisplayMessageBoolean || (eventTargetTime - currentTime <= 10)) {
						long currentTimeToTarget = eventTargetTime - currentTime;
						if (currentTimeToTarget == 0) {
							ActionBarAPI_api.send(String.format("[" + ChatColor.GREEN + "UHC" + ChatColor.RESET + "] " + ChatColor.AQUA + "Next Event: " + ChatColor.RESET + eventMessage + " ในตอนนี้!"));
							Function.pling((float) 0);
							eventMessage = null;
							eventTargetTime = -1;
						} else {
							long currentHourToTarget = currentTimeToTarget/3600;
							long currentMinuteToTarget = (currentTimeToTarget%3600)/60;
							long currentSecondToTarget = (currentTimeToTarget%3600)%60;
							ActionBarAPI_api.send(String.format("[" + ChatColor.GREEN + "UHC" + ChatColor.RESET + "] " + ChatColor.AQUA + "Next Event: " + ChatColor.RESET + eventMessage + " ใน %02d:%02d:%02d", currentHourToTarget, currentMinuteToTarget, currentSecondToTarget));
							if (currentTimeToTarget == 5) {
								Function.pling(2);
							} else if (currentTimeToTarget == 4) {
								Function.pling((float) 1.8);
							} else if (currentTimeToTarget == 3) {
								Function.pling((float) 1.6);
							} else if (currentTimeToTarget == 2) {
								Function.pling((float) 1.4);
							} else if (currentTimeToTarget == 1) {
								Function.pling((float) 1.2);
							}
						}
					} else {
						ActionBarAPI_api.send(String.format("[" + ChatColor.GREEN + "UHC" + ChatColor.RESET + "] " + ChatColor.YELLOW + "Match Time: " + ChatColor.RESET + "%02d:%02d:%02d", currentHour, currentMinute, currentSecond));
					}
				} else {
					ActionBarAPI_api.send(String.format("[" + ChatColor.GREEN + "UHC" + ChatColor.RESET + "] " + ChatColor.YELLOW + "Match Time: " + ChatColor.RESET + "%02d:%02d:%02d", currentHour, currentMinute, currentSecond));
				}
			} else {
				ActionBarAPI_api.send(String.format("[" + ChatColor.GREEN + "UHC" + ChatColor.RESET + "] " + ChatColor.YELLOW + "Match Time: " + ChatColor.RESET + "%02d:%02d:%02d", currentHour, currentMinute, currentSecond));
			}
			time++;
		}
	}

	public static void UHCEventHandler() {
		World overworld = Bukkit.getWorld("world");
		WorldBorder overworld_wb = overworld.getWorldBorder();
		World nether = Bukkit.getWorld("world_nether");
		WorldBorder nether_wb = nether.getWorldBorder();
		
		if (Timer.time == 0) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.isOp() || p.getScoreboard().getPlayerTeam(p).getName().toLowerCase().equalsIgnoreCase("kku"))
					p.setGameMode(GameMode.SPECTATOR);
				else
					p.setGameMode(GameMode.SURVIVAL);
			}
			overworld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
			overworld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
			overworld.setGameRule(GameRule.NATURAL_REGENERATION, true);
			overworld.setTime(0);
			overworld.setStorm(false);
			overworld.setClearWeatherDuration(24000);
			overworld.setDifficulty(Difficulty.HARD);
			overworld_wb.setCenter(new Location(overworld, 0.5,0.5,0.5));
			overworld_wb.setSize(5000, 10);
			
			nether.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
			nether.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
			nether.setGameRule(GameRule.NATURAL_REGENERATION, true);
			nether.setTime(0);
			nether.setStorm(false);
			nether.setDifficulty(Difficulty.HARD);
			nether_wb.setCenter(new Location(nether, 0.5,0.5,0.5));
			nether_wb.setSize(1500);
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.setInvulnerable(true);
			}
			
			onPlayerPvP.peacefulMode = true;
			
			onPortalTeleport.allowPortal = true;
			
			for (long x = 0; x < 500; x++) {
				Bukkit.broadcastMessage("");
			}
			
			Bukkit.broadcastMessage(Prefix.server + ChatColor.GREEN + ChatColor.BOLD + "การแข่งขันเริ่มต้นขึ้นแล้ว!");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(Prefix.server + "PvP " + ChatColor.RED + "ได้ถูกปิดแล้ว" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:00:00]");
			Bukkit.broadcastMessage(Prefix.server + "Nether " + ChatColor.GREEN + "ได้ถูกเปิดแล้ว" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:00:00]");
			Bukkit.broadcastMessage(Prefix.server + "Worldborder ของ Overworld ได้ขยายขนาดเป็น " + ChatColor.GOLD + ChatColor.BOLD + "5000x5000" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:00:00]");
			Bukkit.broadcastMessage(Prefix.server + "Worldborder ของ Nether ได้ขยายขนาดเป็น " + ChatColor.GOLD + ChatColor.BOLD + "1500x1500" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:00:00]");
			Bukkit.broadcastMessage(Prefix.server + "ผู้เล่นจะไม่ได้รับความเสียหายชั่วคราวเป็นเวลา " + ChatColor.GOLD + ChatColor.BOLD + "1 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:00:00]");

			Function.pling(0);
		} else if (Timer.time == 60) {
			overworld.setGameRule(GameRule.FALL_DAMAGE, true);
			nether.setGameRule(GameRule.FALL_DAMAGE, true);
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.setInvulnerable(false);
			}
			Bukkit.broadcastMessage(Prefix.server + "ผู้เล่นจะได้รับความเสียหาย" + ChatColor.GOLD + ChatColor.BOLD + "ตามปกติ" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:01:00]");

		} else if (Timer.time == 1800-(5*60)) { //30 minutes - 5m
			eventMessage = "เปิด PvP";
			eventTargetTime = 1800;
			Bukkit.broadcastMessage(Prefix.server + "PvP กำลังจะเปิดในอีก " + ChatColor.GOLD + "5 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:25:00]");
			Function.pling(1);
		} else if (Timer.time == 1800-(3*60)) { //30 minutes - 3m
			eventMessage = "เปิด PvP";
			eventTargetTime = 1800;
			Bukkit.broadcastMessage(Prefix.server + "PvP กำลังจะเปิดในอีก " + ChatColor.GOLD + "3 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:27:00]");
			Function.pling(1);
		} else if (Timer.time == 1800-(60)) { //30 minutes - 1m
			eventMessage = "เปิด PvP";
			eventTargetTime = 1800;
			Bukkit.broadcastMessage(Prefix.server + "PvP กำลังจะเปิดในอีก " + ChatColor.GOLD + "1 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:29:00]");
			Function.pling(1);
		} else if (Timer.time == 1800) { //30 minutes
			onPlayerPvP.peacefulMode = false;
			Bukkit.broadcastMessage(Prefix.server + "PvP " + ChatColor.GREEN + "ได้ถูกเปิดแล้ว" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:30:00]");
			Bukkit.broadcastMessage(Prefix.server + "Natural Regeneration " + ChatColor.RED + "ได้ถูกปิดแล้ว" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:30:00]");
			Function.pling(0);
			overworld.setGameRule(GameRule.NATURAL_REGENERATION, false);
			nether.setGameRule(GameRule.NATURAL_REGENERATION, false);
			
		} else if (Timer.time == 2700-(5*60)) { //45 minutes - 5m
			eventMessage = "ปิด Nether";
			eventTargetTime = 2700;
			Bukkit.broadcastMessage(Prefix.server + "Nether กำลังจะปิดในอีก " + ChatColor.GOLD + "5 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:40:00]");
			Function.pling(1);
		} else if (Timer.time == 2700-(3*60)) { //45 minutes - 3m
			eventMessage = "ปิด Nether";
			eventTargetTime = 2700;
			Bukkit.broadcastMessage(Prefix.server + "Nether กำลังจะปิดในอีก " + ChatColor.GOLD + "3 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:42:00]");
			Function.pling(1);
		} else if (Timer.time == 2700-(60)) { //45 minutes - 1m
			eventMessage = "ปิด Nether";
			eventTargetTime = 2700;
			Bukkit.broadcastMessage(Prefix.server + "Nether กำลังจะปิดในอีก " + ChatColor.GOLD + "1 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:44:00]");
			Function.pling(1);
		} else if (Timer.time == 2700) { //45 minutes
			onPortalTeleport.allowPortal = false;
			Bukkit.broadcastMessage(Prefix.server + "Nether " + ChatColor.RED + "ได้ถูกปิดแล้ว" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:45:00]");
			Function.pling(1);
			for(Player plo : Bukkit.getOnlinePlayers()) {
				if (plo.getWorld().getEnvironment().equals(World.Environment.NETHER) || plo.getWorld().getName().equalsIgnoreCase("world_nether")) {
					plo.setHealth(0);
					plo.teleport(new Location(overworld, 0, 70, 0));
				}
			}
			//TODO: Kill all player in nether, disable portal
		
		
		} else if (Timer.time == 3000-(5*60)+1) { //50 minutes - 5 m + 1
			eventMessage = "เริ่มการลดขนาด Worldborder เป็น 3000x3000";
			eventTargetTime = 3000;
			Bukkit.broadcastMessage(Prefix.server + "Worldborder กำลังจะลดขนาดเหลือ " + ChatColor.AQUA + "3000x3000" + ChatColor.GRAY + " ในอีก " + ChatColor.GOLD + "5 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:45:00]");
			Function.pling(1);
		} else if (Timer.time == 3000-(3*60)) { //50 minutes - 3m
			eventMessage = "เริ่มการลดขนาด Worldborder เป็น 3000x3000";
			eventTargetTime = 3000;
			Bukkit.broadcastMessage(Prefix.server + "Worldborder กำลังจะลดขนาดเหลือ " + ChatColor.AQUA + "3000x3000" + ChatColor.GRAY + " ในอีก " + ChatColor.GOLD + "3 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:47:00]");
			Function.pling(1);
		} else if (Timer.time == 3000-(60)) { //50 minutes - 1m
			eventMessage = "เริ่มการลดขนาด Worldborder เป็น 3000x3000";
			eventTargetTime = 3000;
			Bukkit.broadcastMessage(Prefix.server + "Worldborder กำลังจะลดขนาดเหลือ " + ChatColor.AQUA + "3000x3000" + ChatColor.GRAY + " ในอีก " + ChatColor.GOLD + "1 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:49:00]");
			Function.pling(1);
		} else if (Timer.time == 3000) { //50 minutes
			Bukkit.broadcastMessage(Prefix.server + "เริ่มต้นการลดขนาด Worldborder จาก " + ChatColor.GOLD + "5000x5000 -> 3000x3000 ด้วยความเร็ว 2 บล็อกต่อวินาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [00:50:00]");
			Function.pling(1);
			overworld_wb.setSize(3000, 1000);
		} else if (Timer.time == 3000+1000) { //50 minutes+1000s [1:06:40]
			Bukkit.broadcastMessage(Prefix.server + "สิ้นสุดการลดขนาด Worldborder ที่ขนาด " + ChatColor.GOLD + "3000x3000" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:06:40]");
			Function.pling(1);
		
		
		} else if (Timer.time == 4500-(5*60)) { //1h15m - 5 m
			eventMessage = "เริ่มการลดขนาด Worldborder เป็น 1500x1500";
			eventTargetTime = 4500;
			Bukkit.broadcastMessage(Prefix.server + "Worldborder กำลังจะลดขนาดเหลือ " + ChatColor.AQUA + "1500x1500" + ChatColor.GRAY + " ในอีก " + ChatColor.GOLD + "5 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:10:00]");
			Function.pling(1);
		} else if (Timer.time == 4500-(3*60)) { //1h15m - 3
			eventMessage = "เริ่มการลดขนาด Worldborder เป็น 1500x1500";
			eventTargetTime = 4500;
			Bukkit.broadcastMessage(Prefix.server + "Worldborder กำลังจะลดขนาดเหลือ " + ChatColor.AQUA + "1500x1500" + ChatColor.GRAY + " ในอีก " + ChatColor.GOLD + "3 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:12:00]");
			Function.pling(1);
		} else if (Timer.time == 4500-(60)) { //1h15m - 1
			eventMessage = "เริ่มการลดขนาด Worldborder เป็น 1500x1500";
			eventTargetTime = 4500;
			Bukkit.broadcastMessage(Prefix.server + "Worldborder กำลังจะลดขนาดเหลือ " + ChatColor.AQUA + "1500x1500" + ChatColor.GRAY + " ในอีก " + ChatColor.GOLD + "1 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:14:00]");
			Function.pling(1);
		} else if (Timer.time == 4500) { //1h15m
			Bukkit.broadcastMessage(Prefix.server + "เริ่มต้นการลดขนาด Worldborder จาก " + ChatColor.GOLD + "3000x3000 -> 1500x1500 ด้วยความเร็ว 3 บล็อกต่อวินาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:15:00]");
			Function.pling(1);
			overworld_wb.setSize(1500, 500);
		} else if (Timer.time == 4500+500) { //1h15m+500s [1:23:20]
			Bukkit.broadcastMessage(Prefix.server + "สิ้นสุดการลดขนาด Worldborder ที่ขนาด " + ChatColor.GOLD + "1500x1500" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:23:20]");
			Function.pling(1);
		
			
			
		} else if (Timer.time == 5400-(5*60)) { //1h30m - 5 m
			eventMessage = "เริ่มการลดขนาด Worldborder เป็น 400x400";
			eventTargetTime = 5400;
			Bukkit.broadcastMessage(Prefix.server + "Worldborder กำลังจะลดขนาดเหลือ " + ChatColor.AQUA + "400x400" + ChatColor.GRAY + " ในอีก " + ChatColor.GOLD + "5 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:25:00]");
			Function.pling(1);
		} else if (Timer.time == 5400-(3*60)) { //1h30m - 3m
			eventMessage = "เริ่มการลดขนาด Worldborder เป็น 400x400";
			eventTargetTime = 5400;
			Bukkit.broadcastMessage(Prefix.server + "Worldborder กำลังจะลดขนาดเหลือ " + ChatColor.AQUA + "400x400" + ChatColor.GRAY + " ในอีก " + ChatColor.GOLD + "3 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:27:00]");
			Function.pling(1);
		} else if (Timer.time == 5400-(60)) { //1h30m - 1m
			eventMessage = "เริ่มการลดขนาด Worldborder เป็น 400x400";
			eventTargetTime = 5400;
			Bukkit.broadcastMessage(Prefix.server + "Worldborder กำลังจะลดขนาดเหลือ " + ChatColor.AQUA + "400x400" + ChatColor.GRAY + " ในอีก " + ChatColor.GOLD + "1 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:29:00]");
			Function.pling(1);
		} else if (Timer.time == 5400) { //1h30m
			Bukkit.broadcastMessage(Prefix.server + "เริ่มต้นการลดขนาด Worldborder จาก " + ChatColor.GOLD + "1500x1500 -> 400x400 ด้วยความเร็ว 5.5 บล็อกต่อวินาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:30:00]");
			Function.pling(1);
			overworld_wb.setSize(400, 200);
		} else if (Timer.time == 5400+200) { //1h30m+200s [1:33:20]
			Bukkit.broadcastMessage(Prefix.server + "สิ้นสุดการลดขนาด Worldborder ที่ขนาด " + ChatColor.GOLD + "400x400" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:33:20]");
			Function.pling(1);
		
			
		} else if (Timer.time == 6300-(5*60)) { //1h45m - 5 m
			eventMessage = "เริ่มการลดขนาด Worldborder เป็น 100x100";
			eventTargetTime = 6300;
			Bukkit.broadcastMessage(Prefix.server + "Worldborder กำลังจะลดขนาดเหลือ " + ChatColor.AQUA + "100x100" + ChatColor.GRAY + " ในอีก " + ChatColor.GOLD + "5 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:40:00]");
			Function.pling(1);
		} else if (Timer.time == 6300-(3*60)) { //1h45m - 3m
			eventMessage = "เริ่มการลดขนาด Worldborder เป็น 100x100";
			eventTargetTime = 6300;
			Bukkit.broadcastMessage(Prefix.server + "Worldborder กำลังจะลดขนาดเหลือ " + ChatColor.AQUA + "100x100" + ChatColor.GRAY + " ในอีก " + ChatColor.GOLD + "3 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:42:00]");
			Function.pling(1);
		} else if (Timer.time == 6300-(60)) { //1h45m - 1m
			eventMessage = "เริ่มการลดขนาด Worldborder เป็น 100x100";
			eventTargetTime = 6300;
			Bukkit.broadcastMessage(Prefix.server + "Worldborder กำลังจะลดขนาดเหลือ " + ChatColor.AQUA + "100x100" + ChatColor.GRAY + " ในอีก " + ChatColor.GOLD + "1 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:44:00]");
			Function.pling(1);
		} else if (Timer.time == 6300) { //1h45m
			Bukkit.broadcastMessage(Prefix.server + "เริ่มต้นการลดขนาด Worldborder จาก " + ChatColor.GOLD + "400x400 -> 100x100 ด้วยความเร็ว 1 บล็อกต่อวินาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:45:00]");
			Function.pling(1);
			overworld_wb.setSize(100, 300);
		} else if (Timer.time == 6300+300) { //1h45m+300s [1:50:00]
			Bukkit.broadcastMessage(Prefix.server + "สิ้นสุดการลดขนาด Worldborder ที่ขนาด " + ChatColor.GOLD + "100x100" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:50:00]");
			Function.pling(1);
		
			
		} else if (Timer.time == 6900-(5*60)+1) { //1h55m - 5 m +1
			eventMessage = "เริ่มการลดขนาด Worldborder เป็น 50x50";
			eventTargetTime = 6900;
			Bukkit.broadcastMessage(Prefix.server + "Worldborder กำลังจะลดขนาดเหลือ " + ChatColor.AQUA + "50x50" + ChatColor.GRAY + " ในอีก " + ChatColor.GOLD + "5 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:50:00]");
			Function.pling(1);
		} else if (Timer.time == 6900-(3*60)) { //1h55m - 3m
			eventMessage = "เริ่มการลดขนาด Worldborder เป็น 50x50";
			eventTargetTime = 6900;
			Bukkit.broadcastMessage(Prefix.server + "Worldborder กำลังจะลดขนาดเหลือ " + ChatColor.AQUA + "50x50" + ChatColor.GRAY + " ในอีก " + ChatColor.GOLD + "3 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:52:00]");
			Function.pling(1);
		} else if (Timer.time == 6900-(60)) { //1h55m - 1m
			eventMessage = "เริ่มการลดขนาด Worldborder เป็น 50x50";
			eventTargetTime = 6900;
			Bukkit.broadcastMessage(Prefix.server + "Worldborder กำลังจะลดขนาดเหลือ " + ChatColor.AQUA + "50x50" + ChatColor.GRAY + " ในอีก " + ChatColor.GOLD + "1 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:54:00]");
			Function.pling(1);
		} else if (Timer.time == 6900) { //1h55m
			Bukkit.broadcastMessage(Prefix.server + "เริ่มต้นการลดขนาด Worldborder จาก " + ChatColor.GOLD + "100x100 -> 50x50 ด้วยความเร็ว 1 บล็อกต่อวินาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:55:00]");
			Function.pling(1);
			overworld_wb.setSize(50, 50);
		} else if (Timer.time == 6900+50) { //1h55m+50s [1:55:50]
			Bukkit.broadcastMessage(Prefix.server + "สิ้นสุดการลดขนาด Worldborder ที่ขนาด " + ChatColor.GOLD + "50x50" + ChatColor.YELLOW + ChatColor.ITALIC + " [01:55:50]");
			Function.pling(1);
		}
		
		
		else if (Timer.time == 7200) {
			Bukkit.broadcastMessage(Prefix.server + "แสดงตำแหน่งของผู้เล่นทั้งหมด" + ChatColor.YELLOW + ChatColor.ITALIC + " [02:00:00]");
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1000000, 0));
			}
		}
		
		
		else if (Timer.time == 7200 + (14*60)) { //overtime -1m
			Bukkit.broadcastMessage(Prefix.server + "ระบบจะทำการเทเลพอร์ตผู้เล่นทั้งหมดมายังลานต่อสู้ในอีก " + ChatColor.GOLD + "1 นาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [02:14:00]");
			eventMessage = "เทเลพอร์ตผู้เล่นทั้งหมดมายังลานต่อสู้";
			eventTargetTime = 7200 + (15*60);
		}
		else if (Timer.time == 7200 + (15*60)) { //overtime
			overworld_wb.setSize(50);
			overworld_wb.setCenter(new Location(overworld, 0.5,0.5,0.5));
			createPlatform();
			Bukkit.broadcastMessage(Prefix.server + "เทเลพอร์ตผู้เล่นทั้งหมดมายังลาน" + ChatColor.YELLOW + ChatColor.ITALIC + " [02:15:00]");
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.setInvulnerable(true);
				p.teleport(new Location(overworld, 0, 317, 0));
			}
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spreadplayers 0 0 10 20 true @a");
			
			onPlayerPvP.peacefulMode = true;
			Bukkit.broadcastMessage(Prefix.server + "PvP " + ChatColor.RED + "ได้ถูกปิดชั่วคราวเป็นเวลา 30 วินาที" + ChatColor.YELLOW + ChatColor.ITALIC + " [02:15:00]");
			Function.pling(1);
		} else if (Timer.time == 7200 + (15*60) + 30) { //overtime
			overworld_wb.setSize(0, 200);
			overworld_wb.setDamageBuffer(0);
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.setInvulnerable(false);
			}
			onPlayerPvP.peacefulMode = false;
			Bukkit.broadcastMessage(Prefix.server + "PvP " + ChatColor.GREEN + "ได้ถูกเปิดแล้ว" + ChatColor.YELLOW + ChatColor.ITALIC + " [02:15:30]");
			Bukkit.broadcastMessage(Prefix.server + "Worldborder กำลังลดขนาดเหลือ " + ChatColor.GOLD + "0x0" + ChatColor.YELLOW + ChatColor.ITALIC + " [02:15:30]");

			Function.pling(1);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "uhcadmin setdeathy");
		}
	}
	
	public static void createPlatform() {
		World w = Bukkit.getWorld("world");
		for (int i = -25; i <= 25; i++) {
			for (int o = -25; o <= 25; o++) {
				Location l1 = new Location(w, i, 314, o);
				l1.getBlock().setType(Material.BEDROCK);
				Location l2 = new Location(w, i, 315, o);
				Location l3 = new Location(w, i, 316, o);
				Location l4 = new Location(w, i, 317, o);
				Location l5 = new Location(w, i, 318, o);
				Location l6 = new Location(w, i, 319, o);

				l1.getBlock().setType(Material.BEDROCK);
				l2.getBlock().setType(Material.AIR);
				l3.getBlock().setType(Material.AIR);
				l4.getBlock().setType(Material.AIR);
				l5.getBlock().setType(Material.AIR);
				l6.getBlock().setType(Material.AIR);

			}
		}
	}
	
	public static void deletePlatform() {
		World w = Bukkit.getWorld("world");
		for (int i = -25; i <= 25; i++) {
			for (int o = -25; o <= 25; o++) {
				Location l1 = new Location(w, i, 314, o);
				l1.getBlock().setType(Material.AIR);
			}
		}
	}
}
