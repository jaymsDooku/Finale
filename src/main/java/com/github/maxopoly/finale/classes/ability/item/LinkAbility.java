package com.github.maxopoly.finale.classes.ability.item;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.classes.ClassType;
import com.github.maxopoly.finale.classes.ability.AbilityPlayer;
import com.github.maxopoly.finale.classes.ability.ClickType;
import com.github.maxopoly.finale.classes.archer.ArcherConfig;
import com.github.maxopoly.finale.classes.archer.ArcherPlayer;
import com.google.common.collect.Sets;

import io.jayms.serenno.util.ParticleTools;
import io.jayms.serenno.util.ParticleTools.ParticlePlay;
import io.jayms.serenno.util.PlayerTools;
import net.md_5.bungee.api.ChatColor;

public class LinkAbility extends ItemAbility {

	public static final String NAME = "linker";
	
	private ArcherConfig config = Finale.getPlugin().getManager().getArcherConfig();
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDisplayName() {
		return ChatColor.GREEN + "Linker";
	}
	
	@Override
	public boolean enable(AbilityPlayer user, boolean instantCD) {
		Entity target = PlayerTools.getTargetedEntity(user.getBukkitPlayer(), config.getLinkerRange());
		if (target == null) {
			user.sendMessage(ChatColor.RED + "Use the linker on an ally player.");
			return false;
		}
		
		if (!(target instanceof Player)) {
			user.sendMessage(ChatColor.RED + "Use the linker on an ally player.");
			return false;
		}
		
		Player playerTarget = (Player) target;

		if (!super.enable(user, instantCD)) {
			return false;
		}
		ArcherPlayer archer = (ArcherPlayer) user.getClassPlayer(ClassType.ARCHER);
		Location start = user.getBukkitPlayer().getEyeLocation();
		Location dest = playerTarget.getEyeLocation();
		
		if (archer.isLinked(playerTarget)) {
			archer.unlink(playerTarget);
			archer.notify(ChatColor.YELLOW + "You have " + ChatColor.RED + "unlinked" + ChatColor.YELLOW + " with " + ChatColor.RED + playerTarget.getName());
			ParticleTools.drawLine(start, dest, 4 * (int) dest.distance(start), new ParticlePlay() {
				
				@Override
				public void play(Location loc) {
					ParticleTools.displayColoredParticle(loc, "#FF2200");
				}
				
			});
		} else {
			archer.link(playerTarget);
			archer.notify(ChatColor.YELLOW + "You have " + ChatColor.GREEN + "linked" + ChatColor.YELLOW + " with " + ChatColor.GREEN + playerTarget.getName());
			ParticleTools.drawLine(start, dest, 4 * (int) dest.distance(start), new ParticlePlay() {
				
				@Override
				public void play(Location loc) {
					ParticleTools.displayColoredParticle(loc, "#1CFC03");
				}
				
			});
		}
		
		disable(user);
		putOnCooldown(user);
		return true;
	}

	@Override
	public boolean update(AbilityPlayer user) {
		return true;
	}

	@Override
	public long getBaseCooldown() {
		return config.getLinkerCooldown();
	}

	@Override
	public Material getRequiredMaterial() {
		return Material.TRIPWIRE_HOOK;
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

	@Override
	public boolean consumeOnUse() {
		return false;
	}

}
