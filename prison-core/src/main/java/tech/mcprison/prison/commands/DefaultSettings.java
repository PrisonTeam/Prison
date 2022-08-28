package tech.mcprison.prison.commands;

/**
 * <p>These are the primary settings that define what the command's primary
 * root will be, and what the command fallback prefix will be. 
 * The command fallback prefix is used when registering commands, it is 
 * added to the beginning of a command until the command is unique, which means
 * it can be added multiple times.
 * </p>
 * 
 * <p>The value for COMMAND_PRIMARY_ROOT_COMMAND should be changed for each plugin.
 * </p>
 * 
 * @author Blue
 *
 */
public class DefaultSettings {

	public static final String COMMAND_PRIMARY_ROOT_COMMAND = "prison";
	public static final String COMMAND_FALLBACK_PREFIX = "prison";
	
}
