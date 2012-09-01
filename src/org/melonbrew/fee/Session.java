package org.melonbrew.fee;

import org.bukkit.entity.Player;

public class Session {
	private final Player player;
	
	private final String command;
	
	private boolean nextCommandFree;
	
	public Session(Player player, String command){
		this.player = player;
		
		this.command = command;
		
		nextCommandFree = false;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public String getCommand(){
		return command;
	}
	
	public void setNextCommandFree(boolean nextCommandFree){
		this.nextCommandFree = nextCommandFree;
	}
	
	public boolean isNextCommandFree(){
		return nextCommandFree;
	}
}
