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
				Phrase.YOU_ARE_NOT_A_PLAYER.sendWithPrefix(sender);
			}else {
				Phrase.YOU_ARE_NOT_A_PLAYER_TWO.sendWithPrefix(sender);
			}
			
			return true;
		}
		
		Player player = (Player) sender;
		
		Session session = plugin.getSession(player);
		
		if (session == null){
			Phrase.NO_PENDING_COMMAND.sendWithPrefix(sender);
			
			return true;
		}
		
		String command = session.getCommand();
		
		double money = plugin.getKeyMoney(command);
		
		if (money == -1){
			plugin.removeSession(player);

			Phrase.NO_PENDING_COMMAND.sendWithPrefix(sender);
			
			return true;
		}
		
		if (!plugin.getEconomy().has(player.getName(), money)){
			Phrase.NEED_MONEY.sendWithPrefix(sender, plugin.getEconomy().format(money));
			
			return true;
		}
		
		plugin.getEconomy().withdrawPlayer(player.getName(), money);
		
		String reciever = plugin.getConfig().getString("serveraccount");
		
		if (plugin.getEconomy().hasAccount(reciever)){
			plugin.getEconomy().depositPlayer(reciever, money);
		}
		
		session.setNextCommandFree(true);
		
		player.chat(command);
		
		return true;
	}

}
