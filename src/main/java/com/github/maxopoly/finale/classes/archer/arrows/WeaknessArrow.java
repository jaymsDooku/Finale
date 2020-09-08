package com.github.maxopoly.finale.classes.archer.arrows;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.maxopoly.finale.classes.archer.ArcherPlayer;
import com.github.maxopoly.finale.classes.archer.ArrowTools;
import com.github.maxopoly.finale.classes.archer.EnergyConsumption;

import io.jayms.serenno.kit.ItemMetaBuilder;
import io.jayms.serenno.kit.ItemStackBuilder;
import io.jayms.serenno.util.ParticleTools;
import net.md_5.bungee.api.ChatColor;

public class WeaknessArrow extends PotionEffectArrow {

	public static final String NAME = "weakness-arrow";
	
	@Override
	public ItemStackBuilder newDisplayItemBuilder() {
		return new ItemStackBuilder(Material.FERMENTED_SPIDER_EYE, 1)
				.meta(new ItemMetaBuilder()
						.name(getDisplayName())
						.lore(Arrays.asList(ChatColor.WHITE + "Duration: " + ChatColor.GRAY + config.getWeaknessDuration(),
								ChatColor.WHITE + "Cooldown: " + ChatColor.GRAY + TimeUnit.MILLISECONDS.toSeconds(config.getWeaknessCooldown()) + "s",
								ChatColor.WHITE + "Additional Cooldown: " + ChatColor.GRAY + TimeUnit.MILLISECONDS.toSeconds(config.getWeaknessAdditionalCooldown()) + "s",
								ChatColor.WHITE + "Cost: " + ChatColor.GRAY + ArrowTools.formatConsumption(getBaseEnergyConsumption()), 
								ChatColor.WHITE + "Additional Cost: " + ChatColor.GRAY + ArrowTools.formatConsumption(getAdditionalEnergyConsumption()))));
	}

	@Override
	public EnergyConsumption getBaseEnergyConsumption() {
		return new EnergyConsumption(config.getWeaknessConsumption(), config.getWeaknessEnergy());
	}
	
	@Override
	public EnergyConsumption getAdditionalEnergyConsumption() {
		return new EnergyConsumption(config.getAdditionalWeaknessConsumption(), config.getAdditionalWeaknessEnergy());
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDisplayName() {
		return ChatColor.DARK_GRAY + "Weakness " + ChatColor.WHITE + "Arrow";
	}

	@Override
	public long getBaseCooldown() {
		return config.getWeaknessCooldown();
	}
	
	@Override
	public long getAdditionalCooldown() {
		return config.getWeaknessAdditionalCooldown();
	}
	
	@Override
	public String getParticleColour() {
		return "#626663";
	}

	@Override
	public PotionEffectType getPotionEffectType() {
		return PotionEffectType.WEAKNESS;
	}
	
	@Override
	public int getPotionDuration(ArcherPlayer player) {
		return (int) ((config.getWeaknessDuration() * 20) * player.getEffectiveLinkedReduction());
	}
	
	@Override
	public double getExplosiveRadius(ArcherPlayer player) {
		return config.getWeaknessExplosiveRadius() * player.getEffectiveLinkedReduction();
	}
}
