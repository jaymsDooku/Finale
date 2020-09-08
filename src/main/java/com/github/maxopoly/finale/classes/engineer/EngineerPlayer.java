package com.github.maxopoly.finale.classes.engineer;

import com.github.maxopoly.finale.classes.ability.AbilityPlayer;

public class EngineerPlayer extends AbilityPlayer {

	private AbilityPlayer parentPlayer;
	
	public EngineerPlayer(AbilityPlayer parentPlayer) {
		super(parentPlayer.getBukkitPlayer());
		this.parentPlayer = parentPlayer;
	}
	
	public void notify(String message) {
		EngineerTools.engineerMessage(getBukkitPlayer(), message);
	}

}
