package com.github.maxopoly.finale.classes.ability;

import java.util.Set;
import java.util.UUID;

import io.jayms.serenno.util.Cooldown;

public interface Ability {

	String getName();
	
	String getDisplayName();
	
	boolean isUsing(AbilityPlayer user);
	
	boolean enable(AbilityPlayer user, boolean instantCD);
	
	void disable(AbilityPlayer user);
	
	void putOnCooldown(AbilityPlayer user);
	
	void addCooldown(AbilityPlayer user, long cooldown);
	
	// return false if not continuing to next update
	boolean update(AbilityPlayer user);
	
	Set<AbilityPlayer> getUsing();
	
	long getBaseCooldown();
	
	long getAdditionalCooldown();
	
	Cooldown<UUID> getCooldowns();
	
}
