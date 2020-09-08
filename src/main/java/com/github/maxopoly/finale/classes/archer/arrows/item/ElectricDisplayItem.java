package com.github.maxopoly.finale.classes.archer.arrows.item;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.archer.Arrow;
import com.github.maxopoly.finale.classes.archer.arrows.electric.ElectricArrow;

public class ElectricDisplayItem extends ArrowDisplayItem {
	
	public ElectricDisplayItem(int id) {
		super(id);
	}

	public static final int ID = 15;
	
	@Override
	public Arrow getArrow() {
		return (ElectricArrow) Finale.getPlugin().getAbilityManager().getAbility(ElectricArrow.NAME);
	}
	
}