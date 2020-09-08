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

public class PoisonArrow extends PotionEffectArrow {

	public static final String NAME = "poison-arrow";
	
	@Override
	public ItemStackBuilder newDisplayItemBuilder() {
		return new ItemStackBuilder(Material.SPIDER_EYE, 1)
				.meta(new ItemMetaBuilder()
						.name(getDisplayName())
						.lore(Arrays.asList(ChatColor.WHITE + "Duration: " + ChatColor.DARK_GREEN + config.getPoisonDuration(),
								ChatColor.WHITE + "Cooldown: " + ChatColor.DARK_GREEN + TimeUnit.MILLISECONDS.toSeconds(config.getPoisonCooldown()) + "s",
								ChatColor.WHITE + "Additional Cooldown: " + ChatColor.DARK_GREEN + TimeUnit.MILLISECONDS.toSeconds(config.getPoisonAdditionalCooldown()) + "s",
								ChatColor.WHITE + "Cost: " + ChatColor.DARK_GREEN + ArrowTools.formatConsumption(getBaseEnergyConsumption()), 
								ChatColor.WHITE + "Additional Cost: " + ChatColor.DARK_GREEN + ArrowTools.formatConsumption(getAdditionalEnergyConsumption()))));
	}
	
	@Override
	public EnergyConsumption getBaseEnergyConsumption() {
		return new EnergyConsumption(config.getPoisonConsumption(), config.getPoisonEnergy());
	}
	
	@Override
	public EnergyConsumption getAdditionalEnergyConsumption() {
		return new EnergyConsumption(config.getAdditionalPoisonConsumption(), config.getAdditionalPoisonEnergy());
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDisplayName() {
		return ChatColor.DARK_GREEN + "Poison " + ChatColor.WHITE + "Arrow";
	}
	
	@Override
	public String getParticleColour() {
		return "#0F962B";
	}

	@Override
	public long getBaseCooldown() {
		return config.getPoisonCooldown();
	}
	
	@Override
	public long getAdditionalCooldown() {
		return config.getPoisonAdditionalCooldown();
	}

	@Override
	public PotionEffectType getPotionEffectType() {
		return PotionEffectType.POISON;
	}
	
	@Override
	public int getPotionDuration(ArcherPlayer player) {
		return (int) ((config.getPoisonDuration() * 20) * player.getEffectiveLinkedReduction());
	}
	
	@Override
	public double getExplosiveRadius(ArcherPlayer player) {
		return config.getPoisonExplosiveRadius() * player.getEffectiveLinkedReduction();
	}

}
