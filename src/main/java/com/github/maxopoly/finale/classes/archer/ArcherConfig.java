package com.github.maxopoly.finale.classes.archer;

import org.bukkit.configuration.ConfigurationSection;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.ConsumptionType;

public class ArcherConfig {

	private double flareRange;
	private ConsumptionType flareConsumption;
	private double flareEnergy;
	private long flareCooldown;
	
	private double huntersMarkDamage;
	private long huntersMarkMinDuration;
	private long huntersMarkDuration;
	private ConsumptionType huntersMarkConsumption;
	private double huntersMarkEnergy;
	private long huntersMarkCooldown;
	
	private double poisonExplosiveRadius;
	private int poisonDuration;
	private ConsumptionType poisonConsumption;
	private double poisonEnergy;
	private ConsumptionType additionalPoisonConsumption;
	private double additionalPoisonEnergy;
	private long poisonCooldown;
	private long poisonAdditionalCooldown;
	
	private double slownessExplosiveRadius;
	private int slownessDuration;
	private ConsumptionType slownessConsumption;
	private double slownessEnergy;
	private ConsumptionType additionalSlownessConsumption;
	private double additionalSlownessEnergy;
	private long slownessCooldown;
	private long slownessAdditionalCooldown;
	
	private double weaknessExplosiveRadius;
	private int weaknessDuration;
	private ConsumptionType weaknessConsumption;
	private double weaknessEnergy;
	private ConsumptionType additionalWeaknessConsumption;
	private double additionalWeaknessEnergy;
	private long weaknessCooldown;
	private long weaknessAdditionalCooldown;
	
	private int electricMaxVictims;
	private double electricMinDamage;
	private double electricMaxDamage;
	private double electricMinHorizontal;
	private double electricMaxHorizontal;
	private double electricMinVertical;
	private double electricMaxVertical;
	private double electricChainRange;
	private double electricFullRange;
	private ConsumptionType electricConsumption;
	private double electricEnergy;
	private ConsumptionType additionalElectricConsumption;
	private double additionalElectricEnergy;
	private long electricCooldown;
	private long electricAdditionalCooldown;
	
	private double explosiveRadius;
	private double explosiveDamage;
	private double explosiveHorizontal;
	private double explosiveVertical;
	private ConsumptionType explosiveConsumption;
	private double explosiveEnergy;
	private ConsumptionType additionalExplosiveConsumption;
	private double additionalExplosiveEnergy;
	private long explosiveCooldown;
	private long explosiveAdditionalCooldown;
	
	private int siegeExplosiveRadius;
	private double siegeDamageReinforcement;
	private double siegeDamageBastion;
	private double siegeDamageExplosiveReinforcement;
	private double siegeDamageExplosiveBastion;
	private ConsumptionType siegeConsumption;
	private double siegeEnergy;
	private ConsumptionType additionalSiegeConsumption;
	private double additionalSiegeEnergy;
	private long siegeCooldown;
	private long siegeAdditionalCooldown;
	
	private int sugarRushDuration;
	private ConsumptionType sugarRushConsumption;
	private double sugarRushEnergy;
	private long sugarRushCooldown;
	
	private boolean linkedFriendlyFire;
	
	private boolean distanceDamageEnabled;
	private double additionalDamagePerBlock;
	private double maxAdditionalDamage;
	
	private double linkedReduction;
	
	private double linkerRange;
	private long linkerCooldown;
	
	public boolean isLinkedFriendlyFire() {
		return linkedFriendlyFire;
	}
	
	private ConsumptionType consumption(String type, ConsumptionType def) {
		try {
			return ConsumptionType.valueOf(type);
		} catch (IllegalArgumentException e) {
			return def;
		}
	}
	
	private Finale finale;
	
