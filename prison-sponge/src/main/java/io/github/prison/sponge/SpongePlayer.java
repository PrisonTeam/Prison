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

import io.github.prison.internal.Player;
import io.github.prison.util.Location;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

/**
 * @author SirFaizdat
 */
public class SpongePlayer extends SpongeCommandSender implements Player {

    private org.spongepowered.api.entity.living.player.Player spongePlayer;

    public SpongePlayer(org.spongepowered.api.entity.living.player.Player player) {
        super(player);
        this.spongePlayer = player;
    }

    @Override
    public String getDisplayName() {
        return spongePlayer.getDisplayNameData().get(Keys.DISPLAY_NAME).get().toPlain();
    }

    @Override
    public void setDisplayName(String newDisplayName) {
        spongePlayer.getDisplayNameData().set(Keys.DISPLAY_NAME, Text.of(newDisplayName));
    }

    @Override
    public Location getLocation() {
        // TODO Pitch and yaw
        return new Location(
                new SpongeWorld(spongePlayer.getWorld()),
                spongePlayer.getLocation().getX(),
                spongePlayer.getLocation().getY(),
                spongePlayer.getLocation().getZ()
        );
    }

    @Override
    public void teleport(Location location) {
        spongePlayer.setLocation(new org.spongepowered.api.world.Location<World>(Sponge.getServer().getWorld(location.getWorld().getName()).get(), location.getX(), location.getY(), location.getZ()));
    }
}
