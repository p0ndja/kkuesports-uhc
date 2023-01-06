package dev.p0ndja.kkuesports;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Global {
	pluginMain pl;

	public Global(pluginMain pl) {
		this.pl = pl;
	}
	public static boolean BarAPIHook = false;
	public static boolean BossBarAPIHook = false;
	public static boolean LoginFeature = false;
	public static boolean spawnOnJoin = true;
	
	public static List<String> blockLogin = new ArrayList<String>();
	
	public static String[] adminTeamList = {"KKU"};
	public static String[] teamList = {"CITH", "PK", "KJ", "RB", "WT", "KKEZ", "NIT", "EM4", "PFF", "TSS", "MT", "YV"};
	
	public static String pluginName = "KKUeSportsUHC";
	public static String pluginDir = "plugins/" + pluginName + "/";
	
	/*
	public static Material[] luckyClick_Simple = {Material.DIRT};
	public static Material[] luckyClick_High = {Material.DIAMOND, Material.COBBLESTONE};
	public static Material[] luckyClick_Rare = {Material.DIAMOND_BLOCK, Material.EMERALD, };
	*/
}
