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

package tech.mcprison.prison.util;

/**
 * This class takes care of the items.csv containing 8000+ different name combinations for blocks.
 *
 * @author Dylan M. Perks
 * @since API 1.0
 */
public class ItemManager {

//    private Multimap<BlockType, String> items;

    
    /**
     * This has not been used for a while.  Will need to provide an alternative way to 
     * add custom blocks.
     * 
     * @throws Exception
     */
//    @Deprecated
//    public ItemManager() throws Exception {
//    	items = ArrayListMultimap.create();
//    /*
//        File file = new File(Prison.get().getDataFolder(), "/items.csv");
//
//        if (!file.exists()) {
//        	try (
//        			// make sure the InputStream is properly closed. May not be 100% needed here:
//        			InputStream inputStream = getClass().getResourceAsStream("/items.csv");
//        			)
//        	{
//        		Files.copy(inputStream, Paths.get(file.getPath()));
//        	}
//            catch (Exception e) {
//            	throw new IOException("Error while copying items.csv from the jar resource to a " +
//            			"file within the plugins directory:", e);
//            }
//        }
//        try (
//        		// Was a memory leak... always must be closed, so the try with resource ensures that it is:
//        		BufferedReader in = new BufferedReader(new FileReader(file));
//        		)
//        {
//        	String inputLine;
//        	
//        	while ((inputLine = in.readLine()) != null) {
//    			if (!inputLine.startsWith("#")) {
//    				String[] array = inputLine.split(",");
//    				String itemName = array[0];
//    				int id = Integer.parseInt(array[1]);
//    				short data = Short.parseShort(array[2]);
//    				items.put(BlockType.getBlockWithData(id, data), itemName.toLowerCase());
//    			}
//        	}
//        	
//        }
//        catch (Exception e) {
//        	throw new IOException("Error while reading items.csv -- it's probably invalid", e);
//        }
//       */
//    }
//
//    public Map<BlockType, Collection<String>> getItems() {
//        return items.asMap();
//    }

}
