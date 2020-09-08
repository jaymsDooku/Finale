package com.github.maxopoly.finale.classes.ability.item;

import java.util.Set;

import io.jayms.serenno.kit.ItemMetaBuilder;
import io.jayms.serenno.kit.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.github.maxopoly.finale.classes.ClassType;
import com.github.maxopoly.finale.classes.ability.AbilityPlayer;
import com.github.maxopoly.finale.classes.ability.AbstractAbility;
import com.github.maxopoly.finale.classes.ability.ClickType;

public abstract class ItemAbility extends AbstractAbility {
	
	public boolean enable(AbilityPlayer user, boolean instantCD, boolean consume) {
		if (!super.enable(user, instantCD)) {
			return false;
		}
		
		if (consume) {
			ItemStack consumeIt = new ItemStack(user.getBukkitPlayer().getInventory().getItemInMainHand());
			consumeIt.setAmount(getRequiredAmount());
			user.getBukkitPlayer().getInventory().remove(consumeIt);
		}
		user.getBukkitPlayer().sendMessage("You've activated " + getDisplayName());
		return true;
	}
	
	@Override
	public boolean enable(AbilityPlayer user, boolean instantCD) {
		return enable(user, instantCD, consumeOnUse());
	}
	
	public ItemStack getRequiredItemStack() {
		return new ItemStackBuilder(getRequiredMaterial(), getRequiredAmount())
				.meta(new ItemMetaBuilder()
						.name(getDisplayName())
						.flag(ItemFlag.HIDE_ATTRIBUTES))
				.build();
	}
	
	public abstract boolean consumeOnUse();
	
	public abstract ClickType getActivateClickType();

	public abstract Material getRequiredMaterial();

	public abstract int getRequiredAmount();
	
	public abstract Set<ClassType> getValidClasses();
	
}
