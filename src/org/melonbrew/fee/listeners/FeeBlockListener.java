package org.melonbrew.fee.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
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
		
		String firstLine = ChatColor.stripColor(event.getLine(0));
		
		String noColorSign = ChatColor.stripColor(Phrase.SIGN_START.parse());
		
		if (!(firstLine.equalsIgnoreCase(noColorSign))){
			return;
		}
		
		event.setLine(0, Phrase.SIGN_START.parse());
		
		if (!player.hasPermission("fee.sign")){
			cancelSignChange(event);
			
			return;
		}
		
		try {
			Double.parseDouble(event.getLine(1));
		} catch (NumberFormatException e){
			cancelSignChange(event);
			
			Phrase.INVALID_AMOUNT.sendWithPrefix(player);
			
			return;
		}
		
		event.setLine(2, player.getName());
		
		Phrase.CREATED_A_SIGN.sendWithPrefix(player);
	}
	
	private void cancelSignChange(SignChangeEvent event){
		event.setCancelled(true);
		
		Block block = event.getBlock();
		
		block.breakNaturally();
		
		
	}
}
