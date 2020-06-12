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

import tech.mcprison.prison.internal.CommandSender;

import java.lang.reflect.Array;

public class WildcardArgument extends CommandArgument {

    private boolean join;

    public WildcardArgument(Arg commandArgAnnotation, Class<?> argumentClass,
        ArgumentHandler<?> argumentHandler, boolean join) {
        super(commandArgAnnotation, argumentClass, argumentHandler);
        this.join = join;
    }

    public WildcardArgument(String name, String description, String def, String verifiers,
        Class<?> argumentClass, ArgumentHandler<?> handler, boolean join) {
        super(name, description, def, verifiers, argumentClass, handler);
        this.join = join;
    }

    @Override public Object execute(CommandSender sender, Arguments args) throws CommandError {
        if (!args.hasNext()) {
            Object o =
                getHandler().handle(sender, this, getDefault().equals(" ") ? "" : getDefault());
            if (join) {
                return o;
            } else {
                Object array = Array.newInstance(getArgumentClass(), 1);
                Array.set(array, 0, o);
                return array;
            }
        }

        if (join) {
            StringBuilder sb = new StringBuilder();

            while (args.hasNext()) {
                sb.append(args.nextArgument()).append(" ");
            }

            return getHandler()
                .handle(sender, this, CommandUtil.escapeArgumentVariable(sb.toString().trim()));
        } else {
            Object array = Array.newInstance(getArgumentClass(), args.over());

            for (int i = 0; i < args.over(); i++) {
                Array.set(array, i, getHandler()
                    .handle(sender, this, CommandUtil.escapeArgumentVariable(args.nextArgument())));
            }

            return array;
        }
    }

    public boolean willJoin() {
        return join;
    }
}
