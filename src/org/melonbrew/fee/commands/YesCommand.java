package org.melonbrew.fee.commands;

import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.melonbrew.fee.Fee;
import org.melonbrew.fee.Phrase;
import org.melonbrew.fee.Session;

public class YesCommand implements CommandExecutor {
	private final Fee plugin;
	
	private final Random random;
	
	public YesCommand(Fee plugin){
		this.plugin = plugin;
		
		random = new Random();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)){
			if (random.nextFloat() <= 0.50F){
				sender.sendMessage(Phrase.YOU_ARE_NOT_A_PLAYER.parseWithPrefix());
			}else {
				sender.sendMessage(Phrase.YOU_ARE_NOT_A_PLAYER_TWO.parseWithPrefix());
			}
			
			return true;
		}
		
		Player player = (Player) sender;
		
		Session session = plugin.getSession(player);
		
		if (session == null){
			sender.sendMessage(Phrase.NO_PENDING_COMMAND.parseWithPrefix());
			
			return true;
		}
		
		String command = session.getCommand();
		
		if (plugin.getKeyMoney(command) == -1){
			plugin.removeSession(player);
			
			sender.sendMessage(Phrase.NO_PENDING_COMMAND.parseWithPrefix());
			
			return true;
		}
		
		plugin.removeSession(player);
		
		player.chat(command);
		
		return true;
	}

}
