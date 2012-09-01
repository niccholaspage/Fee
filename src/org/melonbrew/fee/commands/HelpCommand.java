package org.melonbrew.fee.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.melonbrew.fee.Fee;
import org.melonbrew.fee.FeeCommand;
import org.melonbrew.fee.Phrase;
import org.melonbrew.fee.command.CommandType;
import org.melonbrew.fee.command.SubCommand;

public class HelpCommand extends SubCommand {
	private final Fee plugin;
	
	private final FeeCommand command;
	
	public HelpCommand(Fee plugin, FeeCommand command){
		super("help,?", "fee.?", "help", Phrase.COMMAND_HELP, CommandType.CONSOLE);
		
		this.plugin = plugin;
		
		this.command = command;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		sender.sendMessage(plugin.getEqualMessage(Phrase.HELP.parse(), 10));
		
		ChatColor operatorColor = ChatColor.DARK_GRAY;
		
		ChatColor textColor = ChatColor.GRAY;
		
		sender.sendMessage(textColor + Phrase.HELP_ARGUMENTS.parse(operatorColor + "[]" + textColor, operatorColor + "()" + textColor));
		
		for (SubCommand command : this.command.getCommands()){
			if (command.getName().equalsIgnoreCase(getName())){
				continue;
			}
			
			if (!sender.hasPermission(command.getPermission())){
				continue;
			}
			
			if (!(sender instanceof Player) && command.getCommandType() == CommandType.PLAYER){
				continue;
			}
			
			sender.sendMessage(this.command.parse(commandLabel, command) + textColor + " - " + command.getDescription().parse());
		}
		
		sender.sendMessage(plugin.getEndEqualMessage(27));
		
		return true;
	}
}
