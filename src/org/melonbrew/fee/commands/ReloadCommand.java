package org.melonbrew.fee.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.melonbrew.fee.Fee;
import org.melonbrew.fee.Phrase;
import org.melonbrew.fee.command.CommandType;
import org.melonbrew.fee.command.SubCommand;

public class ReloadCommand extends SubCommand {
	private final Fee plugin;
	
	public ReloadCommand(Fee plugin){
		super("reload", "fee.reload", "reload", Phrase.COMMAND_RELOAD, CommandType.CONSOLE);
		
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		plugin.reloadConfig();
		
		sender.sendMessage(Phrase.CONFIG_RELOADED.parseWithPrefix());
		
		return true;
	}
}