	public ArcherConfig(ConfigurationSection archerConfig) {
		finale = Finale.getPlugin();
		
		linkedFriendlyFire = archerConfig.getBoolean("linkedFriendlyFire", false);
		finale.info("Linked Friendly Fire: " + (linkedFriendlyFire ? "ON" : "OFF"));
		
		distanceDamageEnabled = archerConfig.getBoolean("distance.enabled", true);
		additionalDamagePerBlock = archerConfig.getDouble("distance.additionalDamagePerBlock", 0.25);
		maxAdditionalDamage = archerConfig.getDouble("distance.maxAdditionalDamage", 8);
		finale.info("Distance Damage: " + (distanceDamageEnabled ? "ON" : "OFF"));
		finale.info("Additional Damage Per Block: " + additionalDamagePerBlock);
		finale.info("Max Additional Damage: " + maxAdditionalDamage);
		
		linkedReduction = archerConfig.getDouble("linkReduction", 0.01);
		
		linkerRange = archerConfig.getDouble("linker.range", 20);
		linkerCooldown = archerConfig.getLong("linker.cooldown", 1000);
		
		sugarRushDuration = archerConfig.getInt("sugarRush.duration", 10);
		sugarRushConsumption = consumption(archerConfig.getString("sugarRush.consumption"), ConsumptionType.PERCENTAGE);
		sugarRushEnergy = archerConfig.getDouble("sugarRush.energy", 5);
		sugarRushCooldown = archerConfig.getLong("sugarRush.cooldown", 30000);
		finale.info("Sugar Rush Duration: " + sugarRushDuration);
		finale.info("Sugar Rush Consumption: " + sugarRushConsumption);
		finale.info("Sugar Rush Energy: " + sugarRushEnergy);
		finale.info("Sugar Rush Cooldown: " + sugarRushCooldown);
		
		if (!archerConfig.contains("arrows")) {
			Finale.getPlugin().warning("Arrows Config doesn't exist!");
			return;
		}
		ConfigurationSection arrows = archerConfig.getConfigurationSection("arrows");
		flareRange = arrows.getDouble("flare.range", 50);
		flareConsumption = consumption(arrows.getString("flare.consumption"), ConsumptionType.FLAT);
		flareEnergy = arrows.getDouble("flare.energy", 1);
		flareCooldown = arrows.getLong("flare.cooldown", 2500);
		finale.info("Flare Range: " + flareRange);
		finale.info("Flare Consumption: " + flareConsumption);
		finale.info("Flare Energy: " + flareEnergy);
		finale.info("Flare Cooldown: " + flareCooldown);
		
		huntersMarkDamage = arrows.getDouble("hunter.damage", 1.2);
		huntersMarkMinDuration = arrows.getLong("hunter.duration.min", 5000);
		huntersMarkDuration = arrows.getLong("hunter.duration.force", 10000);
		huntersMarkConsumption = consumption(arrows.getString("hunter.consumption"), ConsumptionType.PERCENTAGE);
		huntersMarkEnergy = arrows.getDouble("hunter.energy", 3.75);
		huntersMarkCooldown = arrows.getLong("hunter.cooldown", 2000);
		finale.info("Hunters Mark Damage: " + huntersMarkDamage);
		finale.info("Hunters Mark Min Duration: " + huntersMarkMinDuration);
		finale.info("Hunters Mark Duration: " + huntersMarkDuration);
		finale.info("Hunters Mark Consumption: " + huntersMarkConsumption);
		finale.info("Hunters Mark Energy: " + huntersMarkEnergy);
		finale.info("Hunters Mark Cooldown: " + huntersMarkCooldown);
		
		poisonExplosiveRadius = arrows.getDouble("poison.explosiveRadius", 5);
		poisonDuration = arrows.getInt("poison.duration", 10);
		poisonConsumption = consumption(arrows.getString("poison.consumption"), ConsumptionType.PERCENTAGE);
		poisonEnergy = arrows.getDouble("poison.energy", 2);
		additionalPoisonConsumption = consumption(arrows.getString("poison.additionalConsumption"), ConsumptionType.PERCENTAGE);
		additionalPoisonEnergy = arrows.getDouble("poison.additionalEnergy", 2);
		poisonCooldown = arrows.getLong("poison.cooldown", 3500);
		poisonAdditionalCooldown = arrows.getLong("poison.additionalCooldown", 3500);
		finale.info("Poison Explosive Radius: " + poisonExplosiveRadius);
		finale.info("Poison Duration: " + poisonDuration);
		finale.info("Poison Consumption: " + poisonConsumption);
		finale.info("Poison Energy: " + poisonEnergy);
		finale.info("Poison Cooldown: " + poisonCooldown);
		finale.info("Poison Additional Consumption: " + additionalPoisonConsumption);
		finale.info("Poison Additional Energy: " + additionalPoisonEnergy);
		finale.info("Poison Additional Cooldown: " + poisonAdditionalCooldown);
		
		slownessExplosiveRadius = arrows.getDouble("slowness.explosiveRadius", 5);
		slownessDuration = arrows.getInt("slowness.duration", 5);
		slownessConsumption = consumption(arrows.getString("slowness.consumption"), ConsumptionType.PERCENTAGE);
		slownessEnergy = arrows.getDouble("slowness.energy", 2.5);
		additionalSlownessConsumption = consumption(arrows.getString("slowness.additionalConsumption"), ConsumptionType.PERCENTAGE);
		additionalSlownessEnergy = arrows.getDouble("slowness.additionalEnergy", 2);
		slownessCooldown = arrows.getLong("slowness.cooldown", 3500);
		slownessAdditionalCooldown = arrows.getLong("slowness.additionalCooldown", 3500);
		finale.info("Slowness Explosive Radius: " + slownessExplosiveRadius);
		finale.info("Slowness Duration: " + slownessDuration);
		finale.info("Slowness Consumption: " + slownessConsumption);
		finale.info("Slowness Energy: " + slownessEnergy);
		finale.info("Slowness Cooldown: " + slownessCooldown);
		finale.info("Slowness Additional Consumption: " + additionalSlownessConsumption);
		finale.info("Slowness Additional Energy: " + additionalSlownessEnergy);
		finale.info("Slowness Additional Cooldown: " + slownessAdditionalCooldown);
		
		weaknessExplosiveRadius = arrows.getDouble("weakness.explosiveRadius", 5);
		weaknessDuration = arrows.getInt("weakness.duration", 10);
		weaknessConsumption = consumption(arrows.getString("weakness.consumption"), ConsumptionType.PERCENTAGE);
		weaknessEnergy = arrows.getDouble("weakness.energy", 2.5);
		additionalWeaknessConsumption = consumption(arrows.getString("weakness.additionalConsumption"), ConsumptionType.PERCENTAGE);
		additionalWeaknessEnergy = arrows.getDouble("weakness.additionalEnergy", 2);
		weaknessCooldown = arrows.getLong("weakness.cooldown", 3500);
		weaknessAdditionalCooldown = arrows.getLong("weakness.additionalCooldown", 3500);
		finale.info("Weakness Explosive Radius: " + weaknessExplosiveRadius);
		finale.info("Weakness Duration: " + weaknessDuration);
		finale.info("Weakness Consumption: " + weaknessConsumption);
		finale.info("Weakness Energy: " + weaknessEnergy);
		finale.info("Weakness Cooldown: " + weaknessCooldown);
		finale.info("Weakness Additional Consumption: " + additionalWeaknessConsumption);
		finale.info("Weakness Additional Energy: " + additionalWeaknessEnergy);
		finale.info("Weakness Additional Cooldown: " + weaknessAdditionalCooldown);
		
		electricMaxVictims = arrows.getInt("electric.maxVictims", 10);
		electricMinDamage = arrows.getDouble("electric.damage.min", 5);
		electricMaxDamage = arrows.getDouble("electric.damage.max", 10);
		electricMinHorizontal = arrows.getDouble("electric.horizontal.min", 1.5);
		electricMaxHorizontal = arrows.getDouble("electric.horizontal.max", 1.5);
		electricMinVertical = arrows.getDouble("electric.vertical.min", 0.8);
		electricMaxVertical = arrows.getDouble("electric.vertical.max", 1.2);
		electricChainRange = arrows.getDouble("electric.range.chain", 20);
		electricFullRange = arrows.getDouble("electric.range.full", 200);
		electricConsumption = consumption(arrows.getString("electric.consumption"), ConsumptionType.PERCENTAGE);
		electricEnergy = arrows.getDouble("electric.energy", 1.25);
		additionalElectricConsumption = consumption(arrows.getString("electric.additionalConsumption"), ConsumptionType.PERCENTAGE);
		additionalElectricEnergy = arrows.getDouble("electric.additionalEnergy", 2.5);
		electricCooldown = arrows.getLong("electric.cooldown", 4000);
		electricAdditionalCooldown = arrows.getLong("electric.additionalCooldown", 1500);
		finale.info("Electric Max Victims: " + electricMaxVictims);
		finale.info("Electric Min Damage: " + electricMinDamage);
		finale.info("Electric Max Damage: " + electricMaxDamage);
		finale.info("Electric Min Horizontal: " + electricMinHorizontal);
		finale.info("Electric Max Horizontal: " + electricMaxHorizontal);
		finale.info("Electric Min Vertical: " + electricMinVertical);
		finale.info("Electric Max Vertical: " + electricMaxVertical);
		finale.info("Electric Chain Range: " + electricChainRange);
		finale.info("Electric Full Range: " + electricFullRange);
		finale.info("Electric Consumption: " + electricConsumption);
		finale.info("Electric Energy: " + electricEnergy);
		finale.info("Electric Cooldown: " + electricCooldown);
		finale.info("Electric Additional Consumption: " + additionalElectricConsumption);
		finale.info("Electric Additional Energy: " + additionalElectricEnergy);
		finale.info("Electric Additional Cooldown: " + electricAdditionalCooldown);
		
		explosiveRadius = arrows.getDouble("explosive.radius", 5);
		explosiveDamage = arrows.getDouble("explosive.damage", 10);
		explosiveHorizontal = arrows.getDouble("explosive.horizontal", 2);
		explosiveVertical = arrows.getDouble("explosive.vertical", 1.2);
		explosiveConsumption = consumption(arrows.getString("explosive.consumption"), ConsumptionType.PERCENTAGE);
		explosiveEnergy = arrows.getDouble("explosive.energy", 1.25);
		additionalExplosiveConsumption = consumption(arrows.getString("explosive.additionalConsumption"), ConsumptionType.PERCENTAGE);
		additionalExplosiveEnergy = arrows.getDouble("explosive.additionalEnergy", 3);
		explosiveCooldown = arrows.getLong("explosive.cooldown", 5000);
		explosiveAdditionalCooldown = arrows.getLong("explosive.additionalCooldown", 1500);
		finale.info("Electric Radius: " + explosiveRadius);
		finale.info("Electric Damage: " + explosiveDamage);
		finale.info("Electric Horizontal: " + explosiveHorizontal);
		finale.info("Electric Vertical: " + explosiveVertical);
		finale.info("Explosive Consumption: " + explosiveConsumption);
		finale.info("Explosive Energy: " + explosiveEnergy);
		finale.info("Explosive Cooldown: " + explosiveCooldown);
		finale.info("Explosive Additional Consumption: " + additionalExplosiveConsumption);
		finale.info("Explosive Additional Energy: " + additionalExplosiveEnergy);
		finale.info("Explosive Additional Cooldown: " + explosiveAdditionalCooldown);
		
		siegeExplosiveRadius = arrows.getInt("siege.explosiveRadius", 3);
		siegeDamageReinforcement = arrows.getDouble("siege.damage.reinforcement", 3);
		siegeDamageBastion = arrows.getDouble("siege.damage.bastion", 1);
		siegeDamageExplosiveReinforcement = arrows.getDouble("siege.damage.explosive.reinforcement", 3);
		siegeDamageExplosiveBastion = arrows.getDouble("siege.damage.explosive.bastion", 1);
		siegeConsumption = consumption(arrows.getString("siege.consumption"), ConsumptionType.PERCENTAGE);
		siegeEnergy = arrows.getDouble("siege.energy", 2);
		additionalSiegeConsumption = consumption(arrows.getString("siege.additionalConsumption"), ConsumptionType.PERCENTAGE);
		additionalSiegeEnergy = arrows.getDouble("siege.additionalEnergy", 2);
		siegeCooldown = arrows.getLong("siege.cooldown", 2000);
		siegeAdditionalCooldown = arrows.getLong("siege.additionalCooldown", 1000);
		finale.info("Siege Explosive Radius: " + siegeExplosiveRadius);
		finale.info("Siege Explosive Reinforcement Damage: " + siegeDamageExplosiveReinforcement);
		finale.info("Siege Explosive Bastion Damage: " + siegeDamageExplosiveBastion);
		finale.info("Siege Concentrated Reinforcement Damage: " + siegeDamageReinforcement);
		finale.info("Siege Concentrated Bastion Damage: " + siegeDamageBastion);
		finale.info("Siege Consumption: " + siegeConsumption);
		finale.info("Siege Energy: " + siegeEnergy);
		finale.info("Siege Cooldown: " + siegeCooldown);
		finale.info("Siege Additional Consumption: " + additionalSiegeConsumption);
		finale.info("Siege Additional Energy: " + additionalSiegeEnergy);
		finale.info("Siege Additional Cooldown: " + siegeAdditionalCooldown);
	}
	
