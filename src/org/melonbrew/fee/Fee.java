package org.melonbrew.fee;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Fee extends JavaPlugin {
	private Logger log;
	
	private Economy economy;
	
	public void onEnable(){
		log = getServer().getLogger();
		
		Phrase.init(this);
		
		if (!setupEconomy()){
			log(Phrase.VAULT_HOOK_FAILED);
			
			getServer().getPluginManager().disablePlugin(this);
			
			return;
		}
		
		getConfig().options().copyDefaults(true);
		
		getConfig().options().header("Fee Config - melonbrew.org\n" +
				"# serveraccount - An account for fees to go too. (Blank for none)\n" +
				"# closespeed - How many milliseconds (1000 milliseconds is 1 second) before doors, trapdoors and gates auto close.\n" +
				"# globalcommands - A command followed by it's cost. For all players.\n" +
				"# groupcommands - Per group commands.);\n");
		
		saveConfig();
	}
	
	public void log(String message){
		log.info("[Fe] " + message);
	}
	
	public void log(Phrase phrase, String... args){
		log(phrase.parse(args));
	}

	private boolean setupEconomy(){
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		
		if (economyProvider != null){
			economy = economyProvider.getProvider();
		}

		return economy != null;
	}
	
	public Economy getEconomy(){
		return economy;
	}
	
	public String getMessagePrefix(){
		return ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Fe" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
	}
}
