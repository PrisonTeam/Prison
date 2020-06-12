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

package tech.mcprison.prison.commands.handlers;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.ArgumentHandler;
import tech.mcprison.prison.commands.CommandArgument;
import tech.mcprison.prison.commands.TransformError;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.util.BlockType;

public class BlockArgumentHandler extends ArgumentHandler<BlockType> {

    public BlockArgumentHandler() {
    }

    @Override
    public BlockType transform(CommandSender sender, CommandArgument argument, String value)
        throws TransformError {
        BlockType m = null;

        // Try block legacy (numerical) ID first
        try {
            m = BlockType.getBlock(Integer.parseInt(value));
        } catch (NumberFormatException ignored) {
        }

        if (m != null) {
            return m;
        }

        // Now try new block IDs

        m = BlockType.getBlock(value);

        if (m != null) {
            return m;
        }

        // Now try id:data format
        if (value.contains(":")) {
            int id;
            short data;
            try {
                id = Integer.parseInt(value.split(":")[0]);
                data = Short.parseShort(value.split(":")[1]);
            } catch (NumberFormatException ignored) {
                throw new TransformError(
                    Prison.get().getLocaleManager().getLocalizable("blockParseError")
                        .withReplacements(value).localizeFor(sender));
            }
            m = BlockType.getBlockWithData(id, data);
        }

        if (m != null) {
            return m;
        }

        // No more checks, just fail

        throw new TransformError(Prison.get().getLocaleManager().getLocalizable("blockParseError")
            .withReplacements(value).localizeFor(sender));
    }
}
