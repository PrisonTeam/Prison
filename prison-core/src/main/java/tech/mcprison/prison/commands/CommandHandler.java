/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.commands;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.handlers.*;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.localization.Localizable;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.ChatColor;

import java.lang.reflect.Method;
import java.util.*;

public class CommandHandler {

    // TODO unregisterCommands method, to fix argument duplication on module re-enable

    private Prison plugin;
    private Map<Class<?>, ArgumentHandler<?>> argumentHandlers =
        new HashMap<Class<?>, ArgumentHandler<?>>();
    private Map<PluginCommand, RootCommand> rootCommands = new HashMap<>();

    private PermissionHandler permissionHandler = (sender, permissions) -> {
        for (String perm : permissions) {
            if (!sender.hasPermission(perm)) {
                return false;
            }
        }
        return true;
    };

    private HelpHandler helpHandler = new HelpHandler() {
        private String formatArgument(CommandArgument argument) {
            String def = argument.getDefault();
            if (def.equals(" ")) {
                def = "";
            } else if (def.startsWith("?")) {
                String varName = def.substring(1);
                def = argument.getHandler().getVariableUserFriendlyName(varName);
                if (def == null) {
                    throw new IllegalArgumentException(
                        "The ArgumentVariable '" + varName + "' is not registered.");
                }
                def = ChatColor.GOLD + " | " + ChatColor.WHITE + def;
            } else {
                def = ChatColor.GOLD + " | " + ChatColor.WHITE + def;
            }

            return ChatColor.AQUA + "[" + argument.getName() + def + ChatColor.AQUA + "] "
                + ChatColor.DARK_AQUA + argument.getDescription();
        }

        @Override public String[] getHelpMessage(RegisteredCommand command) {
            ArrayList<String> message = new ArrayList<String>();

            if (command.isSet()) {
                message.add(ChatColor.DARK_AQUA + command.getDescription());
            }

            message.add(getUsage(command));

            if (command.isSet()) {
                for (CommandArgument argument : command.getArguments()) {
                    message.add(formatArgument(argument));
                }
                if (command.getWildcard() != null) {
                    message.add(formatArgument(command.getWildcard()));
                }
                List<Flag> flags = command.getFlags();
                if (flags.size() > 0) {
                    message.add(ChatColor.DARK_AQUA + "Flags:");
                    for (Flag flag : flags) {
                        StringBuilder args = new StringBuilder();
                        for (FlagArgument argument : flag.getArguments()) {
                            args.append(" [" + argument.getName() + "]");
                        }
                        message.add("-" + flag.getIdentifier() + ChatColor.AQUA + args.toString());
                        for (FlagArgument argument : flag.getArguments()) {
                            message.add(formatArgument(argument));
                        }
                    }
                }
                if ( command.getPermissions() != null && command.getPermissions().length > 0 ) {
                	message.add(ChatColor.DARK_AQUA + "Permissions:");
                	
                	StringBuilder sb = new StringBuilder();
                	for ( String perm : command.getPermissions() ) {
						if ( sb.length() > 0 ) {
							sb.append( " " );
						}
						sb.append( perm );
					}
                	sb.insert( 0, ChatColor.AQUA );
                	sb.insert( 0, "   " );
                	message.add( sb.toString() );
                }
            }

            List<RegisteredCommand> subcommands = command.getSuffixes();
            if (subcommands.size() > 0) {
                message.add(ChatColor.DARK_AQUA + "Subcommands:");
                // Force a sorting by use of a TreeSet. Collections.sort() would not work.
                TreeSet<String> subCommandSet = new TreeSet<>();
                for (RegisteredCommand scommand : subcommands) {
                	String subCmd = scommand.getUsage();

                	int subCmdSubCnt = scommand.getSuffixes().size();
                	
                	subCommandSet.add(subCmd + (subCmdSubCnt <= 1 ? "" : 
                			ChatColor.DARK_AQUA + " (" + subCmdSubCnt + 
                			" Subcommands)"));
                }
                for (String subCmd : subCommandSet) {
                	message.add(subCmd);
                }
            }

            return message.toArray(new String[0]);
        }

        @Override public String getUsage(RegisteredCommand command) {
            StringBuilder usage = new StringBuilder();
            usage.append(command.getLabel());

            RegisteredCommand parent = command.getParent();
            while (parent != null) {
                usage.insert(0, parent.getLabel() + " ");
                parent = parent.getParent();
            }

            usage.insert(0, "/");

            if (!command.isSet()) {
                return usage.toString();
            }

            usage.append(ChatColor.AQUA);

            for (CommandArgument argument : command.getArguments()) {
                usage.append(" [" + argument.getName() + "]");
            }

            usage.append(ChatColor.WHITE);

            for (Flag flag : command.getFlags()) {
                usage.append(" (-" + flag.getIdentifier() + ChatColor.AQUA);
                for (FlagArgument arg : flag.getArguments()) {
                    usage.append(" [" + arg.getName() + "]");
                }
                usage.append(ChatColor.WHITE + ")");
            }

            if (command.getWildcard() != null) {
                usage.append(ChatColor.AQUA + " [" + command.getWildcard().getName() + "]");
            }

            return usage.toString();
        }
    };

