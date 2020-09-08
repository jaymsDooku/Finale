package com.github.maxopoly.finale.classes;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.github.maxopoly.finale.Finale;
import com.google.common.collect.Sets;

import io.jayms.serenno.armourtype.ArmorEquipEvent;
import io.jayms.serenno.ui.UIManager;
import io.jayms.serenno.ui.UIScoreboard;
import net.md_5.bungee.api.ChatColor;

public abstract class ClassController implements Listener {

	private Set<Player> using = Sets.newConcurrentHashSet();
	private ClassType classType;
	
	protected ClassController() {
		Bukkit.getPluginManager().registerEvents(this, Finale.getPlugin());
	}
	
	public ClassType getClassType() {
		return classType;
	}
	
	public void setClassType(ClassType classType) {
		this.classType = classType;
	}
	
	public ItemStack[] activate(Player player, ArmorEquipEvent e) {
		using.add(player);
		player.sendMessage(ChatColor.YELLOW + "You have " + ChatColor.GREEN + "activated " + classType.getDisplayName());
		return onActivate(player, e);
	}
	
	public boolean isActive(Player player) {
		return using.contains(player);
	}
	
	public ItemStack[] deactivate(Player player, ArmorEquipEvent e) {
		ItemStack[] armour = onDeactivate(player, e);
		using.remove(player);
		disposeUI(player, UIManager.getUIManager().getScoreboard(player).getScoreboard());
		player.sendMessage(ChatColor.YELLOW + "You have de-activated " + classType.getDisplayName());
		return armour;
	}
	
	public abstract ItemStack[] onActivate(Player player, ArmorEquipEvent e);
	
	public abstract ItemStack[] onDeactivate(Player player, ArmorEquipEvent e);
	
	public abstract void handleUI(Player player, UIScoreboard scoreboard);
	
	public abstract void disposeUI(Player player, UIScoreboard scoreboard);
	
	public abstract void handleActionBar(Player player, StringBuilder sb);
	
	public static class Activation {
		
		private ItemStack newItem;
		private ItemStack[] armour;
		
		public Activation(ItemStack newItem, ItemStack[] armour) {
			this.newItem = newItem;
			this.armour = armour;
		}
		
		public ItemStack getNewItem() {
			return newItem;
		}
		
		public ItemStack[] getArmour() {
			return armour;
		}
	}
	
}
