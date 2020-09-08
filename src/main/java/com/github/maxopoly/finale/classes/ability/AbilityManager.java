package com.github.maxopoly.finale.classes.ability;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.ability.item.LinkAbility;
import com.github.maxopoly.finale.classes.ability.item.SmokeBombAbility;
import com.github.maxopoly.finale.classes.ability.item.SugarAbility;
import com.google.common.collect.Maps;

public class AbilityManager implements Listener {

	private Map<UUID, AbilityPlayer> abilityPlayers = Maps.newConcurrentMap();
	private Map<String, Ability> registeredAbilities = Maps.newConcurrentMap();
	
	private AbilityRunnable abilityRunnable;
	
	public AbilityManager() {
		registerAbility(new SugarAbility());
		registerAbility(new LinkAbility());
		registerAbility(new SmokeBombAbility());
		
		Bukkit.getPluginManager().registerEvents(this, Finale.getPlugin());
		abilityRunnable = new AbilityRunnable(this);
		Bukkit.getScheduler().runTaskTimer(Finale.getPlugin(), abilityRunnable, 0L, 1L);
	}
	
	public void registerAbility(Ability ability) {
		registeredAbilities.put(ability.getName(), ability);
	}
	
	public Ability getAbility(String name) {
		return registeredAbilities.get(name);
	}
	
	public Collection<Ability> getRegisteredAbilities() {
		return registeredAbilities.values();
	}
	
	public AbilityPlayer getAbilityPlayer(Player player) {
		return abilityPlayers.get(player.getUniqueId());
	}
	
	public Collection<AbilityPlayer> getAllAbilityPlayers() {
		return abilityPlayers.values();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		abilityPlayers.put(player.getUniqueId(), new AbilityPlayer(player));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		abilityPlayers.remove(player.getUniqueId());		
	}
	
}
