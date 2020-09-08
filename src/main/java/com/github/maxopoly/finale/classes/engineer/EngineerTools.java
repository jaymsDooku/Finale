package com.github.maxopoly.finale.classes.engineer;

import org.bukkit.entity.Player;

import com.github.maxopoly.finale.classes.ClassType;

import net.md_5.bungee.api.ChatColor;

public class EngineerTools {

	public static EngineerPlayer getEngineer(Player player) {
		EngineerController ec = ((EngineerController)ClassType.ENGINEER.getController());
		
		if (!ec.isActive(player)) {
			return null;
		}
		
		return ec.getEngineer(player);
	}
	
	public static void engineerMessage(Player p, String message) {
		message = ChatColor.BLACK + "[" + ChatColor.DARK_GRAY + "Engineer" + ChatColor.BLACK + "]: " + ChatColor.RESET + message;
		p.sendMessage(message);
	}
	
}
