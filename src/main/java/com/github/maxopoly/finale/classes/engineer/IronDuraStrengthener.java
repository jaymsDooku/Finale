package com.github.maxopoly.finale.classes.engineer;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import com.github.maxopoly.finale.classes.ClassController;
import com.github.maxopoly.finale.classes.DuraStrengthener;

public class IronDuraStrengthener extends DuraStrengthener {

	public IronDuraStrengthener(ClassController controller) {
		super(controller);
	}
	
	@Override
	public List<Material> getArmour() {
		return Arrays.asList(Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET);
	}
	
	@Override
	public int getStrength() {
		return 2;
	}
	
}
