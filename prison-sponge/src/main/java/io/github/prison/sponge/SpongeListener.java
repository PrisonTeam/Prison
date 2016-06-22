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

package io.github.prison.sponge;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.Optional;

import io.github.prison.Prison;
import io.github.prison.internal.events.BlockPlaceEvent;
import io.github.prison.util.Block;

/**
 * @author Camouflage100
 */
public class SpongeListener {

    private SpongePrison spongePrison;

    public SpongeListener(SpongePrison spongePrison) {
        this.spongePrison = spongePrison;
    }

    public void init() {
        Sponge.getGame().getEventManager().registerListeners(this.spongePrison, this);
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join e) {
        Prison.getInstance().getEventBus().post(new io.github.prison.internal.events.PlayerJoinEvent(new SpongePlayer(e.getTargetEntity())));
    }

    @Listener
    public void onPlayerQuit(ClientConnectionEvent.Disconnect e) {
        Prison.getInstance().getEventBus().post(new io.github.prison.internal.events.PlayerQuitEvent(new SpongePlayer(e.getTargetEntity())));
    }

    @Listener
    public void onBlockPlaced(ChangeBlockEvent.Place e) {
        Cause cause = e.getCause();
        Optional<Player> p = cause.first(Player.class);

        if (p.isPresent()) {
            Prison.getInstance().getEventBus().post(new BlockPlaceEvent(
                    Block.getBlock(cause.last(Player.class).get().getItemInHand().get().getItem().getId()),
                    (new SpongePlayer(cause.last(Player.class).get()))
            ));
        }
    }

}
