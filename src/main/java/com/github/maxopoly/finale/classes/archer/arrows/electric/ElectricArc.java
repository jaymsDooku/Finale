package com.github.maxopoly.finale.classes.archer.arrows.electric;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.archer.ArcherConfig;
import com.github.maxopoly.finale.classes.archer.ArcherPlayer;

import io.jayms.serenno.util.MathTools;
import io.jayms.serenno.util.ParticleEffect;
import io.jayms.serenno.util.ParticleTools;
import io.jayms.serenno.util.ParticleTools.ParticlePlay;
import net.md_5.bungee.api.ChatColor;

public class ElectricArc extends BukkitRunnable {
	
	private static final double MAX_ANGLE = 45;
	
	private ElectricArrow arrow;
	private ElectricArrowData arrowData;
	private Location currentLoc;
	private LivingEntity target;
	
	public ElectricArc(ElectricArrow arrow, ElectricArrowData arrowData, Location startLoc, LivingEntity target) {
		this.arrow = arrow;
		this.arrowData = arrowData;
		this.currentLoc = startLoc.clone();
		this.target = target;
	}
	
	public void start() {
		this.runTaskTimer(Finale.getPlugin(), 0L, 1L);
	}
	
	private Random random = new Random();
	
	@Override
	public void run() {
		if (random.nextInt(3) == 0) {
			currentLoc.getWorld().playSound(currentLoc, Sound.ENTITY_CREEPER_HURT, 1f, 1f);
		}
		
		if (!arrowData.getArcher().hasEnoughEnergy(arrow.getAdditionalEnergyConsumption()) && arrow.isUsing(arrowData.getArcher())) {
			cancel();
			arrowData.getArcher().sendMessage(ChatColor.RED + "Your " + arrow.getDisplayName() + ChatColor.RED + " lost its charge due to a lack of energy!");
			arrow.disable(arrowData.getArcher());
			return;
		}
		
		if (arrowData.isAffected(target)) {
			cancel();
			return;
		}
		
		if (currentLoc.distanceSquared(target.getEyeLocation()) < 1) {
			affect(arrow, arrowData, target);
			cancel();
			return;
		}
		
		double angle = (Math.random() - 0.5) * MAX_ANGLE;
		angle += angle >= 0 ? 15 : -15;
		
		Vector dir = target.getEyeLocation().clone().subtract(currentLoc).toVector().normalize();
		dir = MathTools.rotateXZ(dir.clone(), angle).normalize();
		Location prevLoc = currentLoc;
		currentLoc = currentLoc.clone().add(dir);
		ParticleTools.drawLine(prevLoc, currentLoc, 3, new ParticlePlay() {
			
			@Override
			public void play(Location l) {
				ParticleTools.displayColoredParticle(l, "#1CA2C7");
				ParticleEffect.CRIT_MAGIC.display(l, 0, 0, 0, 0, 1);
			}
			
		});
	}
	
	public static void affect(ElectricArrow arrow, ElectricArrowData arrowData, LivingEntity livingVictim) {
		if (!arrowData.getArcher().hasEnoughEnergy(arrow.getAdditionalEnergyConsumption())) {
			return;
		}
		arrowData.getArcher().consumeEnergy(arrow.getAdditionalEnergyConsumption());
		arrow.addCooldown(arrowData.getArcher(), arrow.getAdditionalCooldown());
		
		ArcherConfig config = arrow.getConfig();
		
		ArcherPlayer archer = arrowData.getArcher();
		double minDamage = config.getElectricMinDamage() * archer.getEffectiveLinkedReduction();
		double maxDamage = config.getElectricMaxDamage() * archer.getEffectiveLinkedReduction();
		
		double minHorizontal = config.getElectricMinHorizontal() * archer.getEffectiveLinkedReduction();
		double maxHorizontal = config.getElectricMaxHorizontal() * archer.getEffectiveLinkedReduction();
		
		double minVertical = config.getElectricMinVertical() * archer.getEffectiveLinkedReduction();
		double maxVertical = config.getElectricMaxVertical() * archer.getEffectiveLinkedReduction();
		
		livingVictim.setFireTicks(20 * 3);
		livingVictim.damage(MathTools.random(minDamage, maxDamage), arrowData.getArcher().getBukkitPlayer());
		livingVictim.setVelocity(new Vector(MathTools.random(-minHorizontal, maxHorizontal),
				MathTools.random(minVertical, maxVertical),
				MathTools.random(-minHorizontal, maxHorizontal)));
		arrowData.removePotentialTarget(livingVictim);
		arrowData.addAffected(livingVictim);
		
		if (arrowData.getPotentialTargets().isEmpty() || arrowData.numberOfAffected() >= ((int)(config.getElectricMaxVictims() * archer.getEffectiveLinkedReduction()))) {
			arrow.disable(arrowData.getArcher());
			return;
		}
		
		for (LivingEntity potVictim : arrowData.getPotentialTargets()) {
			if (potVictim.getLocation().distanceSquared(livingVictim.getLocation()) <= config.getElectricChainRange() * config.getElectricChainRange()) {
				new ElectricArc(arrow, arrowData, livingVictim.getEyeLocation(), potVictim).runTaskTimer(Finale.getPlugin(), 0L, 1L);
			}
		}
	}

}
