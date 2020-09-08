package com.github.maxopoly.finale.classes;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class ClassRequirements {
	
	public abstract Material[] getRequiredArmour(); // 0 = boots, 1 = leggings, 2 = chestplate, 3 = helmet
	
	public boolean meetsRequirement(ItemStack[] armourContents, List<ItemStack> contents) {
		Material[] requiredArmour = getRequiredArmour();
		if (requiredArmour != null) {
			if (requiredArmour.length != 4) {
				throw new IllegalStateException("Required armour of a class must be 4.");
			}
			
			for (int i = 0; i < requiredArmour.length; i++) {
				if (requiredArmour[i] == Material.AIR) continue;
				if (armourContents[i] == null && requiredArmour[i] != null) {
					return false;
				}
				if (armourContents[i].getType() != requiredArmour[i]) {
					return false;
				}
			}
		}
		return true;
	}
	
}
