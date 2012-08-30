package org.melonbrew.fee;

import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class FeePlayerListener implements Listener {
	private final Fee plugin;
	
	public FeePlayerListener(Fee plugin){
		this.plugin = plugin;
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
		Player player = event.getPlayer();
		
		String message = event.getMessage().toLowerCase();
		
		//Do group functionality here
		
		ConfigurationSection globalCommands = plugin.getConfig().getConfigurationSection("globalcommands");
		
		Set<String> keys = globalCommands.getKeys(false);
		
		for (String key : keys){
			double money = globalCommands.getDouble(key);
			
			key = key.toLowerCase();
			
			if (message.startsWith(key)){
				player.sendMessage(Phrase.COMMAND_WILL_COST.parseWithPrefix(plugin.getEconomy().format(money)));
			}
		}
	}
}
