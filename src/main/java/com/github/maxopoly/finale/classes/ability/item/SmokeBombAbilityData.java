package com.github.maxopoly.finale.classes.ability.item;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class SmokeBombAbilityData {

	private Location origin;
	private Location location;
	private Vector velocity;
	private double power;
	private double gravity;
	
	public SmokeBombAbilityData(Location location, Vector direction, double power, double gravity) {
		this.origin = location.clone();
		this.velocity = direction.clone().multiply(power);
		this.location = location;
		this.power = power;
		this.gravity = gravity;
	}

	public Location getOrigin() {
		return origin;
	}

	public void setOrigin(Location origin) {
		this.origin = origin;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public double getGravity() {
		return gravity;
	}

	public void setGravity(double gravity) {
		this.gravity = gravity;
	}
	
}
