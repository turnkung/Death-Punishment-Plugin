package me.turnnyxyz.deathpunishment.command;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.turnnyxyz.deathpunishment.file.PluginFile;

public class PluginCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setspawnpoint")) {
			Player player = (Player) sender;
			if (sender instanceof Player) {
				if (sender.isOp()) {
					Location loc = player.getLocation();
					String worldName = loc.getWorld().getName();
					PluginFile.locYml.set("SpawnPoint", "");
					PluginFile.locYml.set("SpawnPoint" + ".world", worldName);
					double x = loc.getX();
					PluginFile.locYml.set("SpawnPoint" + ".x", x);
					double y = loc.getY();
					PluginFile.locYml.set("SpawnPoint" + ".y", y);
					double z = loc.getZ();
					PluginFile.locYml.set("SpawnPoint" + ".z", z);
					float yaw = loc.getYaw();
					PluginFile.locYml.set("SpawnPoint" + ".yaw", yaw);
					float pitch = loc.getPitch();
					PluginFile.locYml.set("SpawnPoint" + ".pitch", pitch);
					try {
						PluginFile.locYml.save(PluginFile.locFile);
						player.sendMessage("§aเซ็ทจุดเกิดเรียบร้อยแล้ว!§a");
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					sender.sendMessage(ChatColor.RED + "You don't have permission!");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "This command can only execute by player!");
			}
		} else {
			if (cmd.getName().equalsIgnoreCase("setpunishpoint")) {
				Player player = (Player) sender;
				if (sender instanceof Player) {
					if (sender.isOp()) {
						Location loc = player.getLocation();
						String worldName = loc.getWorld().getName();
						PluginFile.locYml.set("PunishPoint", "");
						PluginFile.locYml.set("PunishPoint" + ".world", worldName);
						double x = loc.getX();
						PluginFile.locYml.set("PunishPoint" + ".x", x);
						double y = loc.getY();
						PluginFile.locYml.set("PunishPoint" + ".y", y);
						double z = loc.getZ();
						PluginFile.locYml.set("PunishPoint" + ".z", z);
						float yaw = loc.getYaw();
						PluginFile.locYml.set("PunishPoint" + ".yaw", yaw);
						float pitch = loc.getPitch();
						PluginFile.locYml.set("PunishPoint" + ".pitch", pitch);
						try {
							PluginFile.locYml.save(PluginFile.locFile);
							player.sendMessage("§aเซ็ทจุดลงโทษเรียบร้อยแล้ว!§a");
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						player.sendMessage(ChatColor.RED + "You don't have permission!");
					}
				} else {
					player.sendMessage(ChatColor.RED + "This command can only execute by player!");
				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("soul")) {
			if (args.length == 0) {
				int playerSouls = PluginFile.soulYml.getInt(sender.getName() + ".soul");
				sender.sendMessage("§aจำนวน§c" + " §bวิญญาณ§b " + "§aของท่านที่มีในขณะนี้คือ§a " + ChatColor.YELLOW + playerSouls + " §aดวง§a");
				return true;
			}
			if (args.length > 0 && args.length != 3 && sender.isOp()) {
				sender.sendMessage("§cการใช้งานคำสั่งไม่ถูกต้อง!§c");
				return true;
			}
			if (args.length > 0 && !sender.isOp()) {
				sender.sendMessage("§cท่านไม่ได้รับอณุญาตให้ใช้คำสั่งนี้!§c");
				return true;
			}
			if (args[0].equalsIgnoreCase("add")) {
				int amount = 0;
				try {
					amount = Integer.parseInt(args[2]);
				} catch (Exception e) {
					sender.sendMessage("§cPlease enter number only.");
					return true;
				}
				PluginFile.soulYml.set(args[1] + ".soul", PluginFile.soulYml.getInt(args[1] + ".soul") + amount);
				PluginFile.saveFile(PluginFile.soulYml, PluginFile.soulFile);
				sender.sendMessage("§bวิญญาณ §b" + "§aจำนวน§a " + ChatColor.YELLOW + amount + " §aดวง§a " + "§bได้ถูกเพิ่มให้กับท่าน §b" + ChatColor.YELLOW + args[1] + " §bแล้ว§b");
				Bukkit.getPlayer(args[1]).sendMessage("§aท่านได้รับ§a " + "§bวิญญาณ§b " + "§aจำนวน§a " + ChatColor.YELLOW + amount + " §aดวงแล้ว§a");
			} else if (args[0].equalsIgnoreCase("remove")) {
				int amount = 0;
				try {
					amount = Integer.parseInt(args[2]);
				} catch (Exception e) {
					sender.sendMessage("§cPlease enter number only.");
					return true;
				}
				PluginFile.soulYml.set(args[1] + ".soul", PluginFile.soulYml.getInt(args[1] + ".soul") - amount);
				PluginFile.saveFile(PluginFile.soulYml, PluginFile.soulFile);
				sender.sendMessage("§bวิญญาณ §b" + "§aจำนวน§a " + ChatColor.YELLOW + amount + " §aดวง§a " + "§bได้ถูกนำออกจากท่าน §b" + ChatColor.YELLOW + args[1] + " §bแล้ว§b");
				Bukkit.getPlayer(args[1]).sendMessage("§aท่านถูกลด§a " + "§bวิญญาณ§b " + "§aจำนวน§a " + ChatColor.YELLOW + amount + " §aดวงแล้ว§a");
			} else if (args[0].equalsIgnoreCase("set")) {
				int amount = 0;
				try {
					amount = Integer.parseInt(args[2]);
				} catch (Exception e) {
					sender.sendMessage("§cPlease enter number only.");
					return true;
				}
				PluginFile.soulYml.set(args[1] + ".soul", amount);
				PluginFile.saveFile(PluginFile.soulYml, PluginFile.soulFile);
				sender.sendMessage("§aทำการกำหนดจำนวน§a " + "§bวิญญาณ§b " + " §aของท่าน§a " + ChatColor.YELLOW + args[1] + " §aให้เป็น§a " + ChatColor.YELLOW + amount + " §aดวงแล้ว§a");
				Bukkit.getPlayer(args[1]).sendMessage("§aจำนวน§a " + "§bวิญญาณ§b §aของท่าน ถูกกำหนดให้เป็น§a " + ChatColor.YELLOW + amount + " §aดวง§a");
			} else if (args[0].equalsIgnoreCase("check")) {
				int playerSouls = PluginFile.soulYml.getInt(args[1] + ".soul");
				sender.sendMessage("§aจำนวน§c" + " §bวิญญาณ§b " + "§aของท่าน§a " + ChatColor.YELLOW + args[1] + " §aที่มีในขณะนี้คือ§a " + ChatColor.YELLOW + playerSouls + " §aดวง§a");
			}
		}
		if (cmd.getName().equalsIgnoreCase("spawn")) {
 			Location loc = new Location(null, 0, 0, 0, 0, 0);
 			String worldName = PluginFile.locYml.getString("SpawnPoint" + ".world");
			double x = PluginFile.locYml.getDouble("SpawnPoint" + ".x");
			loc.setX(x);
			double y = PluginFile.locYml.getDouble("SpawnPoint" + ".y");
			loc.setY(y);
			double z = PluginFile.locYml.getDouble("SpawnPoint" + ".z");
			loc.setZ(z);
			float yaw = (float) PluginFile.locYml.getDouble("SpawnPoint" + ".yaw");
			loc.setYaw(yaw);
			float pitch = (float) PluginFile.locYml.getDouble("SpawnPoint" + ".pitch");
			loc.setPitch(pitch);
			loc.setWorld(Bukkit.getServer().getWorld(worldName));
			if (!(sender instanceof Player) && args.length == 0) {
				return true;
			} else if (!(sender instanceof Player) && args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
				target.teleport(loc);
			}
			if (sender.isOp()) {
				if (args.length != 0) {
					Player target = Bukkit.getPlayer(args[0]);
					target.teleport(loc);
				} else {
					((Player) sender).teleport(loc);
				}	
			} else {
				((Player) sender).teleport(loc);
			}
		}
		return false;
	}
}
