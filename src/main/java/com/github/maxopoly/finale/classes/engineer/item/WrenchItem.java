package com.github.maxopoly.finale.classes.engineer.item;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.engineer.EngineerPlayer;
import com.github.maxopoly.finale.classes.engineer.EngineerTools;
import com.github.maxopoly.finale.classes.engineer.event.WrenchUseEvent;

import io.jayms.serenno.item.CustomItem;
import io.jayms.serenno.kit.ItemMetaBuilder;
import io.jayms.serenno.kit.ItemStackBuilder;
import net.md_5.bungee.api.ChatColor;

public class WrenchItem extends CustomItem {
	
	public static final int ID = 100;
	
	public WrenchItem(int id) {
		super(Finale.getPlugin(), id);
	}
	
	@Override
	protected ItemStackBuilder getItemStackBuilder(Map<String, Object> data) {
		return new ItemStackBuilder(Material.STICK, 1)
				.meta(new ItemMetaBuilder().name(ChatColor.GOLD + "Wrench")
						.enchant(Enchantment.DURABILITY, 1, false)
						.flag(ItemFlag.HIDE_ENCHANTS));
	}
	
	@Override
	public boolean preventOnLeftClick() {
		return true;
	}
	
	@Override
	public boolean preventOnRightClick() {
		return true;
	}

	@Override
	public Runnable getLeftClick(PlayerInteractEvent e) {
		return getRunnable(e);
	}
	
	@Override
	public Runnable getRightClick(PlayerInteractEvent e) {
		return getRunnable(e);
	}
	
	private Runnable getRunnable(PlayerInteractEvent e) {
		return () -> {
			Player who = e.getPlayer();
			EngineerPlayer engineer = EngineerTools.getEngineer(who);
			
			if (engineer == null) {
				who.sendMessage(ChatColor.RED + "Only an Engineer knows how to use this tool.");
				return;
			}
			
			Bukkit.getPluginManager().callEvent(new WrenchUseEvent(engineer, e));
		};
	}
}

