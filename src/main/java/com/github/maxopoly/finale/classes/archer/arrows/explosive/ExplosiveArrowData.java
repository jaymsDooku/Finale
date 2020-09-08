package com.github.maxopoly.finale.classes.archer.arrows.explosive;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;

import com.github.maxopoly.finale.classes.archer.ArcherPlayer;
import com.github.maxopoly.finale.classes.archer.ArrowData;

public class ExplosiveArrowData extends ArrowData {

	private Set<LivingEntity> affected = new HashSet<>();
	
	public ExplosiveArrowData(ArcherPlayer archer, EntityShootBowEvent bowEvent) {
		super(archer, bowEvent);
	}
	
	public void addAffected(LivingEntity le) {
		affected.add(le);
	}
	
	public boolean isAffected(LivingEntity le) {
		return affected.contains(le);
	}
	
	public int numberOfAffected() {
		return affected.size();
	}
	
}
