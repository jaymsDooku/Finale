package com.github.maxopoly.finale.classes.ability.item;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.ClassType;
import com.github.maxopoly.finale.classes.ability.AbilityPlayer;
import com.github.maxopoly.finale.classes.ability.ClickType;
import com.github.maxopoly.finale.classes.archer.ArcherPlayer;
import com.github.maxopoly.finale.classes.engineer.EngineerConfig;
import com.github.maxopoly.finale.classes.engineer.EngineerPlayer;
import com.github.maxopoly.finale.classes.engineer.EngineerTools;
import com.google.common.collect.Sets;

import io.jayms.serenno.util.LocationTools;
import io.jayms.serenno.util.ParticleEffect;
import net.md_5.bungee.api.ChatColor;

public class SmokeBombAbility extends ItemAbility {

	public static final String NAME = "smoke-bomb";
	
	private EngineerConfig config = Finale.getPlugin().getManager().getEngineerConfig();
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDisplayName() {
		return ChatColor.DARK_GRAY + "Smoke Bomb";
	}
	
	@Override
	public boolean enable(AbilityPlayer user, boolean instantCD) {
		Player player = user.getBukkitPlayer();
		
		Map<String, Object> abilityData = user.getAbilityData();
		abilityData.put(NAME, new SmokeBombAbilityData(player.getEyeLocation(), player.getEyeLocation().getDirection(),
				config.getSmokeBombPower(), config.getSmokeBombGravity()));
		return super.enable(user, instantCD);
	}

	@Override
	public boolean update(AbilityPlayer user) {
		Map<String, Object> abilityData = user.getAbilityData();
		if (!abilityData.containsKey(NAME)) {
			return false;
		}
		SmokeBombAbilityData smokeBombData = (SmokeBombAbilityData) abilityData.get(NAME);
		Location loc = smokeBombData.getLocation();
		Vector velocity = smokeBombData.getVelocity();
		velocity = velocity.add(new Vector(0, smokeBombData.getGravity(), 0));
		loc = loc.add(velocity);
		ParticleEffect.SMOKE.display(loc, 0.4f, 0.4f, 0.4f, 0, 5);
		ParticleEffect.SMOKE_LARGE.display(loc, 0.4f, 0.4f, 0.4f, 0, 5);
		
		if (loc.getBlock().getType().isSolid()) {
			explode(EngineerTools.getEngineer(user.getBukkitPlayer()), loc);
			return false;
		}
		return true;
	}
	
	private Random random = new Random();
	
	public void potionAffect(EngineerPlayer thrower, LivingEntity hitEntity) {
		int duration = config.getSmokeBombDuration();
		hitEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration * 20, 0));
		
		long durationMilli = (1000 / 20) * duration;
		long durationSeconds = TimeUnit.MILLISECONDS.toSeconds(durationMilli);
		
		if (hitEntity instanceof Player) {
			thrower.notify(ChatColor.YELLOW + "You have hit " + ChatColor.RED + hitEntity.getName() 
				+ ChatColor.YELLOW + " with " + getDisplayName() + ChatColor.YELLOW + " for " + ChatColor.GOLD + durationSeconds + "s");
		}
	}
	
	public void explode(EngineerPlayer thrower, Location loc) {
		loc.getWorld().playSound(loc, Sound.BLOCK_GLASS_BREAK, 1f, 0.2f);
		
		double radius = config.getSmokeBombRadius();
		int increment = 10;
		for (double theta = 0; theta < 180; theta += increment) {
			for (double phi = 0; phi < 360; phi += increment) {
				if (random.nextInt(3) == 0) {
					final double rphi = Math.toRadians(phi);
					final double rtheta = Math.toRadians(theta);
	
					final Location display = loc.clone().add(radius / 1.5 * Math.cos(rphi) * Math.sin(rtheta), radius / 1.5 * Math.cos(rtheta), radius / 1.5 * Math.sin(rphi) * Math.sin(rtheta));
					ParticleEffect.SMOKE.display(display, 0.4f, 0.4f, 0.4f, 0, 5);
				}
			}
		}
		
		Collection<LivingEntity> livingEntities = LocationTools.getNearbyLivingEntities(loc, radius);
		for (LivingEntity le : livingEntities) {
			potionAffect(thrower, le);
		}
	}

	@Override
	public long getBaseCooldown() {
		return config.getSmokeBombCooldown();
	}

	@Override
	public Material getRequiredMaterial() {
		return Material.FIREWORK_CHARGE;
	}

	@Override
	public int getRequiredAmount() {
		return 1;
	}

	@Override
	public Set<ClassType> getValidClasses() {
		return Sets.newHashSet(ClassType.ENGINEER);
	}
	
	@Override
	public ClickType getActivateClickType() {
		return ClickType.RIGHT;
	}
	
	@Override
	public boolean consumeOnUse() {
		return true;
	}

}
