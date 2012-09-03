package org.melonbrew.fee.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.melonbrew.fee.Fee;
import org.melonbrew.fee.Phrase;

public class FeeBlockListener implements Listener {
	public FeeBlockListener(Fee plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignChange(SignChangeEvent event){
		Player player = event.getPlayer();
		
		String firstLine = event.getLine(0).replaceAll("(?i)\u00A7[0-F]", "");
		
		if (!(firstLine.startsWith(Phrase.SIGN_START.parse()))){
			return;
		}
		
		if (!player.hasPermission("fee.sign")){
			event.setCancelled(true);
			
			return;
		}
		
		try {
			Double.parseDouble(event.getLine(1));
		} catch (NumberFormatException e){
			event.setCancelled(true);
			
			Phrase.INVALID_AMOUNT.sendWithPrefix(player);
			
			return;
		}
		
		event.setLine(2, player.getName());
		
		Phrase.CREATED_A_SIGN.sendWithPrefix(player);
	}
}
