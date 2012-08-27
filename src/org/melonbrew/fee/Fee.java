package org.melonbrew.fee;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Fee extends JavaPlugin {
	private Logger log;
	
	public void onEnable(){
		log = getServer().getLogger();
		
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
	
	public String getMessagePrefix(){
		return ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Fe" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
	}
}
