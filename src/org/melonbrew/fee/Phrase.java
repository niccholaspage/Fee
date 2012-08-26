package org.melonbrew.fee;

public enum Phrase {
	GABEN("GABEN");
	
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
	
	public String parseWithPrefix(String... params){
		return plugin.getMessagePrefix() + parse(params);
	}
	
	public static void init(Fee instance){
		plugin = instance;
	}
}
