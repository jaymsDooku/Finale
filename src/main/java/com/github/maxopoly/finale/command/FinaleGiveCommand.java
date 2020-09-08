package com.github.maxopoly.finale.command;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;

import com.github.maxopoly.finale.classes.ability.item.LinkerItem;
import com.github.maxopoly.finale.classes.ability.item.SmokeBombItem;
import com.github.maxopoly.finale.classes.engineer.item.WrenchItem;

import io.jayms.serenno.item.CustomItemManager;
import io.jayms.serenno.util.ItemUtil;
import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.command.CivCommand;
import vg.civcraft.mc.civmodcore.command.StandaloneCommand;

@CivCommand(id = "finalegive")
public class FinaleGiveCommand extends StandaloneCommand {

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		if (args.length != 2) {
			player.sendMessage(ChatColor.RED + "Not enough arguments.");
			return true;
		}
		
		String itemName = args[0];
		int itemAmount = NumberConversions.toInt(args[1]);
		
		if (itemAmount <= 0) {
			player.sendMessage(ChatColor.RED + "Item amount must be greater than 0.");
			return true;
		}
		
		ItemStack it = null;
		switch (itemName) {
			case "wrench":
				it = CustomItemManager.getCustomItemManager().getCustomItem(WrenchItem.ID, WrenchItem.class).getItemStack();
				break;
			case "smoke-bomb":
				it = CustomItemManager.getCustomItemManager().getCustomItem(SmokeBombItem.ID, SmokeBombItem.class).getItemStack();
				break;
			case "linker":
				it = CustomItemManager.getCustomItemManager().getCustomItem(LinkerItem.ID, LinkerItem.class).getItemStack();
				break;
			default:
				break;
		}
		
		if (it == null) {
			player.sendMessage(ChatColor.RED + "That item doesn't exist.");
			return true;
		}
		
		it.setAmount(itemAmount);
		player.getInventory().addItem(it);
		player.sendMessage(ChatColor.YELLOW + "You have been given " + ChatColor.WHITE + itemAmount + " " + ItemUtil.getName(it));
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return new LinkedList<>();
	}

}

