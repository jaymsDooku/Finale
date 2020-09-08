package com.github.maxopoly.finale.classes.archer.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.maxopoly.finale.classes.archer.ArcherPlayer;

public class ArcherLinkPlayerEvent extends Event {

	public enum LinkType { 
		LINK, UNLINK;
	}
	
	private static final HandlerList handlers = new HandlerList(); 
	
	private ArcherPlayer archer;
	private Player linked;
	private LinkType linkType;
	
	public ArcherLinkPlayerEvent(ArcherPlayer archer, Player linked, LinkType linkType) {
		this.archer = archer;
		this.linked = linked;
		this.linkType = linkType;
	}
	
	public ArcherPlayer getArcher() {
		return archer;
	}
	
	public Player getLinked() {
		return linked;
	}
	
	public LinkType getLinkType() {
		return linkType;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
