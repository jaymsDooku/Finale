package com.github.maxopoly.finale.classes.archer;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import com.github.maxopoly.finale.classes.ClassController;
import com.github.maxopoly.finale.classes.DuraStrengthener;

public class LeatherDuraStrengthener extends DuraStrengthener {

	public LeatherDuraStrengthener(ClassController controller) {
		super(controller);
	}
	
	@Override
	public List<Material> getArmour() {
		return Arrays.asList(Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET);
	}
	
	@Override
	public int getStrength() {
		return 6;
	}
	
}
