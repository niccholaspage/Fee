package org.melonbrew.fee;

import org.bukkit.command.CommandSender;

public enum Phrase {
	VAULT_HOOK_FAILED("Could not hook into Vault - disabling Fee."),
	COMMAND_WILL_COST("This command will cost $1. Type /yes to use this command."),
	NEED_MONEY("You need $1 to use this command."),
	NO_PENDING_COMMAND("You don't have a pending command."),
	YOU_ARE_NOT_A_PLAYER("Silly console, you aren't a player!"),
	YOU_ARE_NOT_A_PLAYER_TWO("Silly console, Trix are for kids!"),
	COMMAND_NEEDS_ARGUMENTS("That command needs arguments."),
	COMMAND_NOT_CONSOLE("The command '$1' cannot be used in the console."),
	NO_PERMISSION_FOR_COMMAND("You do not have permission to use that command."),
	TRY_COMMAND("Try $1"),
	HELP("Fee Help"),
	HELP_ARGUMENTS("$1 Required, $2 Optional"),
	CONFIG_RELOADED("The config has been reloaded."),
	SIGN_START("[Fee]"),
	INVALID_AMOUNT("You cannot create a sign with an invalid amount of money."),
	NOT_ABOVE_SUPPORTED_ITEM("The sign is not above a supported block."),
	CREATED_A_SIGN("You've created a Fee sign."),
	COMMAND_HELP("Gives you help", true),
	COMMAND_RELOAD("Reloads the config", true);
	
	private static Fee plugin;
	
	private final String defaultMessage;
	
	private final boolean categorized;

	private String message;
	
	private Phrase(String defaultMessage){
		this(defaultMessage, false);
	}

	private Phrase(String defaultMessage, boolean categorized){
		this.defaultMessage = defaultMessage;
		
		this.categorized = categorized;

		message = defaultMessage + "";
	}

	public void setMessage(String message){
		this.message = message;
	}

	private String getMessage(){
		return message;
	}

	public void reset(){
		message = defaultMessage + "";
	}

	public String getConfigName(){
		String name = name();
		
		if (categorized){
			name = name.replaceFirst("_", ".");
		}
		
		return name().toLowerCase();
	}

	public String parse(String... params){
		String parsedMessage = getMessage();

		if (params != null){
			for (int i = 0; i < params.length; i++){
				parsedMessage = parsedMessage.replace("$" + (i + 1), params[i]);
			}
		}

		return parsedMessage;
	}
	
	public String parseWithoutSpaces(String... params){
		return parse(params).replace(" ", "");
	}
	
	private String parseWithPrefix(String... params){
		return plugin.getMessagePrefix() + parse(params);
	}
	
	public void send(CommandSender sender, String... params){
		sender.sendMessage(parse(params));
	}
	
	public void sendWithPrefix(CommandSender sender, String... params){
		sender.sendMessage(parseWithPrefix(params));
	}
	
	public static void init(Fee instance){
		plugin = instance;
	}
}
