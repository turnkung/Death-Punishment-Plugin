package me.turnnyxyz.deathpunishment.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.turnnyxyz.deathpunishment.ThisPlugin;
import me.turnnyxyz.deathpunishment.file.PluginFile;
import me.turnnyxyz.deathpunishment.roller.Roller;

public class MainListener implements Listener {
	private static List<UUID> deathPunisher = new ArrayList<>();
	
	private static HashMap<Player, ItemStack[]> deathInv = new HashMap<>();
	
	private static Random r = new Random();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPlayedBefore()) {
			player.teleport(MainListener.getSpawnLocation());
		}
		if (!PluginFile.soulYml.contains(player.getName())) {
			int result = Roller.roll(player);
			if (result == -1) {
				return;
			}
			(new BukkitRunnable() {
				public void run() {
					PluginFile.soulYml.set(player.getName() + ".soul", result);
					player.sendMessage("§aการผจญภัยได้เริ่มขึ้นแล้ว!! ท่านได้รับ §bวิญญาณ §aจำนวน§a " + ChatColor.AQUA + result + " §aดวง!§a");
					try {
						PluginFile.soulYml.save(PluginFile.soulFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).runTaskLater(ThisPlugin.getInstance(), 40L);
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		final UUID playerID = player.getUniqueId();
		MainListener.deathPunisher.add(playerID);
		if (player.getInventory().getContents() != null) {
			ItemStack[] invItems = player.getInventory().getContents();
			MainListener.deathInv.put(player, invItems);
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		event.setRespawnLocation(MainListener.getPunishLocation());
		if (MainListener.deathPunisher.contains(player.getUniqueId())) {
			final int result = Roller.roll(player);
			if (result == -1) {
				return;
			}
			(new BukkitRunnable() {
				public void run() {
					if (result >= 18) {
						player.sendMessage("§bคุณถูกละเว้นการลงโทษในครั้งนี้!§b");
						return;
					}
					MainListener.checkBeforePunish(player);
				}
			}).runTaskLater(ThisPlugin.getInstance(), 40L);
		}
	}
	
	public static void checkBeforePunish(Player player) {
		String playerSoul = player.getName() + ".soul";
		int soulAmount = PluginFile.soulYml.getInt(player.getName() + ".soul");
		PluginFile.soulYml.set(playerSoul, soulAmount - 1);
		try {
			PluginFile.soulYml.save(PluginFile.soulFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int currentAmount = soulAmount - 1;
		player.sendMessage("§c✖ §cคุณสูญเสีย §bวิญญาณ§c จำนวน§f 1 §cดวง§c");
		player.sendMessage("§f➤ §bจำนวนวิญญาณที่คงเหลือ§f " + currentAmount);
		if (soulAmount <= 0) {
			int itemIndex = r.nextInt(33);
			if (player.getInventory().getItem(itemIndex) == null && soulAmount <= -4) {
				for (int i=10; i<player.getInventory().getSize(); i++) {
					if (player.getInventory().getItem(i) != null) {
						MainListener.removeItem(player, i);
						break;
					}
				}
			} else {
				MainListener.removeItem(player, itemIndex);
			}
			PluginFile.soulYml.set(playerSoul, soulAmount - 1);
			try {
				PluginFile.soulYml.save(PluginFile.soulFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (soulAmount == -2) {
			player.sendMessage("§fค่า §bวิญญาณ §fในขณะนี้ §e" + currentAmount);
			player.sendMessage("§eหากค่า §bวิญญาณ §eของท่านลดลงต่ำกว่านี้ §cจะมีการเพิ่มบทลงโทษเกิดขึ้น!§c");
		}
		if (soulAmount <= -4) {
			MainListener.removeArmor(player);
		}
	}
	
	public static void removeItem(Player player, int itemIndex) {
		int disAmount = r.nextInt(65);
		if (player.getInventory().getItem(itemIndex) != null && player.getInventory().getItem(itemIndex).getAmount() > disAmount) {
			if (player.getInventory().getItem(itemIndex).hasItemMeta()) {
				String itemName = player.getInventory().getItem(itemIndex).getItemMeta().getDisplayName();
				player.sendMessage(""); player.sendMessage("");
			    player.sendMessage("§c✖ §bไอเท็ม§f " + itemName + " §cx" + ChatColor.WHITE + disAmount + " §c§l※§l" + " §bถูกทำลายแล้ว§b " + "§c§l※§l");
			    player.sendMessage(""); player.sendMessage("");
			    PluginFile.logToFile(player.getName(), itemName, disAmount);
			} else {
				String itemName = StringUtils.capitalize(player.getInventory().getItem(itemIndex).getType().name().toLowerCase());
				player.sendMessage(""); player.sendMessage("");
			    player.sendMessage("§c✖ §bไอเท็ม§f " + itemName + " §cx" + ChatColor.WHITE + disAmount + " §c§l※§l" + " §bถูกทำลายแล้ว§b " + "§c§l※§l");
			    player.sendMessage(""); player.sendMessage("");
			}
			player.getInventory().getItem(itemIndex).setAmount(player.getInventory().getItem(itemIndex).getAmount() - disAmount);
			player.updateInventory();
		} else {
			if (player.getInventory().getItem(itemIndex) != null && player.getInventory().getItem(itemIndex).getAmount() < disAmount) {
				if (player.getInventory().getItem(itemIndex).hasItemMeta()) {
					String itemName = player.getInventory().getItem(itemIndex).getItemMeta().getDisplayName();
					player.sendMessage(""); player.sendMessage("");
				    player.sendMessage("§c✖ §bไอเท็ม§f " + itemName + " §cx" + ChatColor.WHITE + player.getInventory().getItem(itemIndex).getAmount() + " §c§l※§l" + " §bถูกทำลายแล้ว§b " + "§c§l※§l");
				    player.sendMessage(""); player.sendMessage("");
				    PluginFile.logToFile(player.getName(), itemName, player.getInventory().getItem(itemIndex).getAmount());
				} else {
				    String itemName = StringUtils.capitalize(player.getInventory().getItem(itemIndex).getType().name().toLowerCase());
					player.sendMessage(""); player.sendMessage("");
				    player.sendMessage("§c✖ §bไอเท็ม§f " + itemName + " §cx" + ChatColor.WHITE + player.getInventory().getItem(itemIndex).getAmount() + " §c§l※§l" + " §bถูกทำลายแล้ว§b " + "§c§l※§l");
				    player.sendMessage(""); player.sendMessage("");
				}
				player.getInventory().getItem(itemIndex).setAmount(0);
				player.updateInventory();
			}
		}
	}
	
	public static void removeArmor(Player player) {
		int armorIndex = r.nextInt(4);
		ItemStack[] armor = player.getInventory().getArmorContents();
		if (armor[armorIndex] != null) {
			player.sendMessage("§eค่า §bวิญญาณ§b §eต่ำเกินกว่าที่กำหนด§e §cท่านถูกเพิ่มบทลงโทษ §eดังนี้§e");
			if (armorIndex == 3) {
				if (player.getInventory().getHelmet().hasItemMeta()) {
					String item = player.getInventory().getHelmet().getItemMeta().getDisplayName();
					 PluginFile.logToFile(player.getName(), item, 1);
				}
			} else if (armorIndex == 2) {
				if (player.getInventory().getChestplate().hasItemMeta()) {
					String item = player.getInventory().getChestplate().getItemMeta().getDisplayName();
					PluginFile.logToFile(player.getName(), item, 1);
				}
			} else if (armorIndex == 1) {
				if (player.getInventory().getLeggings().hasItemMeta()) {
					String item = player.getInventory().getLeggings().getItemMeta().getDisplayName();
					PluginFile.logToFile(player.getName(), item, 1);
				}
			} else {
				if (player.getInventory().getBoots().hasItemMeta()) {
					String item = player.getInventory().getBoots().getItemMeta().getDisplayName();
					PluginFile.logToFile(player.getName(), item, 1);
				}
			}
		}
		if (armorIndex == 3) {
			if (player.getInventory().getHelmet() != null) {
				player.getInventory().getHelmet().setAmount(0);
				player.sendMessage("§c✖ §bชุดเกราะ §cส่วนหัว§c" + " §c§l※§l" + " §bถูกทำลายแล้ว§b " + "§c§l※§l");
				player.sendMessage("");
			}
		} else if (armorIndex == 2) {
			if (player.getInventory().getChestplate() != null) {
				player.getInventory().getChestplate().setAmount(0);
				player.sendMessage("§c✖ §bชุดเกราะ §cส่วนกลางตัว§c" + " §c§l※§l" + " §bถูกทำลายแล้ว§b " + "§c§l※§l");
				player.sendMessage("");
			}
		} else if (armorIndex == 1) {
			if (player.getInventory().getLeggings() != null) {
				player.getInventory().getLeggings().setAmount(0);
				player.sendMessage("§c✖ §bชุดเกราะ §cส่วนกางเกง§c" + " §c§l※§l" + " §bถูกทำลายแล้ว§b " + "§c§l※§l");
				player.sendMessage("");
			}
		} else {
			if (player.getInventory().getBoots() != null) {
				player.getInventory().getBoots().setAmount(0);
				player.sendMessage("§c✖ §bชุดเกราะ §cส่วนรองเท้า§c" + " §c§l※§l" + " §bถูกทำลายแล้ว§b " + "§c§l※§l");
				player.sendMessage(""); player.sendMessage("");
			}
		}
	}
	
	public static Location getPunishLocation() {
		Location loc = new Location(null, 0, 0, 0, 0, 0);
		String worldName = PluginFile.locYml.getString("PunishPoint" + ".world");
		loc.setWorld(Bukkit.getServer().getWorld(worldName));
		double x = PluginFile.locYml.getDouble("PunishPoint" + ".x");
		loc.setX(x);
		double y = PluginFile.locYml.getDouble("PunishPoint" + ".y");
		loc.setY(y);
		double z = PluginFile.locYml.getDouble("PunishPoint" + ".z");
		loc.setZ(z);
		float yaw = (float) PluginFile.locYml.getDouble("PunishPoint" + ".yaw");
		loc.setYaw(yaw);
		float pitch =(float) PluginFile.locYml.getDouble("PunishPoint" + ".pitch");
		loc.setPitch(pitch);
		return loc;
	}
	
	public static Location getSpawnLocation() {
		Location loc = new Location(null, 0, 0, 0, 0, 0);
		String worldName = PluginFile.locYml.getString("SpawnPoint" + ".world");
		loc.setWorld(Bukkit.getServer().getWorld(worldName));
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
		return loc;
	}
}