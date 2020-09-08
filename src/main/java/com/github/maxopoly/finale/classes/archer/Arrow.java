package com.github.maxopoly.finale.classes.archer;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.github.maxopoly.finale.classes.ability.Ability;

import io.jayms.serenno.kit.ItemStackBuilder;

public interface Arrow extends Ability {
	
	ItemStackBuilder getDisplayItemBuilder();
	
	EnergyConsumption getBaseEnergyConsumption();
	
	EnergyConsumption getAdditionalEnergyConsumption();
	
	boolean enable(ArcherPlayer player, EntityShootBowEvent e);
	
	void handleHit(ProjectileHitEvent e);
	
	org.bukkit.entity.Arrow getArrow(ArcherPlayer player);
	
	ArrowData getArrowData(ArcherPlayer player);
	
	ArcherPlayer getShooter(Entity entity);
	
}
