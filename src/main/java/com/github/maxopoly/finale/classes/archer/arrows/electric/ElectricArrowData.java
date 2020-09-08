package com.github.maxopoly.finale.classes.archer.arrows.electric;

import java.util.Collection;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;

import com.github.maxopoly.finale.classes.archer.ArcherPlayer;
import com.github.maxopoly.finale.classes.archer.arrows.explosive.ExplosiveArrowData;

public class ElectricArrowData extends ExplosiveArrowData {

	private Collection<LivingEntity> potentialTargets;
	
	public ElectricArrowData(ArcherPlayer archer, EntityShootBowEvent bowEvent) {
		super(archer, bowEvent);
	}
	
	public void setPotentialTargets(Collection<LivingEntity> potentialTargets) {
		this.potentialTargets = potentialTargets;
	}
	
	public void removePotentialTarget(LivingEntity le) {
		if (potentialTargets == null) return;
		potentialTargets.remove(le);
	}
	
	public Collection<LivingEntity> getPotentialTargets() {
		return potentialTargets;
	}
	
	

}
