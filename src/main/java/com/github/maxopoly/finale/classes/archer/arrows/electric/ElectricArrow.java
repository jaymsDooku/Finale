package com.github.maxopoly.finale.classes.archer.arrows.electric;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.ability.AbilityPlayer;
import com.github.maxopoly.finale.classes.archer.AbstractArrow;
import com.github.maxopoly.finale.classes.archer.ArcherPlayer;
import com.github.maxopoly.finale.classes.archer.ArrowData;
import com.github.maxopoly.finale.classes.archer.ArrowTools;
import com.github.maxopoly.finale.classes.archer.EnergyConsumption;

import io.jayms.serenno.kit.ItemMetaBuilder;
import io.jayms.serenno.kit.ItemStackBuilder;
import io.jayms.serenno.util.LocationTools;
import io.jayms.serenno.util.ParticleTools;
import net.md_5.bungee.api.ChatColor;

public class ElectricArrow extends AbstractArrow {

	public static final String NAME = "electric-arrow";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDisplayName() {
		return ChatColor.AQUA + "Electric " + ChatColor.WHITE + "Arrow";
	}

	@Override
	public ItemStackBuilder newDisplayItemBuilder() {
		return new ItemStackBuilder(Material.DIAMOND, 1)
				.meta(new ItemMetaBuilder()
						.name(getDisplayName())
						.lore(Arrays.asList(ChatColor.AQUA + "Give your opponents a good ol' shock.",
								ChatColor.WHITE + "Damage: " + ChatColor.AQUA + config.getElectricMinDamage() + ChatColor.WHITE + " - " + ChatColor.AQUA + config.getElectricMaxDamage(),
								ChatColor.WHITE + "Impact: " + ChatColor.BLUE + "(" 
										+ ChatColor.AQUA + config.getElectricMinHorizontal() + ChatColor.WHITE + " - " + ChatColor.AQUA + config.getElectricMaxHorizontal() + ChatColor.BLUE + ", "
										+ ChatColor.AQUA + config.getElectricMinVertical() + ChatColor.WHITE + " - " + ChatColor.AQUA + config.getElectricMaxVertical() + ChatColor.BLUE + ", "
										+ ChatColor.AQUA + config.getElectricMinHorizontal() + ChatColor.WHITE + " - " + ChatColor.AQUA + config.getElectricMaxHorizontal()
										+ ChatColor.BLUE + ")",
								ChatColor.WHITE + "Max Victims: " + ChatColor.AQUA + config.getElectricMaxVictims(),
								ChatColor.WHITE + "Chain Range: " + ChatColor.AQUA + config.getElectricChainRange(),
								ChatColor.WHITE + "Cooldown: " + ChatColor.AQUA + TimeUnit.MILLISECONDS.toSeconds(config.getElectricCooldown()),
								ChatColor.WHITE + "Additional Cooldown: " + ChatColor.AQUA + TimeUnit.MILLISECONDS.toSeconds(config.getElectricAdditionalCooldown()),
								ChatColor.WHITE + "Cost: " + ChatColor.AQUA + ArrowTools.formatConsumption(getBaseEnergyConsumption()),
								ChatColor.WHITE + "Additional Cost: " + ChatColor.AQUA + ArrowTools.formatConsumption(getAdditionalEnergyConsumption()))));
	}
	
	@Override
	public EnergyConsumption getBaseEnergyConsumption() {
		return new EnergyConsumption(config.getElectricConsumption(), config.getElectricEnergy());
	}
	
	@Override
	public EnergyConsumption getAdditionalEnergyConsumption() {
		return new EnergyConsumption(config.getAdditionalElectricConsumption(), config.getAdditionalElectricEnergy());
	}
	
	@Override
	public BukkitRunnable showTrailEffect(Arrow arrow) {
		return new BukkitRunnable() {
			
			@Override
			public void run() {
				ParticleTools.displayColoredParticle(arrow.getLocation(), "#01E1FF");
			}
			
		};
	}
	
	@Override
	public ArrowData instantiateArrowData(ArcherPlayer player, EntityShootBowEvent e) {
		return new ElectricArrowData(player, e);
	}

	@Override
	public boolean update(AbilityPlayer player) {
		return true;
	}

	@Override
	public void handleHit(ProjectileHitEvent e) {
		Entity hitEntity = e.getHitEntity();
		if (hitEntity == null || !(hitEntity instanceof LivingEntity)) {
			return;
		}
		
		if (!(e.getEntity() instanceof Arrow)) {
			return;
		}
		Arrow arrow = (Arrow) e.getEntity();
		if (!ArrowTools.firedByArcher(arrow)) {
			return;
		}
		
		ArcherPlayer archer = ArrowTools.getArcherPlayer(arrow);
		
		if (!isUsing(archer)) {
			return;
		}
		
		ElectricArrowData arrowData = (ElectricArrowData) getArrowData(archer);
		
		LivingEntity livingVictim = (LivingEntity) hitEntity;
		Collection<LivingEntity> nearbyLivingEntities = LocationTools.getNearbyLivingEntities(livingVictim.getEyeLocation(), config.getElectricFullRange());
		Iterator<LivingEntity> nlEntitiesIt = nearbyLivingEntities.iterator();
		while (nlEntitiesIt.hasNext()) {
			LivingEntity le = nlEntitiesIt.next();
			if (le instanceof Player) {
				Player player = (Player) le;
				if (player.getUniqueId().equals(archer.getUniqueId()) || archer.isLinked(player)) {
					nlEntitiesIt.remove();
				}
			}
		}
		arrowData.setPotentialTargets(nearbyLivingEntities);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				new ElectricArc(ElectricArrow.this, arrowData, livingVictim.getLocation(), livingVictim).start();
			}
			
		}.runTaskLater(Finale.getPlugin(), 1L);
	}

	@Override
	public long getBaseCooldown() {
		return config.getElectricCooldown();
	}
	
	@Override
	public long getAdditionalCooldown() {
		return config.getElectricAdditionalCooldown();
	}
	
}
