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

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.util.Direction;
import tech.mcprison.prison.internal.block.BlockFace;
import tech.mcprison.prison.sponge.game.SpongeWorld;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Text;

/**
 * @author Faizaan A. Datoo
 */
public class SpongeUtil {

    // Convention: If name conflicts, Prison classes should be imported, while Sponge classes should use fully qualified names.

    private SpongeUtil() {
    }

    /**
     * Message may include &-prefixed color codes.
     */
    @SuppressWarnings( "deprecation" )
	public static org.spongepowered.api.text.Text prisonTextToSponge(String message) {
        return TextSerializers.LEGACY_FORMATTING_CODE
            .deserialize(Text.translateAmpColorCodes(message));
    }

    @SuppressWarnings( "deprecation" )
	public static String spongeTextToPrison(org.spongepowered.api.text.Text message) {
        return TextSerializers.LEGACY_FORMATTING_CODE.serialize(message);
    }

    public static Direction prisonBlockFaceToSponge(BlockFace blockFace) {

        switch (blockFace) {
            case TOP:
                return Direction.UP;
            case BOTTOM:
                return Direction.DOWN;
            default:
                return Direction.valueOf(blockFace.name());
        }

    }

    public static Location spongeLocationToPrison(
        org.spongepowered.api.world.Location<org.spongepowered.api.world.World> spongeLoc) {

        return new Location(new SpongeWorld(spongeLoc.getExtent()), spongeLoc.getX(),
            spongeLoc.getY(), spongeLoc.getZ());
    }

    public static Location spongeLocationToPrison(
        org.spongepowered.api.world.Location<org.spongepowered.api.world.World> spongeLoc,
        Vector3d rotation) {
        float yaw = (float) ((rotation.getY() + 90) % 360);
        float pitch = (float) ((rotation.getX()) * -1);

        return new Location(new SpongeWorld(spongeLoc.getExtent()), spongeLoc.getX(),
            spongeLoc.getY(), spongeLoc.getZ(), pitch, yaw);
    }

    public static org.spongepowered.api.world.Location<org.spongepowered.api.world.World> prisonLocationToSponge(
        Location prisonLoc) {

        return new org.spongepowered.api.world.Location<>(
            ((SpongeWorld) prisonLoc.getWorld()).getSpongeWorld(), prisonLoc.getX(),
            prisonLoc.getY(), prisonLoc.getZ());
    }

}
