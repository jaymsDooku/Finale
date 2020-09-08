package com.github.maxopoly.finale.classes.archer.arrows.hunter;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.github.maxopoly.finale.classes.archer.ArcherPlayer;
import com.github.maxopoly.finale.classes.archer.ArrowTools;
import com.google.common.collect.Maps;

import io.jayms.serenno.util.Cooldown;
import net.md_5.bungee.api.ChatColor;

public class HunterMarks {

	private final HunterArrow arrow;
	
	private Cooldown<UUID> hunterMarkDurations = new Cooldown<>();
	private Map<UUID, Mark> hunterMarks = Maps.newConcurrentMap();
	
	public HunterMarks(HunterArrow arrow) {
		this.arrow = arrow;
	}
	
	public void mark(ArcherPlayer archer, LivingEntity hitEntity) {
		float force = arrow.getArrowData(archer).getForce();
		float forceDuration = (arrow.getConfig().getHuntersMarkDuration() * force);
		long lForceDuration = (long) forceDuration;
		long finalDuration = arrow.getConfig().getHuntersMarkMinDuration() + lForceDuration;
		
		double hunterDamage = arrow.getConfig().getHuntersMarkDamage();
		hunterDamage *= archer.getEffectiveLinkedReduction();
		
		Mark mark = new Mark(archer, hitEntity, hunterDamage, finalDuration);
		
		hunterMarkDurations.putOnCooldown(hitEntity.getUniqueId(), finalDuration);
		hunterMarks.put(hitEntity.getUniqueId(), mark);
		
		String durationStr = TimeUnit.MILLISECONDS.toSeconds(finalDuration) + "s";
		archer.notify(ChatColor.YELLOW + "You have " + ChatColor.RED + "marked" 
				+ ChatColor.DARK_RED + "(" + ChatColor.RED + "+" + getDamageDisplay(hunterDamage) + ChatColor.DARK_RED + ")" + ChatColor.GOLD + hitEntity.getName() + ChatColor.YELLOW + " for " + ChatColor.RED + durationStr);
		if (hitEntity instanceof Player) {
			Player hitPlayer = (Player) hitEntity;
			ArrowTools.archerMessage(hitPlayer, ChatColor.YELLOW + "You have been " + ChatColor.RED + "marked" 
					+ ChatColor.DARK_RED + "(" + ChatColor.RED + "+" + getDamageDisplay(hunterDamage) + ChatColor.DARK_RED + ")" + ChatColor.YELLOW + " by " + ChatColor.RED + archer.getBukkitPlayer().getName() + ChatColor.YELLOW + " for " + ChatColor.RED + durationStr);
			ArrowTools.linkedBroadcast(archer, ChatColor.RED + hitPlayer.getName() + ChatColor.YELLOW + " has been " + ChatColor.RED + "marked" 
					+ ChatColor.DARK_RED + "(" + ChatColor.RED + "+" + getDamageDisplay(hunterDamage) + ChatColor.DARK_RED + ")" + ChatColor.YELLOW + " for " + ChatColor.RED + durationStr);
		}
	}
	
	private DecimalFormat dp1 = new DecimalFormat("#.#");
	
	private String getDamageDisplay(double damage) {
		return (dp1.format((damage - 1.0D) * 100.0D)) + "%";
	}

	public double getMarkDurationLeft(LivingEntity entity) {
		return hunterMarkDurations.getReadableTimeLeft(entity.getUniqueId());
	}
	
	public boolean isMarked(LivingEntity hitEntity) {
		return hunterMarkDurations.isOnCooldown(hitEntity.getUniqueId());
	}
	
	public Mark getMark(LivingEntity hitEntity) {
		if (!isMarked(hitEntity)) {
			if (hunterMarks.containsKey(hitEntity.getUniqueId()))
				hunterMarks.remove(hitEntity.getUniqueId());
			return null;
		}
		
		return hunterMarks.get(hitEntity.getUniqueId());
	}
	
}
