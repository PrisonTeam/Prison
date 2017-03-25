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

package tech.mcprison.prison.sponge.game;

import com.flowpowered.math.vector.Vector3d;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.scoreboard.Scoreboard;
import tech.mcprison.prison.sponge.SpongeUtil;
import tech.mcprison.prison.util.Gamemode;
import tech.mcprison.prison.util.Location;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Faizaan A. Datoo
 */
public class SpongePlayer extends SpongeCommandSender implements Player {

    private org.spongepowered.api.entity.living.player.Player spongePlayer;

    public SpongePlayer(org.spongepowered.api.entity.living.player.Player spongePlayer) {
        super(spongePlayer);
        this.spongePlayer = spongePlayer;
    }

    @Override public UUID getUUID() {
        return spongePlayer.getUniqueId();
    }

    @Override public String getDisplayName() {
        return SpongeUtil.spongeTextToPrison(spongePlayer.getDisplayNameData().displayName().get());
    }

    @Override public void setDisplayName(String newDisplayName) {
        spongePlayer.getDisplayNameData().displayName()
            .set(SpongeUtil.prisonTextToSponge(newDisplayName));
    }

    @Override public void give(ItemStack itemStack) {
    }

    @Override public Location getLocation() {
        return SpongeUtil.spongeLocationToPrison(spongePlayer.getLocation());
    }

    @Override public void teleport(Location location) {
        spongePlayer.setLocationAndRotation(SpongeUtil.prisonLocationToSponge(location),
            new Vector3d(location.getPitch(), location.getYaw(), 0));
    }

    @Override public boolean isOnline() {
        return spongePlayer.isOnline();
    }

    @Override public void setScoreboard(Scoreboard scoreboard) {
    }

    @Override public Gamemode getGamemode() {
        return null;
    }

    @Override public void setGamemode(Gamemode gamemode) {
    }

    @Override public Optional<String> getLocale() {
        return null;
    }

    @Override public boolean isOp() {
        return false;
    }

    @Override public boolean doesSupportColors() {
        return spongePlayer.isChatColorsEnabled();
    }

    @Override public Inventory getInventory() {
        return null;
    }

    @Override public void updateInventory() {
        // Not needed for sponge :)
    }

}
