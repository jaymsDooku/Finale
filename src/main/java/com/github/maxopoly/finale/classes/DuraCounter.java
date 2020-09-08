package com.github.maxopoly.finale.classes;

public class DuraCounter {

	private int counter;
	private int threshold;
	
	public DuraCounter(int threshold) {
		this.threshold = threshold;
	}
	
	public boolean canBreak() {
		if (counter > threshold) {
			counter = 0;
			return true;
		}
		counter++;
		return false;
	}
	
	public int getCounter() {
		return counter;
	}
	
	public int getThreshold() {
		return threshold;
	}
	
}
