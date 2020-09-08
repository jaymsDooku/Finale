package com.github.maxopoly.finale.classes.archer.event;

import com.github.maxopoly.finale.classes.archer.Arrow;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.github.maxopoly.finale.classes.archer.ArcherPlayer;
import com.github.maxopoly.finale.classes.archer.arrows.SiegeArrow;

public class ArrowHitEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList(); 
	
	private ArcherPlayer shooter;
	private ProjectileHitEvent hitEvent;
	private Arrow arrow;
	
	public ArrowHitEvent(ArcherPlayer shooter, ProjectileHitEvent hitEvent, Arrow siegeArrow) {
		this.shooter = shooter;
		this.hitEvent = hitEvent;
		this.arrow = siegeArrow;
	}
	
	public ArcherPlayer getShooter() {
		return shooter;
	}
	
	public ProjectileHitEvent getHitEvent() {
		return hitEvent;
	}
	
	public Arrow getArrow() {
		return arrow;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
