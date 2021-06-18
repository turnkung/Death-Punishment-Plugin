package me.turnnyxyz.deathpunishment;

import org.bukkit.plugin.Plugin;

public abstract class ThisPlugin {
	private static Plugin plugin;
	
	public static void constuctor(Plugin plugin) {
		ThisPlugin.plugin = plugin;
	}
	
	public static Plugin getInstance() {
		return plugin;
	}
}
