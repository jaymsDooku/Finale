package com.github.maxopoly.finale.classes.archer.arrows.item;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.archer.ArcherPlayer;
import com.github.maxopoly.finale.classes.archer.Arrow;
import com.github.maxopoly.finale.classes.archer.ArrowTools;

import io.jayms.serenno.item.CustomItem;
import io.jayms.serenno.kit.ItemStackBuilder;
import net.md_5.bungee.api.ChatColor;

public abstract class ArrowDisplayItem extends CustomItem {
	
	protected ArrowDisplayItem(int id) {
		super(Finale.getPlugin(), id);
	}

	public abstract Arrow getArrow();
	
	@Override
	protected ItemStackBuilder getItemStackBuilder(Map<String, Object> data) {
		return getArrow().getDisplayItemBuilder();
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
		return selectArrow(e.getPlayer());
	}

	@Override
	public Runnable getRightClick(PlayerInteractEvent e) {
		return selectArrow(e.getPlayer());
	}
	
	@Override
	public Runnable onSwitchSlot(PlayerItemHeldEvent e) {
		return selectArrow(e.getPlayer());
	}
	
	private Runnable selectArrow(Player who) {
		return () -> {
			ArcherPlayer archer = ArrowTools.getArcherPlayer(who);
			if (archer == null) {
				who.sendMessage(ChatColor.RED + "Only archers are allowed to select arrows.");
				return;
			}
			
			if (!archer.isChoosingArrow()) {
				return;
			}
			
			archer.selectArrow(getArrow());
		};
	}
}
