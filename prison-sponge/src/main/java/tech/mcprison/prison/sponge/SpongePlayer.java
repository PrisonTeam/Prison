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

package tech.mcprison.prison.sponge;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.scoreboard.Scoreboard;
import tech.mcprison.prison.util.Location;

import java.util.UUID;

/**
 * @author Faizaan A. Datoo
 */
public class SpongePlayer extends SpongeCommandSender implements Player {

    org.spongepowered.api.entity.living.player.Player sPlayer;

    public SpongePlayer(org.spongepowered.api.entity.living.player.Player sPlayer) {
        super(sPlayer);
        this.sPlayer = sPlayer;
    }

    @Override public UUID getUUID() {
        return sPlayer.getUniqueId();
    }

    @Override public String getDisplayName() {
        return TextSerializers.LEGACY_FORMATTING_CODE
            .serialize(sPlayer.getDisplayNameData().displayName().get());
    }

    @Override public void setDisplayName(String newDisplayName) {
        Text text = TextSerializers.LEGACY_FORMATTING_CODE.deserialize(newDisplayName);
        sPlayer.getDisplayNameData().displayName().set(text);
    }

    @Override public void give(ItemStack itemStack) {
    }

    @Override public Location getLocation() {
        return null;
    }

    @Override public void teleport(Location location) {

    }

    @Override public boolean isOnline() {
        return false;
    }

    @Override public void setScoreboard(Scoreboard scoreboard) {

    }
}
