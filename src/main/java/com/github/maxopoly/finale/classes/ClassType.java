package com.github.maxopoly.finale.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.maxopoly.finale.classes.archer.ArcherController;
import com.github.maxopoly.finale.classes.engineer.EngineerController;

public enum ClassType {

	ARCHER(ChatColor.GOLD + "Archer", new ClassRequirements() {
		
		@Override
		public Material[] getRequiredArmour() {
			return new Material[]{
				Material.LEATHER_BOOTS,
				Material.LEATHER_LEGGINGS,
				Material.LEATHER_CHESTPLATE,
				Material.LEATHER_HELMET
			};
		}
		
	}, new ArcherController()),
	PILOT(ChatColor.GRAY + "Pilot", new ClassRequirements() {
		
		@Override
		public Material[] getRequiredArmour() {
			return new Material[]{
				Material.AIR,
				Material.AIR,
				Material.ELYTRA,
				Material.AIR
			};
		}

	}, null),
	ENGINEER(ChatColor.DARK_GRAY + "Engineer", new ClassRequirements() {
		
		@Override
		public Material[] getRequiredArmour() {
			return new Material[]{
				Material.IRON_BOOTS,
				Material.IRON_LEGGINGS,
				Material.IRON_CHESTPLATE,
				Material.IRON_HELMET
			};
		}

	}, new EngineerController());
	
	private String displayName;
	private ClassRequirements requirements;
	private ClassController controller;
	
	private ClassType(String displayName, ClassRequirements requirements, ClassController controller) {
		this.displayName = displayName;
		this.requirements = requirements;
		this.controller = controller;
		if (controller != null) controller.setClassType(this);
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public ClassRequirements getRequirements() {
		return requirements;
	}
	
	public ClassController getController() {
		return controller;
	}
	
	private static List<ClassController> controllers;
	
	public static List<ClassController> getControllers() {
		if (controllers == null) {
			controllers = new ArrayList<>();
			for (ClassType type : ClassType.values()) {
				controllers.add(type.getController());
			}
		}
		return controllers;
	}
	
	public static ClassController getActiveController(Player player) {
		for (ClassController controller : ClassType.getControllers()) {
			if (controller != null && controller.isActive(player)) {
				return controller;
			}
		}
		return null;
	}
}
