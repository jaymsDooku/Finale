package com.github.maxopoly.finale.classes.archer.arrows.item;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.archer.Arrow;
import com.github.maxopoly.finale.classes.archer.arrows.FlareArrow;

public class FlareDisplayItem extends ArrowDisplayItem {

	public FlareDisplayItem(int id) {
		super(id);
	}

	public static final int ID = 10;
	
	@Override
	public Arrow getArrow() {
		return (FlareArrow) Finale.getPlugin().getAbilityManager().getAbility(FlareArrow.NAME);
	}
	
}
