package com.github.maxopoly.finale.classes.archer.arrows.item;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.archer.Arrow;
import com.github.maxopoly.finale.classes.archer.arrows.SlownessArrow;

public class SlownessDisplayItem extends ArrowDisplayItem {
	
	public SlownessDisplayItem(int id) {
		super(id);
	}

	public static final int ID = 13;

	@Override
	public Arrow getArrow() {
		return (SlownessArrow) Finale.getPlugin().getAbilityManager().getAbility(SlownessArrow.NAME);
	}
	
}
