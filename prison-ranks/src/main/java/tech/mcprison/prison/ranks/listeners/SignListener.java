/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2016 The Prison Team
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

package tech.mcprison.prison.ranks.listeners;

import com.google.common.eventbus.Subscribe;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.platform.Sign;
import tech.mcprison.prison.platform.events.PlayerInteractEvent;
import tech.mcprison.prison.ranks.RanksModule;
import tech.mcprison.prison.util.Block;
import tech.mcprison.prison.util.ChatColor;


/**
 * @author Camouflage100
 */
public class SignListener {
    private RanksModule ranksModule;

    public SignListener(RanksModule ranksModule) {
        this.ranksModule = ranksModule;
    }

    public void init() {
        Prison.getInstance().getEventBus().register(this);
    }

    @Subscribe public void onInteract(PlayerInteractEvent e) {
        try {
            if (e.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                Block clicked = e.getClicked().getWorld().getBlockAt(e.getClicked());

                if (isSign(clicked)) {
                    Sign sign = Prison.getInstance().getPlatform().getSign(e.getClicked());

                    if (sign.getLines()[0].contains(ChatColor.GOLD + "[Rankup]")) {
                        e.getPlayer().dispatchCommand("rankup");
                    }


                    if (sign.getLines()[0].equalsIgnoreCase(ranksModule.getName()) && sign
                        .getLines()[1].equalsIgnoreCase("rankup") && e.getPlayer()
                        .hasPermission("prison.ranks.ranksign")) {
                        makeSign(sign);
                    }
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private boolean isSign(Block block) {
        return block == Block.SIGN || block == Block.STANDING_SIGN_BLOCK
            || block == Block.WALL_MOUNTED_SIGN_BLOCK;
    }

    private void makeSign(Sign sign) {
        sign.setLine(0, ChatColor.GOLD + "[Rankup]");
        sign.setLine(1, "");
        sign.setLine(2, "");
        sign.setLine(3, "");
    }
}
