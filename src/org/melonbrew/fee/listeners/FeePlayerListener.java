package org.melonbrew.fee.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.melonbrew.fee.Fee;
import org.melonbrew.fee.Phrase;
import org.melonbrew.fee.Session;

public class FeePlayerListener implements Listener {
	private final Fee plugin;
	
	public FeePlayerListener(Fee plugin){
		this.plugin = plugin;
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
		Player player = event.getPlayer();
		
		if (player.hasPermission("fee.exempt")){
			return;
		}
		
		String message = event.getMessage();
		
		String key = plugin.getKey(player, message);
		
		if (key != null){
			Session session = plugin.getSession(player);
			
			if (session != null && session.getCommand().toLowerCase().startsWith(key) && session.isNextCommandFree()){
				plugin.removeSession(event.getPlayer());
				
				return;
			}
			
			double money = plugin.getKeyMoney(key);
			
			if (!plugin.getEconomy().has(player.getName(), money)){
				Phrase.NEED_MONEY.sendWithPrefix(player, plugin.getEconomy().format(money));
				
				event.setCancelled(true);
				
				return;
			}
			
			plugin.removeSession(event.getPlayer());

			plugin.addSession(new Session(player, message));
			
			event.setCancelled(true);

			Phrase.COMMAND_WILL_COST.sendWithPrefix(player, plugin.getEconomy().format(money));
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		removeSession(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event){
		removeSession(event.getPlayer());
	}
	
	private void removeSession(Player player){
		plugin.removeSession(player);
	}
}