	public double getMaxAdditionalDamage() {
		return maxAdditionalDamage;
	}
	
	public int getSiegeExplosiveRadius() {
		return siegeExplosiveRadius;
	}
	
	public double getSiegeDamageBastion() {
		return siegeDamageBastion;
	}
	
	public double getSiegeDamageReinforcement() {
		return siegeDamageReinforcement;
	}
	
	public double getSiegeDamageExplosiveReinforcement() {
		return siegeDamageExplosiveReinforcement;
	}

	public double getSiegeDamageExplosiveBastion() {
		return siegeDamageExplosiveBastion;
	}

	public ConsumptionType getSiegeConsumption() {
		return siegeConsumption;
	}

	public double getSiegeEnergy() {
		return siegeEnergy;
	}

	public ConsumptionType getAdditionalSiegeConsumption() {
		return additionalSiegeConsumption;
	}

	public double getAdditionalSiegeEnergy() {
		return additionalSiegeEnergy;
	}

	public long getSiegeCooldown() {
		return siegeCooldown;
	}

	public long getSiegeAdditionalCooldown() {
		return siegeAdditionalCooldown;
	}

	public void setSiegeExplosiveRadius(int siegeExplosiveRadius) {
		this.siegeExplosiveRadius = siegeExplosiveRadius;
	}

