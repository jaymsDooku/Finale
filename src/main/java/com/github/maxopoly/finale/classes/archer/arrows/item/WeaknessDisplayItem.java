package com.github.maxopoly.finale.classes.archer.arrows.item;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.archer.Arrow;
import com.github.maxopoly.finale.classes.archer.arrows.WeaknessArrow;

public class WeaknessDisplayItem extends ArrowDisplayItem {
	
	public WeaknessDisplayItem(int id) {
		super(id);
	}

	public static final int ID = 14;

	@Override
	public Arrow getArrow() {
		return (WeaknessArrow) Finale.getPlugin().getAbilityManager().getAbility(WeaknessArrow.NAME);
	}
	
}
