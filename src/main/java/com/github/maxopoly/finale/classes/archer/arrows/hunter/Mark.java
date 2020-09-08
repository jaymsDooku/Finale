package com.github.maxopoly.finale.classes.archer.arrows.hunter;

import org.bukkit.entity.LivingEntity;

import com.github.maxopoly.finale.classes.archer.ArcherPlayer;

public class Mark {

	private ArcherPlayer marker;
	private LivingEntity marked;
	private double damage;
	private long duration;
	
	public Mark(ArcherPlayer marker, LivingEntity marked, double damage, long duration) {
		this.marker = marker;
		this.marked = marked;
		this.damage = damage;
		this.duration = duration;
	}
	
	public ArcherPlayer getMarker() {
		return marker;
	}
	
	public LivingEntity getMarked() {
		return marked;
	}
	
	public double getDamage() {
		return damage;
	}
	
	public long getDuration() {
		return duration;
	}
	
}
