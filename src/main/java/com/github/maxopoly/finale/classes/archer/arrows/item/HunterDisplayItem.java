package com.github.maxopoly.finale.classes.archer.arrows.item;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.archer.Arrow;
import com.github.maxopoly.finale.classes.archer.arrows.hunter.HunterArrow;

public class HunterDisplayItem extends ArrowDisplayItem {
	
	public HunterDisplayItem(int id) {
		super(id);
	}

	public static final int ID = 11;

	@Override
	public Arrow getArrow() {
		return (HunterArrow) Finale.getPlugin().getAbilityManager().getAbility(HunterArrow.NAME);
	}
	
}
