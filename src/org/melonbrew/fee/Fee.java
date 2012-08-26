package org.melonbrew.fee;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Fee extends JavaPlugin {
	private Logger log;
	
	public void onEnable(){
		log = getServer().getLogger();
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
