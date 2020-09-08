package com.github.maxopoly.finale.classes.ability.item;

import com.github.maxopoly.finale.Finale;

public class SmokeBombItem extends ItemAbilityItem {

	public SmokeBombItem(int id) {
		super(id);
	}

	public static final int ID = 101;
	
	@Override
	public ItemAbility getItemAbility() {
		return (ItemAbility) Finale.getPlugin().getAbilityManager().getAbility(SmokeBombAbility.NAME);
	}

}
