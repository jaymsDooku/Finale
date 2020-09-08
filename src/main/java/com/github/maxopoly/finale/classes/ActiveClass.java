package com.github.maxopoly.finale.classes;

import org.bukkit.entity.Player;

public class ActiveClass {

	private Player player;
	private ClassType type;
	private long timeActivated = System.currentTimeMillis();
	
	public ActiveClass(Player player, ClassType type) {
		this.player = player;
		this.type = type;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public ClassType getType() {
		return type;
	}
	
	public long getTimeActivated() {
		return timeActivated;
	}
	
}
