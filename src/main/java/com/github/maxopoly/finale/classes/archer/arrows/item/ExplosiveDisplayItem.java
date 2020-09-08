package com.github.maxopoly.finale.classes.archer.arrows.item;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.archer.Arrow;
import com.github.maxopoly.finale.classes.archer.arrows.explosive.ExplosiveArrow;

public class ExplosiveDisplayItem extends ArrowDisplayItem {

	public ExplosiveDisplayItem(int id) {
		super(id);
	}

	public static final int ID = 16;
	
	@Override
	public Arrow getArrow() {
		return (ExplosiveArrow) Finale.getPlugin().getAbilityManager().getAbility(ExplosiveArrow.NAME);
	}
	
}