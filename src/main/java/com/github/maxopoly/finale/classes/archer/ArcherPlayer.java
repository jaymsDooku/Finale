package com.github.maxopoly.finale.classes.archer;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.ConsumptionType;
import com.github.maxopoly.finale.classes.ability.AbilityPlayer;
import com.github.maxopoly.finale.classes.archer.event.ArcherLinkPlayerEvent;
import com.github.maxopoly.finale.classes.archer.event.ArcherLinkPlayerEvent.LinkType;

import io.jayms.serenno.kit.Kit;

public class ArcherPlayer extends AbilityPlayer {

	private AbilityPlayer parentPlayer;
	private Arrow selectedArrow;
	private ArrowImpactForm impact = ArrowImpactForm.CONCENTRATED;
	
	private Set<Player> linked = new HashSet<>();
	private double energy;
	private double energyRegen = 0.65;
	private double maxEnergy;
	
	private Kit arrowChoosingKit;
	private boolean choosingArrow;
	
	private boolean notifyDistance = true;
	
	public ArcherPlayer(AbilityPlayer player, Kit arrowChoosingKit, double maxEnergy) {
		super(player.getBukkitPlayer());
		this.parentPlayer = player;
		this.energy = maxEnergy;
		this.maxEnergy = maxEnergy;
		this.arrowChoosingKit = arrowChoosingKit;
	}
	
	public double getLinkedReduction() {
		return Finale.getPlugin().getManager().getArcherConfig().getLinkedReduction() * numberOfLinked();
	}
	
	public double getEffectiveLinkedReduction() {
		return (1.0 - getLinkedReduction());
	}
	
	public int numberOfLinked() {
		return linked.size();
	}
	
	public void setSelectedArrow(Arrow selectedArrow) {
		this.selectedArrow = selectedArrow;
	}
	
	public Arrow getSelectedArrow() {
		return selectedArrow;
	}
	
	public Kit getArrowChoosingKit() {
		return arrowChoosingKit;
	}
	
	public void setNotifyDistance(boolean notifyDistance) {
		this.notifyDistance = notifyDistance;
	}
	
	public boolean isNotifyDistance() {
		return notifyDistance;
	}
	
	public void notify(String message) {
		ArrowTools.archerMessage(getBukkitPlayer(), message);
	}
	
	public AbilityPlayer getParentPlayer() {
		return parentPlayer;
	}
	
	private Kit chooseTemp; 
	private ItemStack bow;
	
	public void chooseArrow(ItemStack bow) {
		arrowChoosingKit.helmet(bukkitPlayer.getInventory().getHelmet());
		arrowChoosingKit.chestplate(bukkitPlayer.getInventory().getChestplate());
		arrowChoosingKit.leggings(bukkitPlayer.getInventory().getLeggings());
		arrowChoosingKit.boots(bukkitPlayer.getInventory().getBoots());
		
		chooseTemp = new Kit(bukkitPlayer);
		this.bow = bow;
		
		new BukkitRunnable() {

			@Override
			public void run() {
				for (int i = 0; i < 9; i++) {
					ItemStack content = arrowChoosingKit.contents()[i];
					if (content != null && content.getType() == Material.BOW) {
						arrowChoosingKit.setHeldSlot(i);
						break;
					}
				}
				arrowChoosingKit.load(bukkitPlayer);
				bukkitPlayer.getInventory().addItem(bow);
			}
			
		}.runTask(Finale.getPlugin());
		
		new BukkitRunnable() {

			@Override
			public void run() {
				choosingArrow = true;
			}
			
		}.runTask(Finale.getPlugin());
	}
	
	public void selectArrow(Arrow arrow) {
		setSelectedArrow(arrow);
		
		if (arrow != null) {
			bukkitPlayer.sendMessage("You have selected " + arrow.getDisplayName());
		}
		
		stopChoosingArrow();
	}
	
	public void stopChoosingArrow() {
		new BukkitRunnable() {
		
			@Override
			public void run() {
				choosingArrow = false;
				if (chooseTemp != null) {
					int arrowChoosingSlot = bukkitPlayer.getInventory().first(bow);
					bukkitPlayer.getInventory().remove(bow);
					
					arrowChoosingKit = new Kit(bukkitPlayer);
					arrowChoosingKit.setHeldSlot(arrowChoosingSlot);
					
					chooseTemp.load(bukkitPlayer);
					chooseTemp = null;
					bow = null;
				}	
			}
			
		}.runTask(Finale.getPlugin());
	}
	
	public void fireArrow(EntityShootBowEvent e) {
		Arrow curArrow = getSelectedArrow();
		if (curArrow == null) {
			return;
		}
		curArrow.enable(this, e);
	}
	
	public void consumeEnergy(EnergyConsumption consumption) {
		double reduce = 0;
		double amount = consumption.getAmount();
		ConsumptionType type = consumption.getType();
		switch (type) {
			case FLAT:
				reduce = amount;
				break;
			case PERCENTAGE:
				reduce = maxEnergy * (amount/100);
				break;
			default:
				break;
		}
		energy -= reduce;
		if (energy < 0) {
			energy = 0;
		}
	}
	
	public boolean hasEnoughEnergy(EnergyConsumption consumption) {
		ConsumptionType type = consumption.getType();
		switch (type) {
			case FLAT:
				return energy > consumption.getAmount();
			case PERCENTAGE:
				double perc = (energy/maxEnergy);
				double consumpPerc = (consumption.getAmount()/100);
				return perc > consumpPerc;
			default:
				return false;
		} 
	}
	
	public Player getBukkitPlayer() {
		return bukkitPlayer;
	}
	
	public void setImpact(ArrowImpactForm impact) {
		this.impact = impact;
	}
	
	public ArrowImpactForm getImpact() {
		return impact;
	}
	
	public double getEnergy() {
		return energy;
	}
	
	public double getEnergyRegen() {
		return energyRegen;
	}
	
	public void regenerateEnergy() {
		if (energy >= maxEnergy) {
			return;
		}
		
		energy += getEnergyRegen();
		if (energy >= maxEnergy) {
			energy = maxEnergy;
		}
	}
	
	public double getMaxEnergy() {
		return maxEnergy;
	}
	
	public double getPercentageEnergy() {
		return (energy / maxEnergy) * 100;
	}
	
	public void setEnergyRegen(double energyRegen) {
		this.energyRegen = energyRegen;
	}
	
	public void link(Player player) {
		linked.add(player);
		
		Bukkit.getPluginManager().callEvent(new ArcherLinkPlayerEvent(this, player, LinkType.LINK));
	}
	
	public void unlink(Player player) {
		linked.remove(player);
		
		Bukkit.getPluginManager().callEvent(new ArcherLinkPlayerEvent(this, player, LinkType.UNLINK));
	}
	
	public boolean isLinked(Player player) {
		return linked.contains(player);
	}
	
	public Set<Player> getLinked() {
		return linked;
	}
	
	public boolean isChoosingArrow() {
		return choosingArrow;
	}
	
}