	public double getLinkerRange() {
		return linkerRange;
	}
	
	public long getLinkerCooldown() {
		return linkerCooldown;
	}
	
	public double getLinkedReduction() {
		return linkedReduction;
	}
	
	public double getPoisonExplosiveRadius() {
		return poisonExplosiveRadius;
	}

	public double getSlownessExplosiveRadius() {
		return slownessExplosiveRadius;
	}

	public double getWeaknessExplosiveRadius() {
		return weaknessExplosiveRadius;
	}

	public double getExplosiveRadius() {
		return explosiveRadius;
	}

	public int getSugarRushDuration() {
		return sugarRushDuration;
	}

	public ConsumptionType getSugarRushConsumption() {
		return sugarRushConsumption;
	}

	public double getSugarRushEnergy() {
		return sugarRushEnergy;
	}

	public long getSugarRushCooldown() {
		return sugarRushCooldown;
	}

	public boolean isDistanceDamageEnabled() {
		return distanceDamageEnabled;
	}
	
	public double getAdditionalDamagePerBlock() {
		return additionalDamagePerBlock;
	}
	
	public long getHuntersMarkMinDuration() {
		return huntersMarkMinDuration;
	}
	
	public long getHuntersMarkDuration() {
		return huntersMarkDuration;
	}

