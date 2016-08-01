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

package tech.mcprison.prison.adapters;

import com.google.gson.*;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.util.Location;

import java.lang.reflect.Type;

/**
 * A serializer and deserializer for locations.
 *
 * @author SirFaizdat
 * @since 3.0
 */
public class LocationAdapter implements JsonDeserializer<Location>, JsonSerializer<Location> {

    public static final String WORLD = "world";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String Z = "z";
    public static final String PITCH = "pitch";
    public static final String YAW = "yaw";

    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
        if (!json.isJsonObject())
            return null;
        JsonObject object = json.getAsJsonObject();

        String worldName = object.getAsJsonPrimitive(WORLD).getAsString();
        World world = Prison.getInstance().getPlatform().getWorld(worldName);

        double x = object.getAsJsonPrimitive(X).getAsDouble();
        double y = object.getAsJsonPrimitive(Y).getAsDouble();
        double z = object.getAsJsonPrimitive(Z).getAsDouble();
        float pitch = object.getAsJsonPrimitive(PITCH).getAsFloat();
        float yaw = object.getAsJsonPrimitive(YAW).getAsFloat();

        return new Location(world, x, y, z, pitch, yaw);
    }

    @Override
    public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.add(WORLD, new JsonPrimitive(src.getWorld().getName()));
        object.add(X, new JsonPrimitive(src.getX()));
        object.add(Y, new JsonPrimitive(src.getY()));
        object.add(Z, new JsonPrimitive(src.getZ()));
        object.add(PITCH, new JsonPrimitive(src.getPitch()));
        object.add(YAW, new JsonPrimitive(src.getYaw()));

        return object;
    }
}
