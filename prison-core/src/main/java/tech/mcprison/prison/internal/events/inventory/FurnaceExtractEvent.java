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

package tech.mcprison.prison.internal.events.inventory;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.util.BlockType;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class FurnaceExtractEvent {

    private int itemAmount;
    private int expToDrop;
    private Block block;
    private BlockType blockType;
    private Player player;

    public FurnaceExtractEvent(Player player, Block block, BlockType itemType, int itemAmount,
        int exp) {
        this.player = player;
        this.block = block;
        this.blockType = itemType;
        this.itemAmount = itemAmount;
        this.expToDrop = exp;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public BlockType getItemType() {
        return blockType;
    }

    public Player getPlayer() {
        return player;
    }

    public int getExpToDrop() {
        return expToDrop;
    }

    public void setExpToDrop(int exp) {
        expToDrop = exp;
    }

    public Block getBlock() {
        return block;
    }

}