	public ConsumptionType getAdditionalPoisonConsumption() {
		return additionalPoisonConsumption;
	}

	public ConsumptionType getAdditionalSlownessConsumption() {
		return additionalSlownessConsumption;
	}

	public ConsumptionType getAdditionalWeaknessConsumption() {
		return additionalWeaknessConsumption;
	}

	public ConsumptionType getAdditionalElectricConsumption() {
		return additionalElectricConsumption;
	}

	public ConsumptionType getAdditionalExplosiveConsumption() {
		return additionalExplosiveConsumption;
	}

	public double getAdditionalPoisonEnergy() {
		return additionalPoisonEnergy;
	}

	public long getPoisonAdditionalCooldown() {
		return poisonAdditionalCooldown;
	}

	public double getAdditionalSlownessEnergy() {
		return additionalSlownessEnergy;
	}

	public long getSlownessAdditionalCooldown() {
		return slownessAdditionalCooldown;
	}

	public double getAdditionalWeaknessEnergy() {
		return additionalWeaknessEnergy;
	}

	public long getWeaknessAdditionalCooldown() {
		return weaknessAdditionalCooldown;
	}

	public int getElectricMaxVictims() {
		return electricMaxVictims;
	}

	public double getAdditionalElectricEnergy() {
		return additionalElectricEnergy;
	}

