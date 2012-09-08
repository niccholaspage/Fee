package org.melonbrew.fee;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Session {
	private final Player player;
	
	private final String command;
	
	private final Block block;
	
	private boolean nextCommandFree;
	
	public Session(Player player, String command){
		this(player, command, null);
	}
	
	public Session(Player player, Block block){
		this(player, null, block);
	}
	
	public Session(Player player, String command, Block block){
		this.player = player;
		
		this.command = command;
		
		this.block = block;
		
		nextCommandFree = false;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public String getCommand(){
		return command;
	}
	
	public Block getBlock(){
		return block;
	}
	
	public void setNextCommandFree(boolean nextCommandFree){
		this.nextCommandFree = nextCommandFree;
	}
	
	public boolean isNextCommandFree(){
		return nextCommandFree;
	}
}
