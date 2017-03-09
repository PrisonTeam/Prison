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

package tech.mcprison.prison.store;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tech.mcprison.prison.store.adapter.LocationAdapter;
import tech.mcprison.prison.util.Location;

/**
 * A singleton which contains an initialized GSON object.
 *
 * @author Faizaan A. Datoo
 */
public class GsonSingleton {

  private static GsonSingleton instance = new GsonSingleton();
  private Gson gson;

  private GsonSingleton() {
    GsonBuilder builder = new GsonBuilder();
    builder.setPrettyPrinting();
    builder.disableHtmlEscaping();
    builder.setExclusionStrategies(new GsonExclusionStrategy());
    builder.registerTypeAdapter(Location.class, new LocationAdapter());
    gson = builder.create();
  }

  public static GsonSingleton getInstance() {
    return instance;
  }

  public Gson getGson() {
    return gson;
  }

}
