package com.github.maxopoly.finale.classes.ability;

import java.text.DecimalFormat;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.github.maxopoly.finale.Finale;
import com.google.common.collect.Sets;

import io.jayms.serenno.util.Cooldown;
import net.md_5.bungee.api.ChatColor;

public abstract class AbstractAbility implements Ability, Listener {

	private Set<AbilityPlayer> using = Sets.newConcurrentHashSet();
	private Cooldown<UUID> cooldowns = new Cooldown<>();
	
	protected final DecimalFormat df = new DecimalFormat("#.#");
	
	protected AbstractAbility() {
		Bukkit.getPluginManager().registerEvents(this, Finale.getPlugin());
	}
	
	@Override
	public boolean enable(AbilityPlayer user, boolean instantCD) {
		if (cooldowns.isOnCooldown(user.getUniqueId())) {
			user.getBukkitPlayer().sendMessage(getDisplayName() +
					ChatColor.RED + " is on cooldown for " +
					ChatColor.BOLD + df.format(cooldowns.getReadableTimeLeft(user.getUniqueId())) +
					ChatColor.RESET + ChatColor.RED + " more seconds.");
			return false;
		}
		if (instantCD)
			putOnCooldown(user);
		using.add(user);
		return true;
	}
	
	@Override
	public void disable(AbilityPlayer user) {
		using.remove(user);
	}
	
	@Override
	public void putOnCooldown(AbilityPlayer user) {
		cooldowns.putOnCooldown(user.getUniqueId(), getBaseCooldown());
	}
	
	@Override
	public void addCooldown(AbilityPlayer user, long cooldown) {
		cooldowns.putOnCooldown(user.getUniqueId(), cooldowns.getTimeLeft(user.getUniqueId()) + cooldown);
	}
	
	@Override
	public boolean isUsing(AbilityPlayer user) {
		return using.contains(user);
	}
	
	@Override
	public Set<AbilityPlayer> getUsing() {
		return using;
	}
	
	@Override
	public Cooldown<UUID> getCooldowns() {
		return cooldowns;
	}
	
	@Override
	public long getAdditionalCooldown() {
		return 0;
	}
	
	
}
