package me.turnnyxyz.deathpunishment.roller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.turnnyxyz.deathpunishment.ThisPlugin;

public abstract class Roller {
	public static List<UUID> playerList = new ArrayList<>();
	
	private static String[] symbolArr = new String[] {
			"Θ", "τ", "ε", "ς", "σ", "ζ", "ɱ", "Ξ", "δ", "ɧ",
			"υ", "μ", "λ", "ʃ", "γ", "ŋ", "ƪ", "ɋ", "Ȣ", "※"};
	
	private static Random r = new Random();
	
	public static int roll(Player player) {
		final int diceResult = getRandomDiceNumber();
		final UUID playerID = player.getUniqueId();
		playerList.add(playerID);
		player.setAllowFlight(true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 2, false, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, 128, false, false, false));
		final BukkitRunnable runnable = new BukkitRunnable() {
			public void run() {
				Roller.rollDice(player, Roller.getRandomDiceNumber(), 3);
				player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 0.8F, 0.75F + Roller.r.nextFloat() / 2.0F);
			}
		};
		(new BukkitRunnable() {
			public void run() {
				runnable.cancel();
				Roller.rollFinalDice(player, diceResult, 20, 40);
				if (diceResult == 19) {
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8F, 1.0F);
					player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.6F, 1.0F);
				} else if (diceResult == 0) {
		            player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.2F, 0.2F);
		            player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.9F, 0.4F);
		            player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.8F, 0.7F);
		            player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.7F, 0.9F);
				} else {
		            float flt = 1.3F - (19.0F - diceResult) / 20.0F;
		            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 0.8F, flt);
		            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8F, flt);
		            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.7F, flt);
				}
			}
		}).runTaskLater(ThisPlugin.getInstance(), 40L);
		runnable.runTaskTimer(ThisPlugin.getInstance(), 0L, 2L);
		return diceResult + 1;
	}
	
	private static void rollDice(Player player, int number, int duration) {
		player.sendTitle("§l§6" + symbolArr[number], "§l§6" + (number + 1), 0, duration, 0);
	}
	
	private static void rollFinalDice(Player player, int number, int duration, int fade) {
		String color = "§6";
		if (fade != 0 && number == 0) {
			color = "§4";
		} else if (fade != 0 && number == 19) {
			color = "§e";
		}
		player.sendTitle("§l" + color + symbolArr[number], "§l" + color + (number + 1), 0, duration, fade);
	}
	
	private static int getRandomDiceNumber() {
		return r.nextInt(20);
	}
}
