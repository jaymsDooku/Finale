package com.github.maxopoly.finale.classes.archer;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.ClassType;
import com.github.maxopoly.finale.classes.ability.AbilityManager;
import com.github.maxopoly.finale.classes.ability.AbilityPlayer;

public class ArcherEnergyRegenRunnable implements Runnable {

	private AbilityManager am = Finale.getPlugin().getAbilityManager();
	
	@Override
	public void run() {
		for (AbilityPlayer ap : am.getAllAbilityPlayers()) {
			if (ap.hasClassPlayer(ClassType.ARCHER)) {
				ArcherPlayer archer = (ArcherPlayer) ap.getClassPlayer(ClassType.ARCHER);
				archer.regenerateEnergy();
			}
		}
	}
	
}
