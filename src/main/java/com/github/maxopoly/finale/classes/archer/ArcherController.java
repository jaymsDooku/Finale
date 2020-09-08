package com.github.maxopoly.finale.classes.archer;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.github.maxopoly.finale.classes.archer.arrows.hunter.HunterMarks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.ClassController;
import com.github.maxopoly.finale.classes.ClassType;
import com.github.maxopoly.finale.classes.ability.AbilityManager;
import com.github.maxopoly.finale.classes.ability.AbilityPlayer;
import com.github.maxopoly.finale.classes.archer.arrows.FlareArrow;
import com.github.maxopoly.finale.classes.archer.arrows.PoisonArrow;
import com.github.maxopoly.finale.classes.archer.arrows.SiegeArrow;
import com.github.maxopoly.finale.classes.archer.arrows.SlownessArrow;
import com.github.maxopoly.finale.classes.archer.arrows.WeaknessArrow;
import com.github.maxopoly.finale.classes.archer.arrows.electric.ElectricArrow;
import com.github.maxopoly.finale.classes.archer.arrows.explosive.ExplosiveArrow;
import com.github.maxopoly.finale.classes.archer.arrows.hunter.HunterArrow;
import com.github.maxopoly.finale.classes.archer.arrows.item.ElectricDisplayItem;
import com.github.maxopoly.finale.classes.archer.arrows.item.ExplosiveDisplayItem;
import com.github.maxopoly.finale.classes.archer.arrows.item.FlareDisplayItem;
import com.github.maxopoly.finale.classes.archer.arrows.item.HunterDisplayItem;
import com.github.maxopoly.finale.classes.archer.arrows.item.PoisonDisplayItem;
import com.github.maxopoly.finale.classes.archer.arrows.item.SiegeDisplayItem;
import com.github.maxopoly.finale.classes.archer.arrows.item.SlownessDisplayItem;
import com.github.maxopoly.finale.classes.archer.arrows.item.WeaknessDisplayItem;
import com.github.maxopoly.finale.listeners.ClassListener;

import io.jayms.serenno.armourtype.ArmorEquipEvent;
import io.jayms.serenno.item.CustomItemManager;
import io.jayms.serenno.kit.Kit;
import io.jayms.serenno.ui.UIScoreboard;
import io.jayms.serenno.util.ArmourCache;
import io.jayms.serenno.util.ArmourType;
import io.jayms.serenno.util.ItemUtil;
import io.jayms.serenno.util.PlayerTools;
import io.jayms.serenno.util.PotionEffectCache;
import net.md_5.bungee.api.ChatColor;

public class ArcherController extends ClassController { 
	
	private Map<UUID, PotionEffectCache> potEffectCaches = new ConcurrentHashMap<>();
	private Map<UUID, ArmourCache> armourCaches = new ConcurrentHashMap<>();

	private AbilityManager am;
	private HunterArrow hunterArrow;

	private ArcherConfig config;
	private ArcherEnergyRegenRunnable regenRunnable;
	
	private LeatherDuraStrengthener leatherStrengthener;
	
	public ArcherController() {
		config = Finale.getPlugin().getManager().getArcherConfig();
		am = Finale.getPlugin().getAbilityManager();
		am.registerAbility(new FlareArrow());
		am.registerAbility(hunterArrow = new HunterArrow());
		am.registerAbility(new PoisonArrow());
		am.registerAbility(new SlownessArrow());
		am.registerAbility(new WeaknessArrow());
		am.registerAbility(new ElectricArrow());
		am.registerAbility(new ExplosiveArrow());
		am.registerAbility(new SiegeArrow());
		
		choosingArrowKit = new Kit()
				.set(1, CustomItemManager.getCustomItemManager().getCustomItem(FlareDisplayItem.ID, FlareDisplayItem.class).getItemStack())
				.set(2, CustomItemManager.getCustomItemManager().getCustomItem(HunterDisplayItem.ID, HunterDisplayItem.class).getItemStack())
				.set(3, CustomItemManager.getCustomItemManager().getCustomItem(PoisonDisplayItem.ID, PoisonDisplayItem.class).getItemStack())
				.set(4, CustomItemManager.getCustomItemManager().getCustomItem(SlownessDisplayItem.ID, SlownessDisplayItem.class).getItemStack())
				.set(5, CustomItemManager.getCustomItemManager().getCustomItem(WeaknessDisplayItem.ID, WeaknessDisplayItem.class).getItemStack())
				.set(6, CustomItemManager.getCustomItemManager().getCustomItem(ElectricDisplayItem.ID, ElectricDisplayItem.class).getItemStack())
				.set(7, CustomItemManager.getCustomItemManager().getCustomItem(ExplosiveDisplayItem.ID, ExplosiveDisplayItem.class).getItemStack())
				.set(8, CustomItemManager.getCustomItemManager().getCustomItem(SiegeDisplayItem.ID, SiegeDisplayItem.class).getItemStack());
		
		leatherStrengthener = new LeatherDuraStrengthener(this);
	}
	
