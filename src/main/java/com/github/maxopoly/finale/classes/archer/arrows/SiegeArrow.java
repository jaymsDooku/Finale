package com.github.maxopoly.finale.classes.archer.arrows;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.maxopoly.finale.classes.ability.AbilityPlayer;
import com.github.maxopoly.finale.classes.archer.AbstractArrow;
import com.github.maxopoly.finale.classes.archer.ArcherPlayer;
import com.github.maxopoly.finale.classes.archer.ArrowTools;
import com.github.maxopoly.finale.classes.archer.EnergyConsumption;

import io.jayms.serenno.kit.ItemMetaBuilder;
import io.jayms.serenno.kit.ItemStackBuilder;
import io.jayms.serenno.util.ParticleEffect;
import net.md_5.bungee.api.ChatColor;

public class SiegeArrow extends AbstractArrow {

	public static final String NAME = "siege-arrow";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDisplayName() {
		return ChatColor.BLUE + "Siege " + ChatColor.WHITE + "Arrow";
	}

	@Override
	public ItemStackBuilder newDisplayItemBuilder() {
		return new ItemStackBuilder(Material.ANVIL, 1)
				.meta(new ItemMetaBuilder()
						.name(getDisplayName())
						.lore(Arrays.asList(ChatColor.DARK_RED + "Destroy reinforcements and bastions.")));
	}

	@Override
	public EnergyConsumption getBaseEnergyConsumption() {
		return new EnergyConsumption(config.getSiegeConsumption(), config.getSiegeEnergy());
	}
	
	@Override
	public EnergyConsumption getAdditionalEnergyConsumption() {
		return new EnergyConsumption(config.getAdditionalSiegeConsumption(), config.getAdditionalSiegeEnergy());
	}
	
	@Override
	public BukkitRunnable showTrailEffect(Arrow arrow) {
		return new BukkitRunnable() {
			
			@Override
			public void run() {
				ParticleEffect.ENCHANTMENT_TABLE.display(arrow.getLocation(), 0, 0, 0, 0, 1);
			}
			
		};
	}

	@Override
	public boolean update(AbilityPlayer player) {
		return true;
	}

	@Override
	public void handleHit(ProjectileHitEvent e) {
	}

	@Override
	public long getBaseCooldown() {
		return config.getSiegeCooldown();
	}
	
	@Override
	public long getAdditionalCooldown() {
		return config.getSiegeAdditionalCooldown();
	}
	
}

