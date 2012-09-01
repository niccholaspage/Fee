package org.melonbrew.fee;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.melonbrew.fee.commands.YesCommand;

public class Fee extends JavaPlugin {
	private Logger log;
	
	private Economy economy;
	
	private Set<Session> sessions;
	
	public void onEnable(){
		log = getServer().getLogger();
		
		sessions = new HashSet<Session>();
		
		Phrase.init(this);
		
		if (!setupEconomy()){
			log(Phrase.VAULT_HOOK_FAILED);
			
			getServer().getPluginManager().disablePlugin(this);
			
			return;
		}
		
		new FeePlayerListener(this);
		
		getConfig().options().copyDefaults(true);
		
		getConfig().options().header("Fee Config - melonbrew.org\n" +
				"# serveraccount - An account for fees to go too. (Blank for none)\n" +
				"# closespeed - How many milliseconds (1000 milliseconds is 1 second) before doors, trapdoors and gates auto close.\n" +
				"# globalcommands - A command followed by it's cost. For all players.\n" +
				"# groupcommands - Per group commands.);\n");
		
		saveConfig();
		
		getCommand("yes").setExecutor(new YesCommand(this));
	}
	
	public String getKey(String message){
		message = message.toLowerCase();
		
		//Group stuff here
		
		ConfigurationSection globalCommands = getConfig().getConfigurationSection("globalcommands");
		
		Set<String> keys = globalCommands.getKeys(false);
		
		for (String key : keys){
			if (message.startsWith(key.toLowerCase())){
				return key;
			}
		}
		
		return null;
	}
	
	public double getKeyMoney(String key){
		return getConfig().getDouble("globalcommands." + key);
	}
	
	public Session getSession(Player player){
		for (Session session : sessions){
			if (session.getPlayer().equals(player)){
				return session;
			}
		}
		
		return null;
	}
	
	public void addSession(Session session){
		sessions.add(session);
	}
	
	public void removeSession(Player player){
		for (Session session : new HashSet<Session>(sessions)){
			if (session.getPlayer().equals(player)){
				sessions.remove(session);
			}
		}
	}
	
	public Set<Session> getSessions(){
		return sessions;
	}
	
	public void log(String message){
		log.info("[Fee] " + message);
	}
	
	public void log(Phrase phrase, String... args){
		log(phrase.parse(args));
	}

	private boolean setupEconomy(){
		economy = null;
		
		if (getServer().getPluginManager().getPlugin("Vault") != null){
			RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);

			if (economyProvider != null){
				economy = economyProvider.getProvider();
			}
		}

		return economy != null;
	}
	
	public Economy getEconomy(){
		return economy;
	}
	
	public String getMessagePrefix(){
		return ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Fee" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
	}
}
