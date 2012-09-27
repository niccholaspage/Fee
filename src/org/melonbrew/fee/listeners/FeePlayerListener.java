package org.melonbrew.fee.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
	public void onPlayerInteract(PlayerInteractEvent event){
		Block block = event.getClickedBlock();
		
		if (block == null){
			return;
		}
		
		Player player = event.getPlayer();
		
		Material type = block.getType();
		
		if (event.getAction() == Action.LEFT_CLICK_BLOCK){
			if (type == Material.FURNACE || type == Material.CHEST){
				return;
			}
		}
		
		if (!plugin.containsSupportedBlock(type)){
			return;
		}
		
		Sign sign = plugin.getSign(player, block, true);
		
		if (sign == null){
			return;
		}
		
		String name = sign.getLine(2);
		
		if (name.equalsIgnoreCase(player.getName())){
			return;
		}
		
		plugin.removeSession(player);
		
		plugin.addSession(new Session(player, block));
		
		event.setCancelled(true);
		
		Phrase.BLOCK_WILL_COST.sendWithPrefix(player, plugin.getEconomy().format(Double.parseDouble(sign.getLine(1))));
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
