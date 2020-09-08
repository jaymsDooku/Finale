package com.github.maxopoly.finale.classes.archer;

import org.bukkit.event.entity.EntityShootBowEvent;

public class ArrowData {

	private ArcherPlayer archer;
	private EntityShootBowEvent bowEvent;
	
	public ArrowData(ArcherPlayer archer, EntityShootBowEvent bowEvent) {
		this.archer = archer;
		this.bowEvent = bowEvent;
	}
	
	public ArcherPlayer getArcher() {
		return archer;
	}
	
	public org.bukkit.entity.Arrow getArrow() {
		return (org.bukkit.entity.Arrow) bowEvent.getProjectile();
	}
	
	public float getForce() {
		return bowEvent.getForce();
	}
	
}
