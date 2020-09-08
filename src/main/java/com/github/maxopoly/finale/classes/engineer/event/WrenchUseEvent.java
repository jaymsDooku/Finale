package com.github.maxopoly.finale.classes.engineer.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.maxopoly.finale.classes.engineer.EngineerPlayer;

public class WrenchUseEvent extends Event {

	private static final HandlerList handlers = new HandlerList(); 
	
	private EngineerPlayer user;
	private PlayerInteractEvent interactEvent;
	
	public WrenchUseEvent(EngineerPlayer user, PlayerInteractEvent interactEvent) {
		this.user = user;
		this.interactEvent = interactEvent;
	}
	
	public EngineerPlayer getUser() {
		return user;
	}
	
	public PlayerInteractEvent getInteractEvent() {
		return interactEvent;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