	public long getElectricAdditionalCooldown() {
		return electricAdditionalCooldown;
	}

	public double getAdditionalExplosiveEnergy() {
		return additionalExplosiveEnergy;
	}

	public long getExplosiveAdditionalCooldown() {
		return explosiveAdditionalCooldown;
	}

	public double getFlareRange() {
		return flareRange;
	}

	public ConsumptionType getFlareConsumption() {
		return flareConsumption;
	}

	public double getFlareEnergy() {
		return flareEnergy;
	}

	public long getFlareCooldown() {
		return flareCooldown;
	}

	public double getHuntersMarkDamage() {
		return huntersMarkDamage;
	}

	public ConsumptionType getHuntersMarkConsumption() {
		return huntersMarkConsumption;
	}

	public double getHuntersMarkEnergy() {
		return huntersMarkEnergy;
	}

	public long getHuntersMarkCooldown() {
		return huntersMarkCooldown;
	}

	public int getPoisonDuration() {
		return poisonDuration;
	}

	public ConsumptionType getPoisonConsumption() {
		return poisonConsumption;
	}

	public double getPoisonEnergy() {
		return poisonEnergy;
	}

	public long getPoisonCooldown() {
		return poisonCooldown;
	}

	public int getSlownessDuration() {
		return slownessDuration;
	}

	public ConsumptionType getSlownessConsumption() {
		return slownessConsumption;
	}

	public double getSlownessEnergy() {
		return slownessEnergy;
	}

	public long getSlownessCooldown() {
		return slownessCooldown;
	}

	public int getWeaknessDuration() {
		return weaknessDuration;
	}

	public ConsumptionType getWeaknessConsumption() {
		return weaknessConsumption;
	}

	public double getWeaknessEnergy() {
		return weaknessEnergy;
	}

	public long getWeaknessCooldown() {
		return weaknessCooldown;
	}

	public double getElectricMinDamage() {
		return electricMinDamage;
	}

	public double getElectricMaxDamage() {
		return electricMaxDamage;
	}

	public double getElectricMinHorizontal() {
		return electricMinHorizontal;
	}

	public double getElectricMaxHorizontal() {
		return electricMaxHorizontal;
	}

	public double getElectricMinVertical() {
		return electricMinVertical;
	}

	public double getElectricMaxVertical() {
		return electricMaxVertical;
	}

	public double getElectricChainRange() {
		return electricChainRange;
	}

	public double getElectricFullRange() {
		return electricFullRange;
	}

	public ConsumptionType getElectricConsumption() {
		return electricConsumption;
	}

	public double getElectricEnergy() {
		return electricEnergy;
	}

	public long getElectricCooldown() {
		return electricCooldown;
	}

	public double getExplosiveDamage() {
		return explosiveDamage;
	}

	public double getExplosiveHorizontal() {
		return explosiveHorizontal;
	}

	public double getExplosiveVertical() {
		return explosiveVertical;
	}

	public ConsumptionType getExplosiveConsumption() {
		return explosiveConsumption;
	}

	public double getExplosiveEnergy() {
		return explosiveEnergy;
	}

	public long getExplosiveCooldown() {
		return explosiveCooldown;
	}
	
}


