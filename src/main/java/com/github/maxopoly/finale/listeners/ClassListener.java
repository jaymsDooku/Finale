package com.github.maxopoly.finale.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.ClassController;
import com.github.maxopoly.finale.classes.ClassType;

import io.jayms.serenno.armourtype.ArmorEquipEvent;
import io.jayms.serenno.ui.ActionBarHandler;
import io.jayms.serenno.ui.UI;
import io.jayms.serenno.ui.UIHandler;
import io.jayms.serenno.ui.UIManager;
import io.jayms.serenno.ui.UIScoreboard;

public class ClassListener implements Listener {
	
	public static ItemStack[] currentArmourContents(ArmorEquipEvent e, ItemStack[] armourContents) {
		if (e == null) {
			return armourContents;
		}
		switch (e.getType()) {
			case HELMET:
				armourContents[3] = e.getNewArmorPiece();
				break;
			case CHESTPLATE:
				armourContents[2] = e.getNewArmorPiece();
				break;
			case LEGGINGS:
				armourContents[1] = e.getNewArmorPiece();
				break;
			case BOOTS:
				armourContents[0] = e.getNewArmorPiece();
				break;
			default:
				break;
		}
		
		return armourContents;
	}
	
	public void clarifyClassStatus(Player player, ItemStack[] armourContents, List<ItemStack> contents, ArmorEquipEvent e) {
		for (ClassType type : ClassType.values()) {
			if (type.getRequirements().meetsRequirement(armourContents, contents)) {
				ClassController controller = type.getController();
				ItemStack[] armour = controller.activate(player, e);
				new BukkitRunnable() {
					
					public void run() {
						player.getInventory().setArmorContents(armour);
					};
					
				}.runTaskLater(Finale.getPlugin(), 1L);
			}
		}
	}
	
	public List<ItemStack> getContents(Player player) {
		List<ItemStack> contents = new ArrayList<>();
		contents.addAll(Arrays.asList(player.getInventory().getStorageContents()));
		contents.addAll(Arrays.asList(player.getInventory().getExtraContents()));
		return contents;
	}
	
	@EventHandler
	public void onArmourEquip(ArmorEquipEvent e) {
		Player player = e.getPlayer();
		
		ItemStack[] armourContents = currentArmourContents(e, player.getInventory().getArmorContents());
		List<ItemStack> contents = getContents(player);
		
		ClassController controller = ClassType.getActiveController(e.getPlayer());
		if (controller == null) {
			clarifyClassStatus(player, armourContents, contents, e);
			return;
		}
		
		if (!controller.getClassType().getRequirements().meetsRequirement(armourContents, contents)) {
			ItemStack[] armour = controller.deactivate(e.getPlayer(), e);
			new BukkitRunnable() {
				
				public void run() {
					player.getInventory().setArmorContents(armour);
				};
				
			}.runTaskLater(Finale.getPlugin(), 1L);
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		clarifyClassStatus(player, player.getInventory().getArmorContents(), getContents(player), null);
		
		UI ui = UIManager.getUIManager().getScoreboard(player);
		ui.getUIHandlers().add(new UIHandler() {
			
			@Override
			public void handle(Player player, UIScoreboard board) {
				ClassController controller = ClassType.getActiveController(player);
				if (controller != null) {
					board.add(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Class: " + controller.getClassType().getDisplayName(), 8);
					controller.handleUI(player, board);
				} else {
					board.remove(8, "");
				}
			}
		});
		ui.getActionBarHandlers().add(new ActionBarHandler() {
			
			@Override
			public StringBuilder handle(Player player, StringBuilder sb) {
				ClassController controller = ClassType.getActiveController(player);
				if (controller != null) {
					controller.handleActionBar(player, sb);
				}
				return sb;
			}
		});
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		
		ClassController controller = ClassType.getActiveController(player);
		if (controller == null) {
			return;
		}
		
		if (controller.isActive(player)) {
			ItemStack[] armour = controller.deactivate(e.getPlayer(), null);
			new BukkitRunnable() {
				
				public void run() {
					player.getInventory().setArmorContents(armour);
				};
				
			}.runTaskLater(Finale.getPlugin(), 1L);
		}
	}
	
}
