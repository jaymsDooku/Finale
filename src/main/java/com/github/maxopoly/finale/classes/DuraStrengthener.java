package com.github.maxopoly.finale.classes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.maxopoly.finale.Finale;

public abstract class DuraStrengthener implements Listener {
	
	private ClassController classController;
	private Map<UUID, Map<Material, DuraCounter>> duraCounters = new ConcurrentHashMap<>();
	
	public DuraStrengthener(ClassController classController) {
		this.classController = classController;
		Bukkit.getPluginManager().registerEvents(this, Finale.getPlugin());
	}
	
	public abstract List<Material> getArmour();
	
	public abstract int getStrength();

	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent e) {
		Player player = e.getPlayer();
		if (!(classController.isActive(player))) {
			return;
		}
		
		if (!getArmour().contains(e.getItem().getType())) {
			return;
		}
		
		Map<Material, DuraCounter> dura = duraCounters.get(player.getUniqueId());
		if (dura == null) {
			dura = new HashMap<>();
			duraCounters.put(player.getUniqueId(), dura);
		}
		
		DuraCounter counter = dura.get(e.getItem().getType());
		if (counter == null) {
			counter = new DuraCounter(getStrength());
			dura.put(e.getItem().getType(), counter);
		}
		
		if (!counter.canBreak()) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemBreak(PlayerItemBreakEvent e) {
		Player player = e.getPlayer();
		if (!(classController.isActive(player))) {
			return;
		}
		
		if (!getArmour().contains(e.getBrokenItem().getType())) {
			return;
		}
		
		Map<Material, DuraCounter> dura = duraCounters.get(player.getUniqueId());
		if (dura == null) {
			return;
		}
		dura.remove(e.getBrokenItem().getType());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (duraCounters.containsKey(e.getPlayer().getUniqueId())) {
			duraCounters.remove(e.getPlayer().getUniqueId());
		}
	}
	
}
