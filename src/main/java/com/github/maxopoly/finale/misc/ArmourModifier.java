package com.github.maxopoly.finale.misc;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

public class ArmourModifier {

	private class ArmourConfig {
		private Material mat;
		private int toughness;
		private int armour;

		private ArmourConfig(Material mat, int toughness, int armour) {
			this.mat = mat;
			this.toughness = toughness;
			this.armour = armour;
		}

		public int getToughness() {
			return toughness;
		}
		
		public int getArmour() {
			return armour;
		}

		public Material getMaterial() {
			return mat;
		}
	}
	
	private Map<Material, ArmourConfig> armour;

	public ArmourModifier() {
		this.armour = new HashMap<Material, ArmourModifier.ArmourConfig>();
	}

	public void addArmour(Material m, int toughness, int armour) {
		this.armour.put(m, new ArmourConfig(m, toughness, armour));
	}

	public int getToughness(Material m) {
		ArmourConfig config = armour.get(m);
		if (config == null) {
			return -1;
		}
		return config.getToughness();
	}

	public int getArmour(Material m) {
		ArmourConfig config = armour.get(m);
		if (config == null) {
			return -1;
		}
		return config.getArmour();
	}

	
}
