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

package tech.mcprison.prison.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import tech.mcprison.prison.Prison;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

/**
 * This class takes care of the items.csv containing 8000+ different name combinations for blocks.
 *
 * @author Dylan M. Perks
 * @since API 0.1
 */
public class ItemManager {

    private Multimap<BlockType, String> items;

    public ItemManager() throws Exception {
        File file = new File(Prison.get().getDataFolder(), "/items.csv");
        items = ArrayListMultimap.create();

        if (!file.exists()) {
            InputStream inputStream = getClass().getResourceAsStream("/items.csv");
            Files.copy(inputStream, Paths.get(file.getPath()));
        }
        BufferedReader in = new BufferedReader(new FileReader(file));
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            try {
                if (!inputLine.startsWith("#")) {
                    String[] array = inputLine.split(",");
                    String itemName = array[0];
                    int id = Integer.parseInt(array[1]);
                    short data = Short.parseShort(array[2]);
                    items.put(BlockType.getBlockWithData(id, data), itemName.toLowerCase());
                }
            } catch (Exception e) {
                throw new IOException("Error while reading items.csv -- it's probably invalid", e);
            }
        }
    }

    public Map<BlockType, Collection<String>> getItems() {
        return items.asMap();
    }

}
