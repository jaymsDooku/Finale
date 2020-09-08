package com.github.maxopoly.finale.classes.ability.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.ClassType;
import com.github.maxopoly.finale.classes.ability.AbilityPlayer;
import com.github.maxopoly.finale.classes.ability.ClickType;
import com.github.maxopoly.finale.classes.archer.ArcherConfig;
import com.github.maxopoly.finale.classes.archer.ArcherPlayer;
import com.github.maxopoly.finale.classes.archer.EnergyConsumption;
import com.google.common.collect.Sets;

import io.jayms.serenno.util.PlayerTools;
import net.md_5.bungee.api.ChatColor;

public class SugarAbility extends ItemAbility {
	
	public static final String NAME = "sugar-speed";

	private ArcherConfig config = Finale.getPlugin().getManager().getArcherConfig();
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDisplayName() {
		return ChatColor.AQUA + "Sugar Rush";
	}
	
	private Map<UUID, PotionEffect> effectCache = new HashMap<>();
	
	@Override
	public boolean enable(AbilityPlayer user, boolean instantCD) {
		if (!super.enable(user, instantCD)) {
			return false;
		}
		ArcherPlayer archer = (ArcherPlayer) user.getClassPlayer(ClassType.ARCHER);
		PotionEffect activeEffect = PlayerTools.getActiveEffect(user.getBukkitPlayer(), PotionEffectType.SPEED);
		effectCache.put(user.getUniqueId(), activeEffect);
		
		user.getBukkitPlayer().getInventory().remove(new ItemStack(getRequiredMaterial(), getRequiredAmount()));
		user.getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * config.getSugarRushDuration(), 2), true);
		
		new BukkitRunnable() {
		
			@Override
			public void run() {
				user.getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2), true);
			}
			
		}.runTaskLater(Finale.getPlugin(), (20 * config.getSugarRushDuration()) + 1);
		
		disable(user);
		putOnCooldown(user);
		archer.consumeEnergy(getEnergyConsumption());
		return true;
	}
	
	private EnergyConsumption energy;
	
	private EnergyConsumption getEnergyConsumption() {
		if (energy == null) {
			energy = new EnergyConsumption(config.getSugarRushConsumption(), config.getSugarRushEnergy());
		}
		return energy;
	}

	@Override
	public boolean update(AbilityPlayer user) {
		return true;
	}

	@Override
	public long getBaseCooldown() {
		return config.getSugarRushCooldown();
	}
	
	@Override
	public boolean consumeOnUse() {
		return true;
	}

	@Override
	public Material getRequiredMaterial() {
		return Material.SUGAR;
	}

	@Override
	public int getRequiredAmount() {
		return 1;
	}

	@Override
	public Set<ClassType> getValidClasses() {
		return Sets.newHashSet(ClassType.ARCHER);
	}
	
	@Override
	public ClickType getActivateClickType() {
		return ClickType.RIGHT;
	}

}
