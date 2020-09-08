package com.github.maxopoly.finale.classes.engineer;

import org.bukkit.configuration.ConfigurationSection;

import com.github.maxopoly.finale.Finale;

public class EngineerConfig {

	private double smokeBombRadius;
	private int smokeBombDuration;
	private double smokeBombPower;
	private double smokeBombGravity;
	private long smokeBombCooldown;
	
	private Finale finale;
	
	public EngineerConfig(ConfigurationSection engineerConfig) {
		finale = Finale.getPlugin();
		
		smokeBombRadius = engineerConfig.getDouble("smokeBomb.radius", 3.5);
		smokeBombDuration = engineerConfig.getInt("smokeBomb.duration", 5);
		smokeBombPower = engineerConfig.getDouble("smokeBomb.power", 2);
		smokeBombGravity = engineerConfig.getDouble("smokeBomb.gravity", -0.2);
		smokeBombCooldown = engineerConfig.getLong("smokeBomb.cooldown", 60000);
		finale.info("Smoke Bomb Radius: " + smokeBombRadius);
		finale.info("Smoke Bomb Duration: " + smokeBombDuration);
		finale.info("Smoke Bomb Power: " + smokeBombPower);
		finale.info("Smoke Bomb Gravity: " + smokeBombGravity);
		finale.info("Smoke Bomb Cooldown: " + smokeBombCooldown);
	}
	
	public double getSmokeBombRadius() {
		return smokeBombRadius;
	}
	
	public long getSmokeBombCooldown() {
		return smokeBombCooldown;
	}
	
	public int getSmokeBombDuration() {
		return smokeBombDuration;
	}
	
	public double getSmokeBombPower() {
		return smokeBombPower;
	}
	
	public double getSmokeBombGravity() {
		return smokeBombGravity;
	}
	
}
