package com.github.maxopoly.finale.classes.archer.arrows.explosive;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.github.maxopoly.finale.classes.ability.AbilityPlayer;
import com.github.maxopoly.finale.classes.archer.AbstractArrow;
import com.github.maxopoly.finale.classes.archer.ArcherPlayer;
import com.github.maxopoly.finale.classes.archer.ArrowTools;
import com.github.maxopoly.finale.classes.archer.EnergyConsumption;

import io.jayms.serenno.kit.ItemMetaBuilder;
import io.jayms.serenno.kit.ItemStackBuilder;
import io.jayms.serenno.util.LocationTools;
import io.jayms.serenno.util.ParticleEffect;
import net.md_5.bungee.api.ChatColor;

public class ExplosiveArrow extends AbstractArrow {

	public static final String NAME = "explosive-arrow";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDisplayName() {
		return ChatColor.DARK_RED + "Explosive " + ChatColor.WHITE + "Arrow";
	}

	@Override
	public ItemStackBuilder newDisplayItemBuilder() {
		return new ItemStackBuilder(Material.TNT, 1)
				.meta(new ItemMetaBuilder()
						.name(getDisplayName())
						.lore(Arrays.asList(ChatColor.DARK_RED + "Boom!",
								ChatColor.WHITE + "Damage: " + ChatColor.DARK_RED + config.getExplosiveDamage(),
								ChatColor.WHITE + "Impact: " + ChatColor.RED + "(" 
										+ ChatColor.DARK_RED + config.getExplosiveHorizontal() + ChatColor.RED + ", "
										+ ChatColor.DARK_RED + config.getExplosiveVertical() + ChatColor.RED + ", "
										+ ChatColor.DARK_RED + config.getExplosiveHorizontal() + ChatColor.RED + ", "
										+ ChatColor.RED + ")",
								ChatColor.WHITE + "Cooldown: " + ChatColor.DARK_RED + TimeUnit.MILLISECONDS.toSeconds(config.getExplosiveCooldown()),
								ChatColor.WHITE + "Additional Cooldown: " + ChatColor.DARK_RED + TimeUnit.MILLISECONDS.toSeconds(config.getExplosiveAdditionalCooldown()),
								ChatColor.WHITE + "Cost: " + ChatColor.DARK_RED + ArrowTools.formatConsumption(getBaseEnergyConsumption()),
								ChatColor.WHITE + "Additional Cost: " + ChatColor.DARK_RED + ArrowTools.formatConsumption(getAdditionalEnergyConsumption()))));
	}

	@Override
	public EnergyConsumption getBaseEnergyConsumption() {
		return new EnergyConsumption(config.getExplosiveConsumption(), config.getExplosiveEnergy());
	}
	
	@Override
	public EnergyConsumption getAdditionalEnergyConsumption() {
		return new EnergyConsumption(config.getAdditionalExplosiveConsumption(), config.getAdditionalExplosiveEnergy());
	}
	
	@Override
	public BukkitRunnable showTrailEffect(Arrow arrow) {
		return new BukkitRunnable() {
			
			@Override
			public void run() {
				ParticleEffect.CLOUD.display(arrow.getLocation(), 0, 0, 0, 0, 1);
			}
			
		};
	}

	@Override
	public boolean update(AbilityPlayer player) {
		return true;
	}

	@Override
	public void handleHit(ProjectileHitEvent e) {
		if (e.getHitEntity() == null && e.getHitBlock() == null) {
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
		
		Location explosiveLoc = arrow.getLocation();
		explode(archer, explosiveLoc);
	}
	
	public void explode(ArcherPlayer shooter, Location loc) {
		loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.5f);
		
		ParticleEffect.EXPLOSION_HUGE.display(loc, 0.2f, 0.2f, 0.2f, 0, 5);
		ParticleEffect.CLOUD.display(loc, 1f, 1f, 1f, 0, 5);
		
		Collection<LivingEntity> livingEntities = LocationTools.getNearbyLivingEntities(loc, config.getExplosiveRadius() * getArrowData(shooter).getForce() * shooter.getEffectiveLinkedReduction());
		for (LivingEntity le : livingEntities) {
			if (le instanceof Player) {
				Player player = (Player) le;
				if (player.getUniqueId().equals(shooter.getUniqueId()) || shooter.isLinked(player)) {
					continue;
				}
			}
			shooter.consumeEnergy(getAdditionalEnergyConsumption());
			addCooldown(shooter, getAdditionalCooldown());
			
			le.damage(config.getExplosiveDamage() * shooter.getEffectiveLinkedReduction(), shooter.getBukkitPlayer());
			
			Vector dir = loc.clone().subtract(le.getEyeLocation()).toVector().normalize().multiply(-1);
			dir = dir.add(new Vector(0, config.getExplosiveVertical() * shooter.getEffectiveLinkedReduction(), 0));
			dir = dir.multiply(new Vector(config.getExplosiveHorizontal() * shooter.getEffectiveLinkedReduction(), 1.0, config.getExplosiveHorizontal() * shooter.getEffectiveLinkedReduction()));
			le.setVelocity(dir);
		}
		disable(shooter);
	}

	@Override
	public long getBaseCooldown() {
		return config.getExplosiveCooldown();
	}
	
	@Override
	public long getAdditionalCooldown() {
		return config.getExplosiveAdditionalCooldown();
	}
	
}
