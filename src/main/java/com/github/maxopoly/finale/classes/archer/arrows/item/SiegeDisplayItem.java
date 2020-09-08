package com.github.maxopoly.finale.classes.archer.arrows.item;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.archer.Arrow;
import com.github.maxopoly.finale.classes.archer.arrows.SiegeArrow;

public class SiegeDisplayItem extends ArrowDisplayItem {
	
	public SiegeDisplayItem(int id) {
		super(id);
	}

	public static final int ID = 17;

	@Override
	public Arrow getArrow() {
		return (SiegeArrow) Finale.getPlugin().getAbilityManager().getAbility(SiegeArrow.NAME);
	}
	
}
