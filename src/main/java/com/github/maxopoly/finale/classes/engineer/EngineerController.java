package com.github.maxopoly.finale.classes.engineer;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.ClassController;
import com.github.maxopoly.finale.classes.ClassType;
import com.github.maxopoly.finale.classes.ability.AbilityManager;
import com.github.maxopoly.finale.classes.ability.AbilityPlayer;
import com.github.maxopoly.finale.classes.archer.arrows.FlareArrow;
import com.github.maxopoly.finale.classes.archer.arrows.PoisonArrow;
import com.github.maxopoly.finale.classes.archer.arrows.SlownessArrow;
import com.github.maxopoly.finale.classes.archer.arrows.WeaknessArrow;
import com.github.maxopoly.finale.classes.archer.arrows.electric.ElectricArrow;
import com.github.maxopoly.finale.classes.archer.arrows.explosive.ExplosiveArrow;
import com.github.maxopoly.finale.classes.archer.arrows.hunter.HunterArrow;
import com.github.maxopoly.finale.listeners.ClassListener;

import io.jayms.serenno.armourtype.ArmorEquipEvent;
import io.jayms.serenno.ui.UIScoreboard;
import io.jayms.serenno.util.ArmourCache;
import io.jayms.serenno.util.ArmourType;
import io.jayms.serenno.util.ItemUtil;
import io.jayms.serenno.util.PlayerTools;
import io.jayms.serenno.util.PotionEffectCache;

public class EngineerController extends ClassController { 
	
	private static final List<Material> ARCHER_ARMOUR = Arrays.asList(Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET);
	
	private Map<UUID, PotionEffectCache> potEffectCaches = new ConcurrentHashMap<>();
	private Map<UUID, ArmourCache> armourCaches = new ConcurrentHashMap<>();

	private AbilityManager am;
	
	private IronDuraStrengthener ironDuraStrengthener;
	
	public EngineerController() {
		am = Finale.getPlugin().getAbilityManager();
		am.registerAbility(new FlareArrow());
		am.registerAbility(new HunterArrow());
		am.registerAbility(new PoisonArrow());
		am.registerAbility(new SlownessArrow());
		am.registerAbility(new WeaknessArrow());
		am.registerAbility(new ElectricArrow());
		am.registerAbility(new ExplosiveArrow());
		
		ironDuraStrengthener = new IronDuraStrengthener(this);
	}
	
	
	@Override
	public ItemStack[] onActivate(Player player, ArmorEquipEvent e) {
		getEngineer(player);
		potEffectCaches.put(player.getUniqueId(), new PotionEffectCache(player));
		PlayerTools.clearEffects(player);
		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
		
		ItemStack[] armorContents = ClassListener.currentArmourContents(e, player.getInventory().getArmorContents());
		ArmourCache armourCache = new ArmourCache(armorContents);
		armourCaches.put(player.getUniqueId(), armourCache);
		if (e == null) {
			return armorContents;
		}
		
		ItemStack newItem = e.getNewArmorPiece();
		
		ItemStack boots = armorContents[0];
		boots = ItemUtil.setArmour(boots, 2, 3);
		
		ItemStack legs = armorContents[1]; 
		legs = ItemUtil.setArmour(legs, 2, 6);
		
		ItemStack chest = armorContents[2];
		chest = ItemUtil.setArmour(chest, 2, 7);
		
		ItemStack helm = armorContents[3];
		helm = ItemUtil.setArmour(helm, 2, 2);
		
		newItem = setArmorContent(armorContents, 0, boots, newItem, e.getOldArmorPiece());
		newItem = setArmorContent(armorContents, 1, legs, newItem, e.getOldArmorPiece());
		newItem = setArmorContent(armorContents, 2, chest, newItem, e.getOldArmorPiece());
		newItem = setArmorContent(armorContents, 3, helm, newItem, e.getOldArmorPiece());
		return armorContents;
	}
	
	private DecimalFormat dp1 = new DecimalFormat("##.#");
	
	@Override
	public void handleUI(Player player, UIScoreboard scoreboard) {
	}
	
	@Override
	public void disposeUI(Player player, UIScoreboard scoreboard) {
	}
	
	@Override
	public void handleActionBar(Player player, StringBuilder sb) {
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
		EngineerPlayer engineer = (EngineerPlayer) abPlayer.removeClassPlayer(ClassType.ENGINEER);

		if (potEffectCaches.containsKey(player.getUniqueId())) {
			potEffectCaches.remove(player.getUniqueId()).restore(player);
		}
		ItemStack[] armour;
		if (armourCaches.containsKey(player.getUniqueId())) {
			armour = armourCaches.remove(player.getUniqueId()).restore(player);
		} else {
			armour = ClassListener.currentArmourContents(e, player.getInventory().getArmorContents());
		}
		if (e != null) {
			ArmourType armourType = ArmourType.getArmourType(e.getType());
			armour[armourType.getIndex()] = e.getNewArmorPiece();
		}
		return armour;
	}
	
	public EngineerPlayer getEngineer(Player player) {
		AbilityPlayer abPlayer = am.getAbilityPlayer(player);
		EngineerPlayer engineer = (EngineerPlayer) abPlayer.getClassPlayer(ClassType.ENGINEER);
		if (engineer == null) {
			engineer = new EngineerPlayer(abPlayer);
			abPlayer.putClassPlayer(ClassType.ENGINEER, engineer);
		}
		return engineer;
	}
}
