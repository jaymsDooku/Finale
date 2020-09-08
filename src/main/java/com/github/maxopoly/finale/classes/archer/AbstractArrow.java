package com.github.maxopoly.finale.classes.archer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.github.maxopoly.finale.classes.archer.event.ArrowHitEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.ClassType;
import com.github.maxopoly.finale.classes.ability.AbilityPlayer;
import com.github.maxopoly.finale.classes.ability.AbstractAbility;
import com.github.maxopoly.finale.classes.archer.arrows.explosive.ExplosiveArrowData;

import io.jayms.serenno.kit.ItemStackBuilder;

public abstract class AbstractArrow extends AbstractAbility implements Arrow, Listener {

	// most recent responsible shooters
	private Map<UUID, ArcherPlayer> shooters = new HashMap<>();
	private Map<AbilityPlayer, ArrowData> arrows = new HashMap<>();
	private Map<ArcherPlayer, BukkitRunnable> trailEffects = new HashMap<>();
	
	protected ArcherConfig config = Finale.getPlugin().getManager().getArcherConfig();
	
	public ArcherConfig getConfig() {
		return config;
	}
	
	private ItemStackBuilder displayItem;
	
	protected AbstractArrow() {
		super();
	}
	
	@Override
	public ItemStackBuilder getDisplayItemBuilder() {
		if (displayItem == null) {
			displayItem = newDisplayItemBuilder();
		}
		return displayItem;
	}
	
	public abstract ItemStackBuilder newDisplayItemBuilder();
	
	@Override
	public boolean enable(AbilityPlayer user, boolean instantCD) {
		return super.enable(user, instantCD);
	}
	
	@Override
	public boolean enable(ArcherPlayer player, EntityShootBowEvent e) {
		if (!enable(player, true)) {
			return false;
		}
		arrows.put(player, instantiateArrowData(player, e));
		player.consumeEnergy(getBaseEnergyConsumption());
		player.getBukkitPlayer().sendMessage("You have fired " + getDisplayName());
		
		BukkitRunnable trailEffect = showTrailEffect((org.bukkit.entity.Arrow) e.getProjectile());
		if (trailEffect != null) {
			trailEffects.put(player, trailEffect);
			trailEffect.runTaskTimer(Finale.getPlugin(), 0L, 1L);
		}
		
		return true;
	}
	
	public BukkitRunnable showTrailEffect(org.bukkit.entity.Arrow arrow) {
		return null;
	}
	
	public ArrowData instantiateArrowData(ArcherPlayer player, EntityShootBowEvent e) {
		return new ExplosiveArrowData(player, e);
	}
	
	@Override
	public void disable(AbilityPlayer player) {
		super.disable(player);
		arrows.remove(player);
		
		if (trailEffects.containsKey(player)) {
			BukkitRunnable trailEffect = trailEffects.remove(player);
			trailEffect.cancel();
		}
	}
	
	@Override
	public org.bukkit.entity.Arrow getArrow(ArcherPlayer player) {
		ArrowData launchData = arrows.get(player);
		if (launchData == null) {
			return null;
		}
		return launchData.getArrow();
	}
	
	@Override
	public ArrowData getArrowData(ArcherPlayer player) {
		return arrows.get(player);
	}
	
	@EventHandler
	public void onArrowHit(ProjectileHitEvent e) {
		Projectile proj = e.getEntity();
		if (!(proj instanceof org.bukkit.entity.Arrow)) {
			return;
		}
		org.bukkit.entity.Arrow arrow = (org.bukkit.entity.Arrow) proj;
		ProjectileSource shooter = arrow.getShooter();
		if (!(shooter instanceof Player)) {
			return;
		}
		Player p = (Player) shooter;
		ArcherPlayer player = ((ArcherController)ClassType.ARCHER.getController()).getArcher(p);
		if (!isUsing(player)) {
			return;
		}
		
		if (trailEffects.containsKey(player)) {
			BukkitRunnable trailEffect = trailEffects.remove(player);
			trailEffect.cancel();
		}
		
		arrow.remove();
		
		Entity hitEntity = e.getHitEntity();
		if (hitEntity != null) {
			if (hitEntity instanceof Player) {
				Player hitPlayer = (Player) hitEntity;
				ArcherPlayer curShooter = getShooter(hitPlayer);
				if (player.isLinked(hitPlayer) && (curShooter != null && isUsing(curShooter) && !curShooter.isLinked(hitPlayer))) {
					disable(player);
					return;
				}
			}
			shooters.put(hitEntity.getUniqueId(), player);
		}
		ArrowHitEvent arrowHitEvent = new ArrowHitEvent(player, e, this);
		Bukkit.getPluginManager().callEvent(arrowHitEvent);
		handleHit(e);
	}
	
	@Override
	public ArcherPlayer getShooter(Entity entity) {
		return shooters.get(entity.getUniqueId());
	}
	
}

