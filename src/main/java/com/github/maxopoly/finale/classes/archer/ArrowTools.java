package com.github.maxopoly.finale.classes.archer;

import java.util.Set;

import org.bukkit.entity.Player;

import com.github.maxopoly.finale.classes.ClassType;
import com.github.maxopoly.finale.classes.ConsumptionType;

import net.md_5.bungee.api.ChatColor;

public final class ArrowTools {

	public static double distance(ArcherPlayer player, org.bukkit.entity.Arrow arrow) {
		return player.getBukkitPlayer().getLocation().distance(arrow.getLocation());
	}
	
	public static ChatColor energyColour(double energyPerc) {
		if (energyPerc < 25) {
			return ChatColor.RED;
		} else if (energyPerc < 50) {
			return ChatColor.GOLD;
		} else if (energyPerc < 75) {
			return ChatColor.YELLOW;
		} else {
			return ChatColor.GREEN;
		}
	}
	
	public static boolean firedByArcher(org.bukkit.entity.Arrow arrow) {
		if (!(arrow.getShooter() instanceof Player)) {
			return false;
		}
		return isArcher((Player) arrow.getShooter());
	}
	
	public static boolean isArcher(Player p) {
		return ClassType.ARCHER.getController().isActive(p);
	}
	
	public static ArcherPlayer getArcherPlayer(org.bukkit.entity.Arrow arrow) {
		if (!(arrow.getShooter() instanceof Player)) {
			return null;
		}
		return getArcherPlayer((Player) arrow.getShooter());
	}
	
	public static ArcherPlayer getArcherPlayer(Player p) {
		ArcherController ac = ((ArcherController)ClassType.ARCHER.getController());
		
		if (!ac.isActive(p)) {
			return null;
		}
		
		return ac.getArcher(p);
	}
	
	public static String formatConsumption(EnergyConsumption consumption) {
		return Double.toString(consumption.getAmount()) + (consumption.getType() == ConsumptionType.PERCENTAGE ? "%" : "");
	}
	
	public static void archerMessage(Player p, String message) {
		message = ChatColor.BLACK + "[" + ChatColor.GOLD + "Archer" + ChatColor.BLACK + "]: " + ChatColor.RESET + message;
		p.sendMessage(message);
	}
	
	public static void linkedBroadcast(ArcherPlayer archer, String message) {
		message = ChatColor.BLACK + "[" + ChatColor.GOLD + "Archer " + ChatColor.WHITE + archer.getBukkitPlayer() + ChatColor.BLACK + "]: " + ChatColor.RESET + message;
		
		Set<Player> linked = archer.getLinked();
		for (Player p : linked) {
			p.sendMessage(message);
		}
	}
	
}