    private String helpSuffix = "help";

    public CommandHandler() {
        this.plugin = Prison.get();

        registerArgumentHandler(String.class, new StringArgumentHandler());
        registerArgumentHandler(int.class, new IntegerArgumentHandler());
        registerArgumentHandler(double.class, new DoubleArgumentHandler());
        registerArgumentHandler(Player.class, new PlayerArgumentHandler());
        registerArgumentHandler(World.class, new WorldArgumentHandler());
        registerArgumentHandler(BlockType.class, new BlockArgumentHandler());
    }

    @SuppressWarnings("unchecked")
    public <T> ArgumentHandler<? extends T> getArgumentHandler(Class<T> clazz) {
        return (ArgumentHandler<? extends T>) argumentHandlers.get(clazz);
    }

    public HelpHandler getHelpHandler() {
        return helpHandler;
    }

    public void setHelpHandler(HelpHandler helpHandler) {
        this.helpHandler = helpHandler;
    }

    public PermissionHandler getPermissionHandler() {
        return permissionHandler;
    }

    public void setPermissionHandler(PermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
    }

    public <T> void registerArgumentHandler(Class<? extends T> clazz,
        ArgumentHandler<T> argHandler) {
        if (argumentHandlers.get(clazz) != null) {
            throw new IllegalArgumentException(
                "The is already a ArgumentHandler bound to the class " + clazz.getName() + ".");
        }

        argHandler.handler = this;
        argumentHandlers.put(clazz, argHandler);
    }

    public void registerCommands(Object commands) {
        for (Method method : commands.getClass().getDeclaredMethods()) {
            Command commandAnno = method.getAnnotation(Command.class);
            if (commandAnno == null) {
                continue;
            }

            String[] identifiers = commandAnno.identifier().split(" ");
            if (identifiers.length == 0) {
                throw new RegisterCommandMethodException(method, "Invalid identifiers");
            }

            Optional<PluginCommand> rootPcommandOptional =
                plugin.getPlatform().getCommand(identifiers[0]);
            PluginCommand rootPcommand;

            if (!rootPcommandOptional.isPresent()) {
                rootPcommand = new PluginCommand(identifiers[0], commandAnno.description(),
                    "/" + identifiers[0]);
                plugin.getPlatform().registerCommand(rootPcommand);
            } else {
                rootPcommand = rootPcommandOptional.get();
            }

            RegisteredCommand mainCommand = rootCommands
                .computeIfAbsent(rootPcommand, k -> new RootCommand(rootPcommand, this));

            for (int i = 1; i < identifiers.length; i++) {
                String suffix = identifiers[i];
                if (mainCommand.doesSuffixCommandExist(suffix)) {
                    mainCommand = mainCommand.getSuffixCommand(suffix);
                } else {
                    RegisteredCommand newCommand = new RegisteredCommand(suffix, this, mainCommand);
                    mainCommand.addSuffixCommand(suffix, newCommand);
                    mainCommand = newCommand;
                }
            }

            mainCommand.set(commands, method);
        }
    }

    public String getHelpSuffix() {
        return helpSuffix;
    }

    public void setHelpSuffix(String suffix) {
        this.helpSuffix = suffix;
    }

    public boolean onCommand(CommandSender sender, PluginCommand command, String label,
        String[] args) {
        RootCommand rootCommand = rootCommands.get(command);
        if (rootCommand == null) {
            return false;
        }

        if (rootCommand.onlyPlayers() && !(sender instanceof Player)) {
            Prison.get().getLocaleManager().getLocalizable("cantAsConsole")
                .sendTo(sender, Localizable.Level.ERROR);
            return true;
        }

        rootCommand.execute(sender, args);

        return true;
    }

/*
 * ###Tab-Complete###
 * 
 * Disabled for now until a full solution can be implemented for tab complete.
 * 
    public List<String> getRootCommandKeys() {
    	List<String> results = new ArrayList<>();
    	
    	Set<PluginCommand> keys = rootCommands.keySet();
    	for ( PluginCommand pluginCommand : keys ) {
    		// These are the core command sets:
			results.add( pluginCommand.getLabel() );
			
			// Then expand them to all the sub commands that are assoicated with the cores:
			RootCommand cmd = rootCommands.get( pluginCommand );
			List<RegisteredCommand> regCmds = cmd.getSuffixes();
			for ( RegisteredCommand regCmd : regCmds ) {
				results.add( pluginCommand.getLabel() + " " + regCmd.getLabel() );
			}
			
		}
    	
    	return results;
    }
 */
}
