package com.github.maxopoly.finale.classes.archer.arrows;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.maxopoly.finale.classes.ability.AbilityPlayer;
import com.github.maxopoly.finale.classes.archer.AbstractArrow;
import com.github.maxopoly.finale.classes.archer.ArcherPlayer;
import com.github.maxopoly.finale.classes.archer.ArrowImpactForm;
import com.github.maxopoly.finale.classes.archer.ArrowTools;

import io.jayms.serenno.util.LocationTools;
import io.jayms.serenno.util.ParticleTools;
import net.md_5.bungee.api.ChatColor;

public abstract class PotionEffectArrow extends AbstractArrow {

	@Override
	public boolean enable(ArcherPlayer player, EntityShootBowEvent e) {
		return super.enable(player, e);
	}
	
	@Override
	public boolean update(AbilityPlayer player) {
		return true;
	}
	
	public abstract PotionEffectType getPotionEffectType();
	
	public abstract int getPotionDuration(ArcherPlayer player);
	
	public abstract double getExplosiveRadius(ArcherPlayer player);
	
	public abstract String getParticleColour();
	
	public int getPotionAmplifier() {
		return 0;
	}
	
	@Override
	public BukkitRunnable showTrailEffect(Arrow arrow) {
		return new BukkitRunnable() {
			
			@Override
			public void run() {
				ParticleTools.displayColoredParticle(arrow.getLocation(), getParticleColour());
			}
			
		};
	}

	@Override
	public void handleHit(ProjectileHitEvent e) {
		Arrow arrow = (Arrow) e.getEntity();
		ArcherPlayer player = ArrowTools.getArcherPlayer(arrow);
		ArrowImpactForm impact = player.getImpact();
		
		Block hitBlock = e.getHitBlock();
		Entity hitEntity = e.getHitEntity();
		if (impact == ArrowImpactForm.CONCENTRATED) {
			if (hitEntity == null || !(hitEntity instanceof LivingEntity)) {
				return;
			}
			LivingEntity le = (LivingEntity) hitEntity;
			
			potionAffect(player, le);
		} else if (impact == ArrowImpactForm.EXPLOSIVE) {
			Location explosiveLoc = null;
			if (hitBlock != null) {
				explosiveLoc = hitBlock.getLocation();
			}
			if (hitEntity != null) {
				explosiveLoc = hitEntity.getLocation();
			}
			if (explosiveLoc != null) {
				explode(player, explosiveLoc);
			}
		}
		disable(player);
	}
	
	public void potionAffect(ArcherPlayer player, LivingEntity hitEntity) {
		int duration = getPotionDuration(player);
		hitEntity.addPotionEffect(new PotionEffect(getPotionEffectType(), duration, getPotionAmplifier()));
		
		long durationMilli = (1000 / 20) * duration;
		long durationSeconds = TimeUnit.MILLISECONDS.toSeconds(durationMilli);
		
		if (hitEntity instanceof Player) {
			player.notify(ChatColor.YELLOW + "You have hit " + ChatColor.RED + hitEntity.getName() 
				+ ChatColor.YELLOW + " with " + getDisplayName() + ChatColor.YELLOW + " for " + ChatColor.GOLD + durationSeconds + "s");
		}
	}
	
	private Random random = new Random();
	
	public void explode(ArcherPlayer shooter, Location loc) {
		loc.getWorld().playSound(loc, Sound.BLOCK_GLASS_BREAK, 1f, 0.5f);
		
		double radius = getExplosiveRadius(shooter) * getArrowData(shooter).getForce();
		int increment = 10;
		for (double theta = 0; theta < 180; theta += increment) {
			for (double phi = 0; phi < 360; phi += increment) {
				if (random.nextInt(3) == 0) {
					final double rphi = Math.toRadians(phi);
					final double rtheta = Math.toRadians(theta);
	
					final Location display = loc.clone().add(radius / 1.5 * Math.cos(rphi) * Math.sin(rtheta), radius / 1.5 * Math.cos(rtheta), radius / 1.5 * Math.sin(rphi) * Math.sin(rtheta));
					for (int i = 0; i < 5; i++) {
						ParticleTools.displayColoredParticle(display, getParticleColour(), 0.4f, 0.4f, 0.4f);
					}
				}
			}
		}
		
		Collection<LivingEntity> livingEntities = LocationTools.getNearbyLivingEntities(loc, radius);
		for (LivingEntity le : livingEntities) {
			if (le instanceof Player) {
				Player player = (Player) le;
				if (player.getUniqueId().equals(shooter.getUniqueId()) || shooter.isLinked(player)) {
					continue;
				}
			}
			
			shooter.consumeEnergy(this.getAdditionalEnergyConsumption());
			potionAffect(shooter, le);
		}
		disable(shooter);
	}

}
