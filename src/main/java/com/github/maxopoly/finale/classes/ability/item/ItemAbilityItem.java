package com.github.maxopoly.finale.classes.ability.item;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.ability.AbilityManager;
import com.github.maxopoly.finale.classes.ability.AbilityPlayer;

import io.jayms.serenno.item.CustomItem;
import io.jayms.serenno.kit.ItemMetaBuilder;
import io.jayms.serenno.kit.ItemStackBuilder;
import net.md_5.bungee.api.ChatColor;

public abstract class ItemAbilityItem extends CustomItem {
	
	public ItemAbilityItem(int id) {
		super(Finale.getPlugin(), id);
	}
	
	public abstract ItemAbility getItemAbility();
	
	@Override
	protected ItemStackBuilder getItemStackBuilder(Map<String, Object> data) {
		return new ItemStackBuilder(getItemAbility().getRequiredMaterial(), getItemAbility().getRequiredAmount())
				.meta(new ItemMetaBuilder()
						.name(getItemAbility().getDisplayName())
						.flag(ItemFlag.HIDE_ATTRIBUTES));
	}
	
	@Override
	public boolean preventOnLeftClick() {
		return false;
	}
	
	@Override
	public boolean preventOnRightClick() {
		return true;
	}

	@Override
	public Runnable getRightClick(PlayerInteractEvent e) {
		return () -> {
			Player player = e.getPlayer();
			ItemAbility itemAbility = getItemAbility();
			
			AbilityManager am = Finale.getPlugin().getAbilityManager();
			AbilityPlayer abPlayer = am.getAbilityPlayer(player);
			if (!abPlayer.hasClassPlayer(itemAbility.getValidClasses())) {
				player.sendMessage(ChatColor.RED + "You aren't using a class that can use this item.");
				return;
			}
			
			itemAbility.enable(abPlayer, true);
		};
	}

}
