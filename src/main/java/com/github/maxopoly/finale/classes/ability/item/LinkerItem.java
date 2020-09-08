package com.github.maxopoly.finale.classes.ability.item;

import com.github.maxopoly.finale.Finale;

public class LinkerItem extends ItemAbilityItem {

	public static final int ID = 102;
	
	public LinkerItem(int id) {
		super(id);
	}
	
	@Override
	public ItemAbility getItemAbility() {
		return (ItemAbility) Finale.getPlugin().getAbilityManager().getAbility(LinkAbility.NAME);
	}

}

