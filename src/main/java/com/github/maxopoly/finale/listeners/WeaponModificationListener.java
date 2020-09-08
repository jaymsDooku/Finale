package com.github.maxopoly.finale.listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.misc.ArmourModifier;
import com.github.maxopoly.finale.misc.WeaponModifier;

import io.jayms.serenno.util.ItemUtil;

public class WeaponModificationListener implements Listener {

	@EventHandler
	public void weaponMod(InventoryClickEvent e) {
		ItemStack is = e.getCurrentItem();
		if (is == null) {
			return;
		}
		WeaponModifier weaponMod = Finale.getPlugin().getManager().getWeaponModifer();
		
		double adjustedDamage = weaponMod.getDamage(is.getType());
		double adjustedAttackSpeed = weaponMod.getAttackSpeed(is.getType());
		if (adjustedAttackSpeed == -1.0 && adjustedDamage == -1) {
			return;
		}
		ItemStack result = ItemUtil.setWeapon(is, adjustedDamage, adjustedAttackSpeed);
		e.setCurrentItem(result);
	}
	
	@EventHandler
	public void armourMod(InventoryClickEvent e) {
		ItemStack is = e.getCurrentItem();
		if (is == null) {
			return;
		}
		ArmourModifier armourMod = Finale.getPlugin().getManager().getArmourModifier();
		
		int toughness = armourMod.getToughness(is.getType());
		int armour = armourMod.getArmour(is.getType());
		if (toughness == -1 && armour == -1) {
			return;
		}
		
		ItemStack result = ItemUtil.setArmour(is, toughness, armour);
		e.setCurrentItem(result);
	}

}
