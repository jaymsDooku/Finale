package com.github.maxopoly.finale.classes.ability.item;

import com.github.maxopoly.finale.Finale;

public class SugarRushItem extends ItemAbilityItem {
	
	public SugarRushItem(int id) {
		super(id);
	}

	public static final int ID = 201;

	@Override
	public ItemAbility getItemAbility() {
		return (ItemAbility) Finale.getPlugin().getAbilityManager().getAbility(SugarAbility.NAME);
	}

}