	private Kit choosingArrowKit;
	
	public ArcherPlayer getArcher(Player player) {
		AbilityPlayer abPlayer = am.getAbilityPlayer(player);
		ArcherPlayer archer = (ArcherPlayer) abPlayer.getClassPlayer(ClassType.ARCHER);
		if (archer == null) {
			if (regenRunnable == null) {
				regenRunnable = new ArcherEnergyRegenRunnable();
				Bukkit.getScheduler().runTaskTimerAsynchronously(Finale.getPlugin(), regenRunnable, 0L, 20L);
			}
			
			archer = new ArcherPlayer(abPlayer, choosingArrowKit, 100);
			abPlayer.putClassPlayer(ClassType.ARCHER, archer);
		}
		return archer;
	}
	
	@Override
	public ItemStack[] onActivate(Player player, ArmorEquipEvent e) {
		getArcher(player);
		potEffectCaches.put(player.getUniqueId(), new PotionEffectCache(player));
		PlayerTools.clearEffects(player);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
		
		ItemStack[] armorContents = ClassListener.currentArmourContents(e, player.getInventory().getArmorContents());
		ArmourCache armourCache = new ArmourCache(armorContents);
		armourCaches.put(player.getUniqueId(), armourCache);
		if (e == null) {
			return armorContents;
		}
		
		ItemStack newItem = e.getNewArmorPiece();
		
		ItemStack boots = armorContents[0];
		boots = ItemUtil.setArmour(boots, 2, 2);
		
		ItemStack legs = armorContents[1]; 
		legs = ItemUtil.setArmour(legs, 1, 6);
		
		ItemStack chest = armorContents[2];
		chest = ItemUtil.setArmour(chest, 1, 7);
		
		ItemStack helm = armorContents[3];
		helm = ItemUtil.setArmour(helm, 2, 1);
		
		newItem = setArmorContent(armorContents, 0, boots, newItem, e.getOldArmorPiece());
		newItem = setArmorContent(armorContents, 1, legs, newItem, e.getOldArmorPiece());
		newItem = setArmorContent(armorContents, 2, chest, newItem, e.getOldArmorPiece());
		newItem = setArmorContent(armorContents, 3, helm, newItem, e.getOldArmorPiece());
		return armorContents;
	}
	
	private DecimalFormat dp1 = new DecimalFormat("##.#");
	
	@Override
	public void handleUI(Player player, UIScoreboard scoreboard) {
		if (isActive(player)) {
			ArcherPlayer archer = getArcher(player);
			double percEnergy = archer.getPercentageEnergy();
			ChatColor percColor = ArrowTools.energyColour(percEnergy);
			scoreboard.add(ChatColor.YELLOW + "Energy: " + percColor + dp1.format(archer.getEnergy()) + " (" + dp1.format(percEnergy)  + "%)", 7);
			if (archer.numberOfLinked() > 0) {
				scoreboard.add(ChatColor.GREEN + "Linked: " + ChatColor.WHITE + archer.numberOfLinked() + " (" + ChatColor.RED + "-" + dp1.format(archer.getLinkedReduction() * 100) + "%" + ChatColor.WHITE + ")", 6);
			} else {
				scoreboard.remove(6, "");
			}
		} else {
			scoreboard.remove(7, "");
			scoreboard.remove(6, "");
		}
	}
	
	@Override
	public void disposeUI(Player player, UIScoreboard scoreboard) {
		scoreboard.remove(7, "");
		scoreboard.remove(6, "");
	}
	
	@Override
	public void handleActionBar(Player player, StringBuilder sb) {
		if (isActive(player)) {
			ArcherPlayer archer = getArcher(player);
			Arrow arrow = archer.getSelectedArrow();
			if (arrow == null) {
				return;
			}
			long cooldownLeft = arrow.getCooldowns().getTimeLeft(player.getUniqueId());
			if (cooldownLeft > 0) {
				double cooldownSecs = ((double)cooldownLeft) / 1000D;
				String cooldown = dp1.format(cooldownSecs) + "s";
				sb.append(" " + arrow.getDisplayName() + ": " + ChatColor.RED + cooldown + " ");
			}
		}
	}
	
	private ItemStack setArmorContent(ItemStack[] armorContents, int i, ItemStack replace, ItemStack newPiece, ItemStack oldPiece) {
		if (armorContents[i] == oldPiece) {
			newPiece = replace;
		}
		armorContents[i] = replace;
		return newPiece;
	}
	
