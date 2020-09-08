package com.github.maxopoly.finale.classes.archer.arrows.item;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.archer.Arrow;
import com.github.maxopoly.finale.classes.archer.arrows.PoisonArrow;

public class PoisonDisplayItem extends ArrowDisplayItem {
	
	public PoisonDisplayItem(int id) {
		super(id);
	}

	public static final int ID = 12;

	@Override
	public Arrow getArrow() {
		return (PoisonArrow) Finale.getPlugin().getAbilityManager().getAbility(PoisonArrow.NAME);
	}
	
}
