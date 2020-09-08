package com.github.maxopoly.finale.classes.archer.arrows;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
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
import io.jayms.serenno.util.CustomEntityFirework;
import io.jayms.serenno.util.ParticleEffect;
import net.md_5.bungee.api.ChatColor;

public class FlareArrow extends AbstractArrow {
	
	public static final String NAME = "flare-arrow";
	
	public FlareArrow() {
		super();
	}
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDisplayName() {
		return ChatColor.GOLD + "Flare " + ChatColor.WHITE + "Arrow";
	}

	@Override
	public ItemStackBuilder newDisplayItemBuilder() {
		return new ItemStackBuilder(Material.FIREWORK, 1)
				.meta(new ItemMetaBuilder()
						.name(getDisplayName())
                        .lore(Arrays.asList(ChatColor.GOLD + "Communicate with your allies using this flare.",
                        		ChatColor.WHITE + "Range: " + ChatColor.GOLD + config.getFlareRange(),
                        		ChatColor.WHITE + "Cooldown: " + ChatColor.GOLD + TimeUnit.MILLISECONDS.toSeconds(config.getFlareCooldown()) + "s",
                        		ChatColor.WHITE + "Cost: " + ChatColor.GOLD + ArrowTools.formatConsumption(getBaseEnergyConsumption()))));
	}
	
	@Override
	public EnergyConsumption getBaseEnergyConsumption() {
		return new EnergyConsumption(config.getFlareConsumption(), config.getFlareEnergy());
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
				ParticleEffect.FIREWORKS_SPARK.display(arrow.getLocation(), 0, 0, 0, 0, 1);
			}
			
		};
	}

	@Override
	public boolean update(AbilityPlayer user) {
		ArcherPlayer player = (ArcherPlayer) user;
		org.bukkit.entity.Arrow arrow = getArrow(player);
		double distance = ArrowTools.distance(player, arrow);
		if (distance > config.getFlareRange()) {
			flare(player, arrow);
			return false;
		}
		return true;
	}
	
	private void flare(ArcherPlayer player, org.bukkit.entity.Arrow arrow) {
		FireworkEffect.Builder builder = FireworkEffect.builder();
		FireworkEffect effect = builder.flicker(false).trail(false).with(FireworkEffect.Type.BALL).withColor(Color.RED).withFade(Color.BLUE).build();
		CustomEntityFirework.spawn(arrow.getLocation(), effect);
		arrow.remove();
		disable(player);
	}

	@Override
	public void handleHit(ProjectileHitEvent e) {
		org.bukkit.entity.Arrow arrow = (org.bukkit.entity.Arrow) e.getEntity();
		ArcherPlayer player = ArrowTools.getArcherPlayer(arrow);
		flare(player, arrow);
	}

	@Override
	public long getBaseCooldown() {
		return config.getFlareCooldown();
	}

}
