package com.github.maxopoly.finale.classes.archer.arrows.hunter;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.maxopoly.finale.classes.ConsumptionType;
import com.github.maxopoly.finale.classes.ability.AbilityPlayer;
import com.github.maxopoly.finale.classes.archer.AbstractArrow;
import com.github.maxopoly.finale.classes.archer.ArcherPlayer;
import com.github.maxopoly.finale.classes.archer.ArrowData;
import com.github.maxopoly.finale.classes.archer.ArrowTools;
import com.github.maxopoly.finale.classes.archer.EnergyConsumption;

import io.jayms.serenno.kit.ItemMetaBuilder;
import io.jayms.serenno.kit.ItemStackBuilder;
import io.jayms.serenno.util.ParticleEffect;
import net.md_5.bungee.api.ChatColor;

public class HunterArrow extends AbstractArrow {

	public static final String NAME = "hunter-arrow";
	
	private HunterMarks hunterMarks = new HunterMarks(this);

	public HunterMarks getHunterMarks() {
		return hunterMarks;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDisplayName() {
		return ChatColor.RED + "Hunter " + ChatColor.WHITE + "Arrow";
	}

	@Override
	public ItemStackBuilder newDisplayItemBuilder() {
		return new ItemStackBuilder(Material.GOLD_SWORD, 1)
				.meta(new ItemMetaBuilder()
						.name(getDisplayName())
						.lore(Arrays.asList(ChatColor.RED + "Marked players take increased melee damage.",
								ChatColor.WHITE + "Damage: " + ChatColor.RED + getDamageDisplay(),
								ChatColor.WHITE + "Cooldown: " + ChatColor.RED + TimeUnit.MILLISECONDS.toSeconds(config.getHuntersMarkCooldown()) + "s",
								ChatColor.WHITE + "Cost: " + ChatColor.RED + ArrowTools.formatConsumption(getBaseEnergyConsumption()))));
	}
	
	private String dmgDisplay;
	
	private String getDamageDisplay() {
		if (dmgDisplay == null) {
			dmgDisplay = ((config.getHuntersMarkDamage() - 1.0D) * 100.0D) + "%";
		}
		return dmgDisplay;
	}

	@Override
	public EnergyConsumption getBaseEnergyConsumption() {
		return new EnergyConsumption(config.getHuntersMarkConsumption(), config.getHuntersMarkEnergy());
	}
	
	@Override
	public EnergyConsumption getAdditionalEnergyConsumption() {
		return new EnergyConsumption(ConsumptionType.FLAT, 0);
	}
	
	@Override
	public ArrowData instantiateArrowData(ArcherPlayer player, EntityShootBowEvent e) {
		return new ArrowData(player, e);
	}
	
	@Override
	public BukkitRunnable showTrailEffect(Arrow arrow) {
		return new BukkitRunnable() {
			
			@Override
			public void run() {
				ParticleEffect.FLAME.display(arrow.getLocation(), 0, 0, 0, 0, 1);
			}
			
		};
	}

	@Override
	public boolean update(AbilityPlayer player) {
		return true;
	}

	@Override
	public void handleHit(ProjectileHitEvent e) {
		Entity hitEntity = e.getHitEntity();
		if (hitEntity == null || !(hitEntity instanceof LivingEntity)) {
			return;
		}
		
		if (!(e.getEntity() instanceof Arrow)) {
			return;
		}
		Arrow arrow = (Arrow) e.getEntity();
		ArcherPlayer player = ArrowTools.getArcherPlayer(arrow);
		if (player == null) {
			return;
		}
		
		if (!isUsing(player)) {
			return;
		}
		
		hunterMarks.mark(player, (LivingEntity) hitEntity); 
		
		disable(player);
	}

	@Override
	public long getBaseCooldown() {
		return config.getHuntersMarkCooldown();
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onDamage(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		if (!(damager instanceof LivingEntity)) {
			return;
		}
		
		Entity victim = e.getEntity();
		if (!(victim instanceof LivingEntity)) {
			return;
		}
		
		if (hunterMarks.isMarked((LivingEntity) victim)) {
			double ogDamage = e.getDamage();
			double newDamage = ogDamage;
			newDamage *= config.getHuntersMarkDamage();
			e.setDamage(newDamage);
		}
	}
	
}
