package com.github.maxopoly.finale.classes.archer;

import com.github.maxopoly.finale.classes.ConsumptionType;

public class EnergyConsumption {

	private ConsumptionType type;
	private double amount;
	
	public EnergyConsumption(ConsumptionType type, double amount) {
		this.type = type;
		this.amount = amount;
	}
	
	public void add(double a) {
		amount += a;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public ConsumptionType getType() {
		return type;
	}
	
}
