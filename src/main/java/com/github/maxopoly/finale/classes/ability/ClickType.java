package com.github.maxopoly.finale.classes.ability;

import java.util.Arrays;
import java.util.List;

import org.bukkit.event.block.Action;

public enum ClickType {

	NONE(null),
	LEFT(Arrays.asList(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK)),
	RIGHT(Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK));
	
	private List<Action> validActions;
	
	private ClickType(List<Action> validActions) {
		this.validActions = validActions;
	}
	
	public List<Action> getValidActions() {
		return validActions;
	}
	
	public static ClickType getClickType(Action a) {
		for (ClickType clickType : ClickType.values()) {
			List<Action> validActions = clickType.getValidActions();
			if (validActions != null && validActions.contains(a)) {
				return clickType;
			}
		}
		return null;
	}
	
}