	@Override
	public ItemStack[] onDeactivate(Player player, ArmorEquipEvent e) {
		if (!isActive(player)) {
			return armourCaches.remove(player.getUniqueId()).restore(player);
		}
		
		AbilityPlayer abPlayer = am.getAbilityPlayer(player);
		ArcherPlayer archer = (ArcherPlayer) abPlayer.removeClassPlayer(ClassType.ARCHER);
		
		if (archer != null && archer.isChoosingArrow()) {
			archer.stopChoosingArrow();
		}

		if (potEffectCaches.containsKey(player.getUniqueId())) {
			potEffectCaches.remove(player.getUniqueId()).restore(player);
		}
		ItemStack[] armour = armourCaches.remove(player.getUniqueId()).restore(player);
		if (e != null) {
			ArmourType armourType = ArmourType.getArmourType(e.getType());
			armour[armourType.getIndex()] = e.getNewArmorPiece();
		}
		return armour;
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (e.getHand() != EquipmentSlot.HAND) {
			return;
		}
		
		Player player = e.getPlayer();
		
		if (!isActive(player)) {
			return;
		}
		
		ArcherPlayer archer = getArcher(player);
		
		if (PlayerTools.isLeftClick(e.getAction())) {
			if (player.isSneaking()) {
				archer.setImpact(archer.getImpact() == ArrowImpactForm.CONCENTRATED ? ArrowImpactForm.EXPLOSIVE : ArrowImpactForm.CONCENTRATED);
				archer.sendMessage(ChatColor.YELLOW + "Set arrow impact form to " + ChatColor.GOLD + archer.getImpact());
				return;
			}
			
			if (e.getItem() == null || e.getItem().getType() != Material.BOW) {
				return;
			}
			
			if (archer.isChoosingArrow()) {
				archer.stopChoosingArrow();
				return;
			}
			
			archer.chooseArrow(e.getItem());
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		Player player = e.getPlayer();
		ArcherPlayer archer = ArrowTools.getArcherPlayer(player);
		if (archer == null) {
			return;
		}
		
		e.setCancelled(archer.isChoosingArrow());
	}
	
	@EventHandler
	public void onArrowFire(EntityShootBowEvent e) {
		Projectile proj = (Projectile) e.getProjectile();
		if (!(proj instanceof org.bukkit.entity.Arrow)) {
			return;
		}
		
		ArcherPlayer archer = ArrowTools.getArcherPlayer((org.bukkit.entity.Arrow) proj);
		if (archer == null) {
			return;
		}
		
		archer.fireArrow(e);
	}
	
	@EventHandler
	public void onProjectileCollide(ProjectileCollideEvent e) {
		if (!(e.getEntity() instanceof org.bukkit.entity.Arrow)) {
			return;
		}
		
		org.bukkit.entity.Arrow arrow = (org.bukkit.entity.Arrow) e.getEntity();
		
		ArcherPlayer archer = ArrowTools.getArcherPlayer(arrow);
		if (archer == null) {
			return;
		}
		
		if (!(e.getCollidedWith() instanceof Player)) {
			return;
		}
		
		Player collided = (Player) e.getCollidedWith();
		if (archer.isLinked(collided)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onArrowDamage(EntityDamageByEntityEvent e) {
		if (!config.isDistanceDamageEnabled()) {
			return;
		}
		
		if (!(e.getDamager() instanceof org.bukkit.entity.Arrow)) {
			return;
		}
		
		org.bukkit.entity.Arrow arrow = (org.bukkit.entity.Arrow) e.getDamager();
		
		ArcherPlayer archer = ArrowTools.getArcherPlayer(arrow);
		if (archer == null) {
			return;
		}
		
		Location arrowLoc = arrow.getLocation();
		
		double distance = arrowLoc.distance(archer.getBukkitPlayer().getLocation());
		double ogDamage = e.getDamage();
		double addDamage = distance * config.getAdditionalDamagePerBlock();
		double newDamage = ogDamage + addDamage;
		
		if (archer.isNotifyDistance()) {
			String message = ChatColor.WHITE + "Original: " + ChatColor.RED + dp1.format(ogDamage) + ChatColor.BLACK + " | "
					+ ChatColor.WHITE + "Distance(" + ChatColor.YELLOW + dp1.format(distance) + " Blocks" + ChatColor.WHITE + ") Damage: " + ChatColor.RED + dp1.format(addDamage) + ChatColor.BLACK + " | "
					+ ChatColor.WHITE + "New: " + ChatColor.RED + dp1.format(newDamage);
			archer.notify(message);
		}
		
		e.setDamage(newDamage);
	}
	
}
