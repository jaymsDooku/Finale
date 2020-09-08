package com.github.maxopoly.finale.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.maxopoly.finale.Finale;
import com.google.common.collect.Sets;

import io.jayms.serenno.ui.ActionBarHandler;
import io.jayms.serenno.ui.UI;
import io.jayms.serenno.ui.UIManager;
import net.md_5.bungee.api.ChatColor;

public class CPSHandler {

	private Map<UUID, List<Long>> playerClicks = new ConcurrentHashMap<>();

	public int getCPS(UUID uuid) {
		final long time = System.currentTimeMillis();
		List<Long> clicks = this.playerClicks.get(uuid);
		if (clicks == null) {
			return 0;
		}
	    Iterator<Long> iterator = clicks.iterator();
	    while (iterator.hasNext()) {
	        if (iterator.next() + Finale.getPlugin().getManager().getCombatConfig().getCpsCounterInterval() < time) {
	            iterator.remove();
	        }
	    }
	    return this.playerClicks.get(uuid).size();
	}
	 
	public void updateClicks(Player player) {
		List<Long> clicks = playerClicks.get(player.getUniqueId());
		if (clicks == null) {
			clicks = new ArrayList<>();
			playerClicks.put(player.getUniqueId(), clicks);
		}
		clicks.add(System.currentTimeMillis());
	}
	 
	private Set<Player> showCps = Sets.newHashSet();
		
	public void showCPS(Player player) {
		showCps.add(player);
	}
	
	public void hideCPS(Player player) {
		showCps.remove(player);
	}
	
	public boolean isShowingCPS(Player player) {
		return showCps.contains(player);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		UI ui = UIManager.getUIManager().getScoreboard(p);
		ui.getActionBarHandlers().add(new ActionBarHandler() {
			
			@Override
			public StringBuilder handle(Player player, StringBuilder sb) {
				if (showCps.contains(player)) {
					sb.append("   " + ChatColor.GOLD + ChatColor.BOLD + "CPS: " + ChatColor.YELLOW + Finale.getPlugin().getManager().getCPSHandler().getCPS(p.getUniqueId()) + ChatColor.GOLD + "s" + "   ");
				}
				return sb;
			}
		});
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		
		if (isShowingCPS(player)) {
			hideCPS(player);
		}
		
		if (playerClicks.containsKey(player.getUniqueId())) {
			playerClicks.remove(player.getUniqueId());
		}
	}
}
