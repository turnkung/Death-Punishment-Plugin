package me.turnnyxyz.deathpunishment;

import org.bukkit.plugin.java.JavaPlugin;

import me.turnnyxyz.deathpunishment.command.PluginCommand;
import me.turnnyxyz.deathpunishment.file.PluginFile;
import me.turnnyxyz.deathpunishment.listener.MainListener;

public class Main extends JavaPlugin {
	public void onEnable() {
		ThisPlugin.constuctor(this);
		PluginFile.checkFiles();
		this.registerEvents();
		this.registerCommands();
	}
	
	public void registerEvents() {
		this.getServer().getPluginManager().registerEvents(new MainListener(), this);
	}
	
	public void registerCommands() {
		this.getCommand("setspawnpoint").setExecutor(new PluginCommand());
		this.getCommand("setpunishpoint").setExecutor(new PluginCommand());
		this.getCommand("soul").setExecutor(new PluginCommand());
		this.getCommand("spawn").setExecutor(new PluginCommand());
	}
}
