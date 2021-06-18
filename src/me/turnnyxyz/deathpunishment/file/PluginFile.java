package me.turnnyxyz.deathpunishment.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.configuration.file.YamlConfiguration;

import me.turnnyxyz.deathpunishment.ThisPlugin;

public abstract class PluginFile {
	public static File cfgFile = new File(ThisPlugin.getInstance().getDataFolder(), "config.yml");
	public static YamlConfiguration cfgYml = YamlConfiguration.loadConfiguration(cfgFile);
	
	public static File soulFile = new File(ThisPlugin.getInstance().getDataFolder(), "souldata.yml");
	public static YamlConfiguration soulYml = YamlConfiguration.loadConfiguration(soulFile);
	
	public static File locFile = new File(ThisPlugin.getInstance().getDataFolder(), "locationdata.yml");
	public static YamlConfiguration locYml = YamlConfiguration.loadConfiguration(locFile);
	
	public static void checkFiles() {
		/*if (!cfgFile.exists()) {
			ThisPlugin.getInstance().saveResource("config.yml", false);
		}*/
		if (!soulFile.exists()) {
			try {
				soulFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!locFile.exists()) {
			try {
				locFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void saveFile(YamlConfiguration cfg, File file) {
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void logToFile(String playerName, String item, int amount) {
		File folder = new File(ThisPlugin.getInstance().getDataFolder(), "logs");
		if (!folder.exists()) {
			folder.mkdir();
		}
		Date fileDate = new Date();
		SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String logDate = fileDateFormat.format(fileDate);
		try {
			File logFile = new File(folder, logDate + "-log.txt");
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			PrintWriter writer = new PrintWriter(new FileWriter(logFile, true), true);
			
			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(date);
			
			writer.write(time + " " + playerName + " " + item + " " + amount);
			writer.write(System.getProperty("line.separator"));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
