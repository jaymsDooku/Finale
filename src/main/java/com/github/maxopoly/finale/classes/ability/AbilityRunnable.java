package com.github.maxopoly.finale.classes.ability;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AbilityRunnable implements Runnable {

	private final AbilityManager am;
	
	public AbilityRunnable(AbilityManager am) {
		this.am = am;
	}
	
	@Override
	public void run() {
		Collection<Ability> abilities = am.getRegisteredAbilities();
		
		if (abilities.isEmpty()) return;
		
		for (Ability ability : abilities) {
			Set<AbilityPlayer> using = ability.getUsing();
			if (using.isEmpty()) continue;
			
			Set<AbilityPlayer> toRemove = null;
			for (AbilityPlayer user : using) {
				if (!ability.update(user)) {
					if (toRemove == null) toRemove = new HashSet<>();
					toRemove.add(user);
				}
			}
			if (toRemove != null) {
				for (AbilityPlayer removing : toRemove) {
					using.remove(removing);
				}
			}
		}
	}
	
}
