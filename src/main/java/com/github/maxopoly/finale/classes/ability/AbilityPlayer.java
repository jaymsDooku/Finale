package com.github.maxopoly.finale.classes.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.github.maxopoly.finale.classes.ClassType;
import com.google.common.collect.Maps;

public class AbilityPlayer {

	protected final Player bukkitPlayer;
	private Map<ClassType, AbilityPlayer> classPlayers = Maps.newConcurrentMap();
	private Map<String, Object> abilityData;
	
	public AbilityPlayer(Player bukkitPlayer) {
		this.bukkitPlayer = bukkitPlayer;
	}
	
	public Map<String, Object> getAbilityData() {
		if (abilityData == null) {
			abilityData = new HashMap<>();
		}
		return abilityData;
	}
	
	public void putClassPlayer(ClassType type, AbilityPlayer abPlayer) {
		classPlayers.put(type, abPlayer);
	}
	
	public AbilityPlayer removeClassPlayer(ClassType type) {
		return classPlayers.remove(type);
	}
	
	public AbilityPlayer getClassPlayer(ClassType type) {
		return classPlayers.get(type);
	}
	
	public boolean hasClassPlayer(ClassType type) {
		return classPlayers.containsKey(type);
	}
	
	public boolean hasClassPlayer(Set<ClassType> type) {
		for (ClassType ct : type) {
			if (hasClassPlayer(ct)) {
				return true;
			}
		}
		return false;
	}
	
	public Player getBukkitPlayer() {
		return bukkitPlayer;
	}
	
	public UUID getUniqueId() {
		return bukkitPlayer.getUniqueId();
	}
	
	public void sendMessage(String message) {
		bukkitPlayer.sendMessage(message);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbilityPlayer)) {
			return false;
		}
		
		AbilityPlayer abilityPlayer = (AbilityPlayer) obj;
		return abilityPlayer.bukkitPlayer.equals(this.bukkitPlayer);
	}
	
	@Override
	public int hashCode() {
		return bukkitPlayer.hashCode();
	}
	
}
