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

public class SlownessArrow extends PotionEffectArrow {

	public static final String NAME = "slowness-arrow";
	
	@Override
	public ItemStackBuilder newDisplayItemBuilder() {
		return new ItemStackBuilder(Material.WEB, 1)
				.meta(new ItemMetaBuilder()
						.name(getDisplayName())
						.lore(Arrays.asList(ChatColor.WHITE + "Duration: " + ChatColor.GRAY + config.getSlownessDuration(),
								ChatColor.WHITE + "Cooldown: " + ChatColor.GRAY + TimeUnit.MILLISECONDS.toSeconds(config.getSlownessCooldown()) + "s",
								ChatColor.WHITE + "Additional Cooldown: " + ChatColor.GRAY + TimeUnit.MILLISECONDS.toSeconds(config.getSlownessAdditionalCooldown()) + "s",
								ChatColor.WHITE + "Cost: " + ChatColor.GRAY + ArrowTools.formatConsumption(getBaseEnergyConsumption()), 
								ChatColor.WHITE + "Additional Cost: " + ChatColor.GRAY + ArrowTools.formatConsumption(getAdditionalEnergyConsumption()))));
	}

	@Override
	public EnergyConsumption getBaseEnergyConsumption() {
		return new EnergyConsumption(config.getSlownessConsumption(), config.getSlownessEnergy());
	}
	
	@Override
	public EnergyConsumption getAdditionalEnergyConsumption() {
		return new EnergyConsumption(config.getAdditionalSlownessConsumption(), config.getAdditionalSlownessEnergy());
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDisplayName() {
		return ChatColor.GRAY + "Slowness " + ChatColor.WHITE + "Arrow";
	}
	
	@Override
	public String getParticleColour() {
		return "#859288";
	}

	@Override
	public long getBaseCooldown() {
		return config.getSlownessCooldown();
	}
	
	@Override
	public long getAdditionalCooldown() {
		return config.getSlownessAdditionalCooldown();
	}

	@Override
	public PotionEffectType getPotionEffectType() {
		return PotionEffectType.SLOW;
	}
	
	@Override
	public int getPotionDuration(ArcherPlayer player) {
		return (int) ((config.getSlownessDuration() * 20) * player.getEffectiveLinkedReduction());
	}
	
	@Override
	public double getExplosiveRadius(ArcherPlayer player) {
		return config.getSlownessExplosiveRadius() * player.getEffectiveLinkedReduction();
	}
	
}