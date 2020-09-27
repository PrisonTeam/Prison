/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
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
import tech.mcprison.prison.internal.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class CommandArgument implements ExecutableArgument {

    private final String name;
    private final String description;
    private final String def;
    private final Map<String, String[]> verifyArguments;
    private final ArgumentHandler<?> handler;
    private final Class<?> argumentClass;

    private Map<String, String> overrideMessages = new HashMap<String, String>();

    public CommandArgument(Arg commandArgAnnotation, Class<?> argumentClass,
        ArgumentHandler<?> argumentHandler) {
        this(commandArgAnnotation.name(), commandArgAnnotation.description(),
            commandArgAnnotation.def(), commandArgAnnotation.verifiers(), argumentClass,
            argumentHandler);
    }

    public CommandArgument(String name, String description, String def, String verifiers,
        Class<?> argumentClass, ArgumentHandler<?> handler) {
        this.name = name;
        this.description = description;
        this.def = def;
        this.verifyArguments = CommandUtil.parseVerifiers(verifiers);
        this.handler = handler;
        this.argumentClass = argumentClass;
    }

    @Override public Object execute(CommandSender sender, Arguments args) throws CommandError {
        String arg;
        if (!args.hasNext()) {
            if (def.equals(" ")) {
                throw new CommandError(
                    Prison.get().getLocaleManager().getLocalizable("missingArgument")
                        .withReplacements(name).localizeFor(sender), true);
            }

            arg = def;
        } else {
            arg = CommandUtil.escapeArgumentVariable(args.nextArgument());
        }

        return handler.handle(sender, this, arg);
    }

    private String formatMessage(String msg, String[] vars) {
        msg = msg.replace("%p", name);

        for (int i = 1; i <= vars.length; i++) {
            msg = msg.replace("%" + i, vars[i - 1]);
        }

        return msg.replaceAll("%\\d+", "<variable not available>");
    }

    public Class<?> getArgumentClass() {
        return argumentClass;
    }

    public String getDefault() {
        return def;
    }

    public String getDescription() {
        return description;
    }

    public ArgumentHandler<?> getHandler() {
        return handler;
    }

    public String getMessage(String node) {
        return getMessage(node, new String[0]);
    }

    public String getMessage(String node, String... vars) {
        String msg = overrideMessages.get(node);

        if (msg != null) {
            return formatMessage(msg, vars);
        }

        msg = handler.getMessage(node);

        if (msg != null) {
            return formatMessage(msg, vars);
        }

        throw new IllegalArgumentException("The node \"" + node + "\" is not available.");
    }

    public String getName() {
        return name;
    }

    public Map<String, String[]> getVerifyArguments() {
        return verifyArguments;
    }
}
